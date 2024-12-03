package fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.OrderDetailAdapter;
import fpoly.dungnm.duan1.fragments.fragment_user.HomeFragment;
import fpoly.dungnm.duan1.models.Cart;
import fpoly.dungnm.duan1.models.Order;
import fpoly.dungnm.duan1.models.OrderDetail;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StartOrderFragment extends Fragment {

    private ImageView btnBackOrder;
    private LinearLayout btnOrder;
    private RecyclerView rvOrderDetail;
    private TextView tvTotalPriceOrder;
    private String username, password;
    private double balance;
    private HttpRequest request;
    private ArrayList<OrderDetail> listOrderDetail = new ArrayList<>();
    private OrderDetailAdapter adapter;
    private User user;
    private double totalPrice;
    private Order order = new Order();

    public StartOrderFragment() {
        // Required empty public constructor
    }

    public static StartOrderFragment newInstance() {
        StartOrderFragment fragment = new StartOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                request.callAPI().deleteOrder(order.getId()).enqueue(new Callback<ResponeData<Order>>() {
                    @Override
                    public void onResponse(Call<ResponeData<Order>> call, Response<ResponeData<Order>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                // Quay lại màn hình trước đó
                                requireActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<Order>> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi kết nối deleteOrder", Toast.LENGTH_SHORT).show();
                        Log.e("er","Lỗi kết nối deleteOrder"+ t.getMessage());
                    }
                });
            };
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_order, container, false);
        btnBackOrder = view.findViewById(R.id.btnBackOrder);
        btnOrder = view.findViewById(R.id.btnOrder);
        rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
        tvTotalPriceOrder = view.findViewById(R.id.tvTotalPriceOrder);



        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");
        request.callAPI().login(username, password).enqueue(callbackGetUser);

        // Tạo một đơn hàng khi click vào nút "Mua hàng" bên giỏ hàng
        request.callAPI().addOrder(username).enqueue(new Callback<ResponeData<Order>>() {
            @Override
            public void onResponse(Call<ResponeData<Order>> call, Response<ResponeData<Order>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        order = response.body().getData();

                        // Thêm sản phẩm vào đơn hàng
                        request.callAPI().addOrderDetail(username, order.getId()).enqueue(new Callback<ResponeData<ArrayList<OrderDetail>>>() {
                            @Override
                            public void onResponse(Call<ResponeData<ArrayList<OrderDetail>>> call, Response<ResponeData<ArrayList<OrderDetail>>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus() == 200) {
                                        listOrderDetail.clear();
                                        listOrderDetail.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                        tinhTong();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponeData<ArrayList<OrderDetail>>> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi kết nối addOrderDetail", Toast.LENGTH_SHORT).show();
                                Log.e("er","Lỗi kết nối addOrderDetail"+ t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối addOrder", Toast.LENGTH_SHORT).show();
                Log.e("er","Lỗi kết nối addOrder"+ t.getMessage());
            }
        });



        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        rvOrderDetail.setLayoutManager(manager);
        adapter = new OrderDetailAdapter(getContext(), listOrderDetail);
        rvOrderDetail.setAdapter(adapter);

        btnOrder.setOnClickListener(v -> {
            if (!(totalPrice > balance)) {
                request.callAPI().deleteOrderedProductsFromCart(username).enqueue(new Callback<ResponeData<Cart>>() {
                    @Override
                    public void onResponse(Call<ResponeData<Cart>> call, Response<ResponeData<Cart>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(getContext(), "Đơn hàng đã được đặt", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<Cart>> call, Throwable t) {
                        Log.e("er", "Lỗi kết nối deleteOrderedProductsFromCart" + t.getMessage());
                    }
                });
                HomeFragment fragment = new HomeFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment) // ID container của layout
                        .addToBackStack(null) // Để quay lại fragment trước
                        .commit();
            } else {
                Toast.makeText(getContext(), "Số dư không đủ", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClick(new OrderDetailAdapter.OnItemClick() {
            @Override
            public void onItemClick(String total) {
                tvTotalPriceOrder.setText(total);
                tinhTong();
            }

            @Override
            public void totalPrice(double total) {

            }
        });


        btnBackOrder.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return view;
    }
    private double tinhTong() {
        totalPrice = 0;
        for (OrderDetail detail : listOrderDetail) {
            totalPrice += detail.getUnitPrice()* detail.getQuantity();
        }
        return totalPrice;
    }
    Callback<ResponeData<User>> callbackGetUser = new Callback<ResponeData<User>>() {
        @Override
        public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        user = response.body().getData();
                        if (user.getStatus().equals("Banned")) {
                            Toast.makeText(getContext(), "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                        } else {
                            balance = user.getBalance();
                        }
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(getContext(), "Lỗi: trống username hoặc password", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        @Override
        public void onFailure(Call<ResponeData<User>> call, Throwable t) {
            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            Log.e("er", "Lỗi kết nối login: "+ t.getMessage());
        }
    };
}