package fpoly.dungnm.duan1.fragments.fragment_admin;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.BestSelling;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.Revenue;
import fpoly.dungnm.duan1.models.UserBest;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Thong_Ke_Fragment extends Fragment {
    private ImageView btnBackThongKe, img_product_best, img_user_best;
    private TextInputEditText edt_from_date, edt_to_date;
    private TextView tv_name_product_best, tv_quantity_product_best, tv_price_product_best, tv_name_user_best, tv_price_user_best, tv_quantity_user_best, tv_total_price, btnThongKe;
    private HttpRequest request;

    public Thong_Ke_Fragment() {
        // Required empty public constructor
    }

    public static Thong_Ke_Fragment newInstance() {
        Thong_Ke_Fragment fragment = new Thong_Ke_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong__ke_, container, false);
        anhXa(view);
        request = new HttpRequest();

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));


        request.callAPI().getBestSellingProducts().enqueue(new Callback<ResponeData<ArrayList<BestSelling>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<BestSelling>>> call, Response<ResponeData<ArrayList<BestSelling>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200) {
                        if (response.body().getData().size() == 0) {
                            tv_name_product_best.setText("Không có sản phẩm nào");
                            tv_quantity_product_best.setText("");
                            tv_price_product_best.setText("");
                        } else {
                            BestSelling bestSelling = response.body().getData().get(0);
                            Picasso.get().load(bestSelling.getProductInfo().getImage()).into(img_product_best);
                            tv_name_product_best.setText(bestSelling.getProductInfo().getProductname());
                            tv_quantity_product_best.setText("Đã bán được: " + bestSelling.getTotalQuantity());
                            String formattedPrice = formatter.format(bestSelling.getProductInfo().getPrice()) + "đ";
                            tv_price_product_best.setText(formattedPrice);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<BestSelling>>> call, Throwable t) {
                Log.e("er", "onFailure: "+t.getMessage() );
            }
        });

        request.callAPI().getTopSpendingCustomers().enqueue(new Callback<ResponeData<ArrayList<UserBest>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<UserBest>>> call, Response<ResponeData<ArrayList<UserBest>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200) {
                        if (response.body().getData().size() == 0) {
                            tv_name_user_best.setText("Không có khách hàng nào");
                            tv_price_user_best.setText("");
                            tv_quantity_user_best.setText("");
                        } else {
                            UserBest userBest = response.body().getData().get(0);
                            tv_name_user_best.setText(userBest.getId());
                            String formattedPrice = formatter.format(userBest.getTotalSpent()) + "đ";
                            tv_price_user_best.setText(formattedPrice);
                            tv_quantity_user_best.setText("Số đơn đã đặt: " + userBest.getOrderCount());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<UserBest>>> call, Throwable t) {
                Log.e("er", "onFailure: "+t.getMessage() );
            }
        });

        Calendar calendar = Calendar.getInstance();
        edt_from_date.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                edt_from_date.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Sự kiện chọn ngày cho End Date
        edt_to_date.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                edt_to_date.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnThongKe.setOnClickListener(v -> {
            String fromDate = edt_from_date.getText().toString();
            String toDate = edt_to_date.getText().toString();
            request.callAPI().getRevenueByDate(fromDate, toDate).enqueue(new Callback<ResponeData<Revenue>>() {
                @Override
                public void onResponse(Call<ResponeData<Revenue>> call, Response<ResponeData<Revenue>> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus() == 200) {
                            Revenue revenue = response.body().getData();
                            String formattedPrice = formatter.format(revenue.getTotalRevenue()) + "đ";
                            tv_total_price.setText(formattedPrice);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Revenue>> call, Throwable t) {
                    Log.e("er", "onFailure: "+t.getMessage() );
                }
            });
        });

        btnBackThongKe.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });


        return view;
    }

    private void anhXa(View view) {
        btnBackThongKe = view.findViewById(R.id.btnBackThongKe);
        edt_from_date = view.findViewById(R.id.edt_from_date);
        edt_to_date = view.findViewById(R.id.edt_to_date);
        tv_name_product_best = view.findViewById(R.id.tv_name_product_best);
        tv_quantity_product_best = view.findViewById(R.id.tv_quantity_product_best);
        tv_price_product_best = view.findViewById(R.id.tv_price_product_best);
        tv_name_user_best = view.findViewById(R.id.tv_name_user_best);
        tv_price_user_best = view.findViewById(R.id.tv_price_user_best);
        tv_quantity_user_best = view.findViewById(R.id.tv_quantity_user_best);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        btnThongKe = view.findViewById(R.id.btnThongKe);
        img_product_best = view.findViewById(R.id.img_product_best);
        img_user_best = view.findViewById(R.id.img_user_best);
    }

}