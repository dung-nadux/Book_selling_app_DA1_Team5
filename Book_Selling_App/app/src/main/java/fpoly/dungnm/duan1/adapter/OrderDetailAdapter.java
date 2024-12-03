package fpoly.dungnm.duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.Order;
import fpoly.dungnm.duan1.models.OrderDetail;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDetail> listOrderDetail;
    private HttpRequest request;
    private String username;
    private double totalPrice;
    private int indexDetail;
    private NumberFormat formatter;
    private Order order;
    public OrderDetailAdapter(Context mContext, ArrayList<OrderDetail> listOrderDetail) {
        this.mContext = mContext;
        this.listOrderDetail = listOrderDetail;
    }

    private OnItemClick click;
    public interface OnItemClick {
        void onItemClick(String total);
        void totalPrice(double totalPrice);
    }
    public void setOnItemClick(OnItemClick click) {
        this.click = click;
    }

    @Override
    public int getItemCount() {
        if (listOrderDetail != null) {
            return listOrderDetail.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = listOrderDetail.get(position);
        if (orderDetail == null) {
            return;
        }
        Product product = orderDetail.getProductId();
        formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.imgDetail);
        holder.tvTitle.setText(product.getProductname());
        holder.tvPrice.setText(formattedPrice);
        holder.tvQuantity.setText("Số lượng: " + orderDetail.getQuantity());

        request = new HttpRequest();
        SharedPreferences preferences = mContext.getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");


        String total = formatter.format(tinhTong())+"đ";
        click.onItemClick(total);
        click.totalPrice(tinhTong());

        holder.imgDeleteDetail.setOnClickListener(v -> {
            indexDetail = holder.getAdapterPosition();
            if (listOrderDetail.size() <= 1) {
                Toast.makeText(mContext, "Đơn hàng phải có ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                dialogDeleteDetail(orderDetail.getId());

                String total1 = formatter.format(tinhTong())+"đ";
                click.onItemClick(total1);
                click.totalPrice(tinhTong());
            }
        });
    }

    private void dialogDeleteDetail(String id) {
        new AlertDialog.Builder(mContext)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    request.callAPI().deleteOneDetail(id).enqueue(new Callback<ResponeData<OrderDetail>>() {
                        @Override
                        public void onResponse(Call<ResponeData<OrderDetail>> call, Response<ResponeData<OrderDetail>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    listOrderDetail.remove(indexDetail);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Đã xóa sản phẩm khỏi đơn hàng", Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus() == 404) {
                                    Toast.makeText(mContext, "Không tìm thấy sản phẩm trong đơn hàng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponeData<OrderDetail>> call, Throwable t) {
                            Toast.makeText(mContext, "Lỗi kết nối deleteDetail", Toast.LENGTH_SHORT).show();
                            Log.e("er","Lỗi kết nối deleteDetail"+ t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private double tinhTong() {
        totalPrice = 0;
        for (OrderDetail detail : listOrderDetail) {
            totalPrice += detail.getUnitPrice()* detail.getQuantity();
        }
        return totalPrice;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDetail, imgDeleteDetail;
        private TextView tvTitle, tvPrice, tvQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDetail = itemView.findViewById(R.id.imgOrderDetail);
            imgDeleteDetail = itemView.findViewById(R.id.imgDeleteDetail);
            tvTitle = itemView.findViewById(R.id.tvDetailTitle);
            tvPrice = itemView.findViewById(R.id.tvDetailPrice);
            tvQuantity = itemView.findViewById(R.id.tvDetailQuantity);
        }
    }
}
