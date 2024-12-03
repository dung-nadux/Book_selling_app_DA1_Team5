package fpoly.dungnm.duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import fpoly.dungnm.duan1.models.Cart;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Cart> listCart;
    private HttpRequest request;
    private double totalPrice;
    private String username;
    private int indexCart;

    public CartAdapter(Context mContext, ArrayList<Cart> listCart) {
        this.mContext = mContext;
        this.listCart = listCart;
    }

    private OnItemClick click;
    public interface OnItemClick {
        void onItemClick(String total);
    }
    public void setOnItemClick(OnItemClick click) {
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (listCart != null) {
            return listCart.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if (cart == null) {
            return;
        }
        Product product = cart.getProductID();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + "đ";

        Picasso.get().load(product.getImage()).into(holder.imgCartProduct);
        holder.tvCartTitle.setText(product.getProductname());
        holder.tvCartPrice.setText(formattedPrice);
        holder.tvCartQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.cbCheck.setOnCheckedChangeListener(null); // Loại bỏ listener tạm thời
        holder.cbCheck.setChecked(cart.getStatus() == 1);

        request = new HttpRequest();
        SharedPreferences preferences = mContext.getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        click.onItemClick(tinhTong());
        // Thay đổi checkbox
        holder.cbCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            indexCart = holder.getAdapterPosition();
            cart.setStatus(isChecked?1:0);
            request.callAPI().updateCartProduct(cart).enqueue(updateCartCallback);
            click.onItemClick(tinhTong());
        });
        // Thay đổi số lượng +
        holder.btnPlus.setOnClickListener(v -> {
            indexCart = holder.getAdapterPosition();
            int quantity = cart.getQuantity();
            cart.setQuantity(quantity + 1); // Tăng số lượng
            cart.setAmount(product.getPrice() * (quantity + 1));
            holder.tvCartQuantity.setText(String.valueOf(cart.getQuantity()));

            request.callAPI().updateCartProduct(cart).enqueue(updateCartCallback);

        });
        // Thay đổi số lượng -
        holder.btnMinus.setOnClickListener(v -> {
            indexCart = holder.getAdapterPosition();
            int quantity = cart.getQuantity();
            if (quantity > 1) {
                cart.setQuantity(quantity - 1); // Giảm số lượng
                cart.setAmount(product.getPrice() * (quantity - 1));
                holder.tvCartQuantity.setText(String.valueOf(cart.getQuantity())); // Cập nhật giao diện

                request.callAPI().updateCartProduct(cart).enqueue(updateCartCallback);
                click.onItemClick(tinhTong());
            }
        });
        // Xóa khỏi giỏ hàng
        holder.imgDeleteProduct.setOnClickListener(v -> {
            indexCart = holder.getAdapterPosition();
            dialogDeleteCart(cart.getId());
            click.onItemClick(tinhTong());
        });

    }

    private String tinhTong() {
        totalPrice = 0;
        for (Cart cart : listCart) {
            if (cart.getStatus() == 1) {
                totalPrice += cart.getProductID().getPrice() * cart.getQuantity();
            }
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(totalPrice) + "đ";
        return formattedPrice;
    }

    private void loadData() {
        request.callAPI().getListCart(username).enqueue(new Callback<ResponeData<ArrayList<Cart>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Cart>>> call, Response<ResponeData<ArrayList<Cart>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listCart.clear();
                        listCart.addAll(response.body().getData());
                        notifyDataSetChanged();
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(mContext, "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                        listCart.clear();
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<Cart>>> call, Throwable t) {
                Toast.makeText(mContext, "Lỗi kết nối getListCart", Toast.LENGTH_SHORT).show();
                Log.e("er", t.getMessage());
            }
        });
    }

    private void dialogDeleteCart(String id) {
        new AlertDialog.Builder(mContext)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    request.callAPI().deleteProductFromCart(id).enqueue(new Callback<ResponeData<Cart>>() {
                        @Override
                        public void onResponse(Call<ResponeData<Cart>> call, Response<ResponeData<Cart>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    listCart.remove(indexCart);
                                    notifyItemRemoved(indexCart);
                                    Toast.makeText(mContext, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus() == 404) {
                                    Toast.makeText(mContext, "Không tìm thấy sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponeData<Cart>> call, Throwable t) {
                            Toast.makeText(mContext, "Lỗi khi xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            Log.e("er", "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();



    }

    Callback<ResponeData<Cart>> updateCartCallback = new Callback<ResponeData<Cart>>() {
        @Override
        public void onResponse(Call<ResponeData<Cart>> call, Response<ResponeData<Cart>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Cart updatedCart = response.body().getData();
                    listCart.set(indexCart, updatedCart);
                    notifyItemChanged(indexCart);
                } else if (response.body().getStatus() == 404) {
                    Toast.makeText(mContext, "Không tìm thấy sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Cart>> call, Throwable t) {
            Toast.makeText(mContext, "Lỗi khi cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
            Log.e("er", "Lỗi khi cập nhật giỏ hàng: " + t.getMessage());
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCartProduct, imgDeleteProduct;
        private TextView tvCartTitle, tvCartPrice, tvCartQuantity;
        private CheckBox cbCheck;
        private ImageButton btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            imgDeleteProduct = itemView.findViewById(R.id.imgDeleteProduct);
            tvCartTitle = itemView.findViewById(R.id.tvCartTitle);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            cbCheck = itemView.findViewById(R.id.cbCheck);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
