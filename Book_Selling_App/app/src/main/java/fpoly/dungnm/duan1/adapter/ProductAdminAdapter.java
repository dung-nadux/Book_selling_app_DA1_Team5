package fpoly.dungnm.duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.Product;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder> {
    private Context mContext;
    private ArrayList<Product> listProduct;

    private OnItemClickListener onclick;
    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onItemLongClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener onclick) {
        this.onclick = onclick;
    }

    public ProductAdminAdapter(Context mContext, ArrayList<Product> listProduct) {
        this.mContext = mContext;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_product_admin, parent, false);
        return new ProductAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdminViewHolder holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.img_product_admin);
        holder.tv_name_product_admin.setText(product.getProductname());
        holder.tv_price_product_admin.setText(formattedPrice);
        holder.tv_stock_product_admin.setText("Số lượng: "+product.getStock());

        holder.item_product_admin.setOnClickListener(v -> {
            onclick.onItemClick(product);
        });

        holder.item_product_admin.setOnLongClickListener(v -> {
            onclick.onItemLongClick(product);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        if (listProduct != null) {
            return listProduct.size();
        }
        return 0;
    }

    public static final class ProductAdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_product_admin;
        private TextView tv_price_product_admin, tv_stock_product_admin, tv_name_product_admin;
        private RelativeLayout item_product_admin;
        public ProductAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product_admin = itemView.findViewById(R.id.img_product_admin);
            tv_price_product_admin = itemView.findViewById(R.id.tv_price_product_admin);
            tv_stock_product_admin = itemView.findViewById(R.id.tv_stock_product_admin);
            tv_name_product_admin = itemView.findViewById(R.id.tv_name_product_admin);
            item_product_admin = itemView.findViewById(R.id.item_product_admin);
        }
    }
}
