package fpoly.dungnm.duan1.fragments.fragment_user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.Cart;
import fpoly.dungnm.duan1.models.Favourite;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailFragment extends Fragment {

    private ImageView imgProductDetail, btnMinusQuantity, btnPlusQuantity, btnBackDetail, btnAddFavourite;
    private TextView txtPrice, txtProductName, txtProductAuthor, txtProductDescription, txtQuantity, txtStock;
    private LinearLayout btnAddToCart;
    private String username;
    private Product product;
    private  int quantity = 1;
    private HttpRequest request;

    public ProductDetailFragment() {
        // Required empty public constructor
    }


    public static ProductDetailFragment newInstance() {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        anhXa(view);

        request = new HttpRequest();
        // Nhận dữ liệu từ Fragment A
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
            if (product != null) {
                // Sử dụng dữ liệu sản phẩm
                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                String formattedPrice = formatter.format(product.getPrice()) + "đ";

                Picasso.get().load(product.getImage()).into(imgProductDetail);
                txtPrice.setText(formattedPrice);
                txtProductName.setText(product.getProductname());
                txtProductAuthor.setText(product.getAuthor());
                txtProductDescription.setText(product.getDescription());
                txtStock.setText(String.valueOf(product.getStock()));
                txtQuantity.setText(String.valueOf(quantity));
            }
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        request.callAPI().checkFavourite(username, product.getId()).enqueue(new Callback<ResponeData<Favourite>>() {
            @Override
            public void onResponse(Call<ResponeData<Favourite>> call, Response<ResponeData<Favourite>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        btnAddFavourite.setImageResource(R.drawable.ic_add_favourite_ok);
                    } else {
                        btnAddFavourite.setImageResource(R.drawable.ic_add_favourite);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<Favourite>> call, Throwable t) {
                Log.d("er", "Lỗi kết nối checkFavourite: " + t.getMessage());
            }
        });

        btnAddFavourite.setOnClickListener(v -> {
            Favourite favourite = new Favourite();
            favourite.setUsername(username);
            favourite.setProductID(product);
            request.callAPI().addFavourite(favourite).enqueue(new Callback<ResponeData<Favourite>>() {
                @Override
                public void onResponse(Call<ResponeData<Favourite>> call, Response<ResponeData<Favourite>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            btnAddFavourite.setImageResource(R.drawable.ic_add_favourite_ok);
                            Toast.makeText(getActivity(), "Thêm vào yêu thích thành công", Toast.LENGTH_SHORT).show();
                        } else if (response.body().getStatus() == 400) {
                            btnAddFavourite.setImageResource(R.drawable.ic_add_favourite);
                            Toast.makeText(getActivity(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Favourite>> call, Throwable t) {
                    Log.d("er", "Lỗi kết nối addFavourite: " + t.getMessage());
                }
            });
        });

        btnBackDetail.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnPlusQuantity.setOnClickListener(v -> {
            if (quantity >= product.getStock()) {
                Toast.makeText(getActivity(), "Số lượng tối đa là " + product.getStock(), Toast.LENGTH_SHORT).show();
                return;
            }
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        });

        btnMinusQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(getActivity(), "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            if (product.getStock() == 0 || product.getStatus().equals("Hết hàng")) {
                Toast.makeText(getActivity(), "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
            } else if (product.getStatus().equals("Ngừng bán")) {
                Toast.makeText(getActivity(), "Sản phẩm đã ngưng bán", Toast.LENGTH_SHORT).show();
            } else {
                quantity = Integer.parseInt(txtQuantity.getText().toString());
                Cart cart = new Cart();
                cart.setUsername(username);
                cart.setProductID(product);
                cart.setQuantity(quantity);
                request.callAPI().addProductToCart(cart).enqueue(new Callback<ResponeData<Cart>>() {
                    @Override
                    public void onResponse(Call<ResponeData<Cart>> call, Response<ResponeData<Cart>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(getActivity(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                product.setStock(product.getStock() - quantity);
                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(getActivity(), "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<Cart>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lỗi kết nối addProductToCart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("er", "Lỗi kết nối addProductToCart: " + t.getMessage());
                    }
                });
            }
        });

        return view;
    }

    private void anhXa(View view) {
        imgProductDetail = view.findViewById(R.id.imgProductDetail);
        btnMinusQuantity = view.findViewById(R.id.btnMinusQuantity);
        btnPlusQuantity = view.findViewById(R.id.btnPlusQuantity);
        btnBackDetail = view.findViewById(R.id.btnBackDetail);
        btnAddFavourite = view.findViewById(R.id.btnAddFavourite);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductAuthor = view.findViewById(R.id.txtProductAuthor);
        txtProductDescription = view.findViewById(R.id.txtProductDescription);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtStock = view.findViewById(R.id.txtStock);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
    }


}