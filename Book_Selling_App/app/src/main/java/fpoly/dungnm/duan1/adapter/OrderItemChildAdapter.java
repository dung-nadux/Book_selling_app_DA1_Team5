package fpoly.dungnm.duan1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import fpoly.dungnm.duan1.service.HttpRequest;

public class OrderItemChildAdapter extends RecyclerView.Adapter<OrderItemChildAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<OrderDetail> listOrderDetail;
    private HttpRequest request;
    private String username;
    private double totalPrice;
    private int indexDetail;
    private Order order;
    public OrderItemChildAdapter(Context mContext, ArrayList<OrderDetail> listOrderDetail) {
        this.mContext = mContext;
        this.listOrderDetail = listOrderDetail;
    }
    private OnItemClicko click;
    public interface OnItemClicko {
        void onItemClick(Product product);
    }
    public void setOnItemClick(OnItemClicko click) {
        this.click = click;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_order_detail_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = listOrderDetail.get(position);
        if (orderDetail == null) {
            return;
        }
        // Mặc định item đầu tiên được mở rộng
        boolean isExpanded = orderDetail.isExpanded();
        if (position == 0 && !isExpanded) {
            orderDetail.setExpanded(true); // Mở rộng item đầu tiên
            isExpanded = true;
        }

        holder.itemView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.getLayoutParams().height = isExpanded ? 380: 0;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (isExpanded) {
            // Thiết lập margin khi item được mở rộng
            params.setMargins(0, 8, 0, 8); // Left, Top, Right, Bottom
        } else {
            // Thiết lập margin khi item thu gọn
            params.setMargins(0, 0, 0, 0); // Không có margin khi thu gọn
        }
        holder.itemView.setLayoutParams(params);



        Product product = orderDetail.getProductId();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.imgDetail);
        holder.tvTitle.setText(product.getProductname());
        holder.tvPrice.setText(formattedPrice);
        holder.tvQuantity.setText("Số lượng: " + orderDetail.getQuantity());

        request = new HttpRequest();
        SharedPreferences preferences = mContext.getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        if (!username.equals("admin")) {
            holder.item_order_detail.setOnClickListener(v -> {
                click.onItemClick(product);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listOrderDetail != null) {
            return listOrderDetail.size();
        }
        return 0;
    }

    private String tinhTong() {
        totalPrice = 0;
        for (OrderDetail detail : listOrderDetail) {
            totalPrice += detail.getUnitPrice()* detail.getQuantity();
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(totalPrice) + "đ";
        return formattedPrice;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDetail;
        private TextView tvTitle, tvPrice, tvQuantity;
        private RelativeLayout item_order_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDetail = itemView.findViewById(R.id.imgOrderDetailChild);
            tvTitle = itemView.findViewById(R.id.tvDetailTitleChild);
            tvPrice = itemView.findViewById(R.id.tvDetailPriceChild);
            tvQuantity = itemView.findViewById(R.id.tvDetailQuantityChild);
            item_order_detail = itemView.findViewById(R.id.item_order_detail_child);
        }
    }
}
