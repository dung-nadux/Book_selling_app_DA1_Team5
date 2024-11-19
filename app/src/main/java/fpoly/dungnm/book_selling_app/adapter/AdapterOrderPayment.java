package fpoly.dungnm.book_selling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.pages.productdetail.ProductDetailsActivity;

public class AdapterOrderPayment extends RecyclerView.Adapter<AdapterOrderPayment.ViewHoder> {
    Context context;
    ArrayList<ModelProducts> list;
    CartDAO cartDAO;

    public AdapterOrderPayment(Context context, ArrayList<ModelProducts> listSanPham) {
        this.context = context;
        this.list = listSanPham;
        cartDAO = new CartDAO(context);
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_payment, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        ModelProducts product = list.get(position);
        byte[] imageBytes = product.getImage();

//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        holder.itemImage.setImageBitmap(bitmap);

        // Nếu imageBytes không rỗng, giải mã và hiển thị ảnh
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imgAvaterOrderProduct.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không có ảnh
            holder.imgAvaterOrderProduct.setImageResource(R.drawable.ic_launcher_background); // Ví dụ: ảnh mặc định
        }
        holder.tvTitleOrderProduct.setText(product.getTitle());
        holder.tvPriceOrderProduct.setText(String.valueOf(product.getPrice()));
        holder.tvQualityOrderProduct.setText(String.valueOf(product.getQuantity()));


        // Nếu có URL ảnh, bạn có thể sử dụng Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(product.getImage()).into(holder.imgAvaterOrderProduct);


        // chọn xem chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("cartID", product.getId());

            context.startActivity(intent);
        });

        // Xóa sản phẩm khỏi giỏ hàng
        holder.imgDeleteOrderProduct.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Bạn có muốn bỏ sản phẩm này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        cartDAO.deleteCart(product.getId());
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                            })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        });


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgAvaterOrderProduct, imgDeleteOrderProduct;
        TextView tvTitleOrderProduct,tvPriceOrderProduct,tvQualityOrderProduct;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgAvaterOrderProduct = itemView.findViewById(R.id.imgAvaterOrderProduct);
            imgDeleteOrderProduct = itemView.findViewById(R.id.imgDeleteOrderProduct);
            tvTitleOrderProduct = itemView.findViewById(R.id.tvTitleOrderProduct);
            tvPriceOrderProduct = itemView.findViewById(R.id.tvPriceOrderProduct);
            tvQualityOrderProduct = itemView.findViewById(R.id.tvQualityOrderProduct);
        }
    }
}
