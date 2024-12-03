package fpoly.dungnm.duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.Product;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product> listProduct;
    private OnItemClickListener click;
    public interface OnItemClickListener {
        void onClick(Product product);
    }

    public void setOnClick(OnItemClickListener click) {
        this.click = click;
    }

    public ProductHomeAdapter(Context mContext, ArrayList<Product> listProduct) {
        this.mContext = mContext;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_product_home, parent,  false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";
        Picasso.get().load(product.getImage()).into(holder.img_product_home);
        holder.tv_name_product_home.setText(product.getProductname());
        holder.tv_price_product_home.setText(formattedPrice);

        holder.item_product_home.setOnClickListener(v -> {
            click.onClick(product);
        });
    }

    @Override
    public int getItemCount() {
        if (listProduct != null) {
            return listProduct.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_product_home;
        private ImageView img_product_home;
        private TextView tv_name_product_home, tv_price_product_home;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_product_home = itemView.findViewById(R.id.item_product_home);
            img_product_home = itemView.findViewById(R.id.img_product_home);
            tv_name_product_home = itemView.findViewById(R.id.tv_name_product_home);
            tv_price_product_home = itemView.findViewById(R.id.tv_price_product_home);
        }
    }
}
