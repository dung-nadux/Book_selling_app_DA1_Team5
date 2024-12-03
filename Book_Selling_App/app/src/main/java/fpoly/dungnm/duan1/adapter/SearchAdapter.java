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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.Product;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context mContext;
    private ArrayList<Product> listProduct;

    private OnItemClickSearch click;
    public interface OnItemClickSearch {
        void onItemClickSearch(Product product);
    }
    public void setOnItemClickSearch(OnItemClickSearch click) {
        this.click = click;
    }
    public SearchAdapter(Context mContext, ArrayList<Product> listProduct) {
        this.mContext = mContext;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Product product = listProduct.get(position);
        if (product == null) {
            return;
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.imgProduct);
        holder.tvProductName.setText(product.getProductname());
        holder.tvProductPrice.setText(formattedPrice);
        holder.tvProductStock.setText("Số lượng: " + product.getStock());

        holder.itemSearch.setOnClickListener(v -> {
            click.onItemClickSearch(product);
        });
    }

    @Override
    public int getItemCount() {
        if (listProduct != null) {
            return listProduct.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName, tvProductPrice, tvProductStock;
        private RelativeLayout itemSearch;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSearch = itemView.findViewById(R.id.item_search);
            tvProductName = itemView.findViewById(R.id.tv_name_product_search);
            imgProduct = itemView.findViewById(R.id.img_product_search);
            tvProductPrice = itemView.findViewById(R.id.tv_price_product_search);
            tvProductStock = itemView.findViewById(R.id.tv_stock_product_search);

        }
    }
}
