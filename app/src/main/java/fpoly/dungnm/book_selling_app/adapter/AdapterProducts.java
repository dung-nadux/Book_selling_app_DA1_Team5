package fpoly.dungnm.book_selling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.models.ModelProducts;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHoder> {
    Context context;
    ArrayList<ModelProducts> list;
    ProductDAO productDAO;

    public AdapterProducts(Context context, ArrayList<ModelProducts> listSanPham) {
        this.context = context;
        this.list = listSanPham;
        productDAO = new ProductDAO(context);
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        ModelProducts product = list.get(position);
        holder.itemTitle.setText(product.getTitle());
        holder.itemAuthor.setText(product.getAuthor());
        holder.itemPrice.setText(String.valueOf(product.getPrice()));
        holder.itemDescription.setText(product.getDescription());
        holder.itemCategory.setText(product.getCategory());

        // Nếu có URL ảnh, bạn có thể sử dụng Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(product.getImage()).into(holder.itemImage);

    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemAuthor, itemPrice, itemDescription, itemCategory;
        ImageView itemImage;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemAuthor = itemView.findViewById(R.id.itemAuthor);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
