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
import fpoly.dungnm.duan1.models.Favourite;
import fpoly.dungnm.duan1.models.Product;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Favourite> listFavourite;
    public FavouriteAdapter(Context mContext, ArrayList<Favourite> listFavourite) {
        this.mContext = mContext;
        this.listFavourite = listFavourite;
    }
    private OnItemClickListener onclick;
    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onItemLongClick(String id);
    }

    public void setOnItemClickListener(OnItemClickListener onclick) {
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favourite favourite = listFavourite.get(position);
        if (favourite == null) {
            return;
        }
        Product product = favourite.getProductID();

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.img_product_favourite);
        holder.tv_name_product_favourite.setText(product.getProductname());
        holder.tv_price_product_favourite.setText(formattedPrice);
        holder.tv_stock_product_favourite.setText(String.valueOf(product.getStock()));

        holder.item_product_favourite.setOnClickListener(v -> {
            onclick.onItemClick(product);
        });

        holder.item_product_favourite.setOnLongClickListener(v -> {
            onclick.onItemLongClick(favourite.getId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (listFavourite != null) {
            return listFavourite.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_product_favourite;
        private TextView tv_price_product_favourite, tv_stock_product_favourite, tv_name_product_favourite;
        private RelativeLayout item_product_favourite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product_favourite = itemView.findViewById(R.id.img_product_favourite);
            tv_price_product_favourite = itemView.findViewById(R.id.tv_price_product_favourite);
            tv_stock_product_favourite = itemView.findViewById(R.id.tv_stock_product_favourite);
            tv_name_product_favourite = itemView.findViewById(R.id.tv_name_product_favourite);
            item_product_favourite = itemView.findViewById(R.id.item_product_favourite);

        }
    }
}
