package fpoly.dungnm.book_selling_app.adapter;

import static androidx.core.content.ContextCompat.startActivity;

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
import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.pages.order_payment.OrderPaymentActivity;
import fpoly.dungnm.book_selling_app.pages.productdetail.ProductDetailsActivity;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHoder> {
    Context context;
    ArrayList<ModelProducts> list;
    CartDAO cartDAO;

    public AdapterCart(Context context, ArrayList<ModelProducts> listSanPham) {
        this.context = context;
        this.list = listSanPham;
        cartDAO = new CartDAO(context);
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
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
            holder.imgCartProduct.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không có ảnh
            holder.imgCartProduct.setImageResource(R.drawable.ic_launcher_background); // Ví dụ: ảnh mặc định
        }
        holder.tvCartTitle.setText(product.getTitle());
        holder.tvCartPrice.setText(String.valueOf(product.getPrice()));
        holder.tvCartQuantity.setText(String.valueOf(product.getQuantity()));
//        holder.tvCartQuantity.setText(product.getCategory());

        // Nếu có URL ảnh, bạn có thể sử dụng Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(product.getImage()).into(holder.imgCartProduct);


        // chọn xem chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("cartID", product.getId());

            context.startActivity(intent);
        });

        // Xóa sản phẩm khỏi giỏ hàng
        holder.imgDeleteProduct.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Bạn có muốn xóa sản phẩm này không?")
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

//         Thêm sự kiện cho checkbox
        holder.cbCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked); // Cập nhật trạng thái đã chọn
        });
        
        // Ensure the checkbox reflects the current state
        holder.cbCheck.setChecked(product.isSelected());


        // Cập nhật số lượng từ đối tượng sản phẩm
        holder.tvCartQuantity.setText(String.valueOf(product.getQuantity()));

        // Xử lý sự kiện khi nhấn nút +
        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            product.setQuantity(quantity + 1); // Tăng số lượng
            holder.tvCartQuantity.setText(String.valueOf(product.getQuantity())); // Cập nhật giao diện

            // Cập nhật vào cơ sở dữ liệu nếu cần
            cartDAO.updateQuantity(product.getId(), quantity+1); // Giả sử bạn có phương thức này trong CartDAO
        });

        // Xử lý sự kiện khi nhấn nút -
        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = product.getQuantity();
            if (quantity > 1) {
                product.setQuantity(quantity - 1); // Giảm số lượng
                holder.tvCartQuantity.setText(String.valueOf(product.getQuantity())); // Cập nhật giao diện

                // Cập nhật vào cơ sở dữ liệu nếu cần
                cartDAO.updateQuantity(product.getId(), quantity-1); // Giả sử bạn có phương thức này trong CartDAO
            }
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
        ImageView imgCartProduct, imgDeleteProduct;
        TextView tvCartTitle, tvCartPrice, tvCartQuantity;
        CheckBox cbCheck;
        ImageButton btnDecrease, btnIncrease;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            tvCartTitle = itemView.findViewById(R.id.tvCartTitle);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            cbCheck = itemView.findViewById(R.id.cbCheck);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            imgDeleteProduct = itemView.findViewById(R.id.imgDeleteProduct);
        }
    }
}
