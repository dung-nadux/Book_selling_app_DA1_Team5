package fpoly.dungnm.book_selling_app.screens.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterCart;
import fpoly.dungnm.book_selling_app.adapter.AdapterProducts;
import fpoly.dungnm.book_selling_app.models.ModelCart;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.pages.order_payment.OrderPaymentActivity;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    private TextView tvTotalPrice;
    AdapterCart adapter;
    ArrayList<ModelCart> listCart = new ArrayList<>();
    ImageView imgBackCart;
    CartDAO cartDAO;
    Button btnContinue;
    NumberFormat formatter;
    double totalPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        imgBackCart = view.findViewById(R.id.imgBackCart);
        btnContinue = view.findViewById(R.id.btnCheckout);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

        cartDAO = new CartDAO(getContext());
        listCart = cartDAO.getAllCart();

        formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        // Cập nhật tổng giá tiền
        updateTotalPrice();
        String formattedTotalPrice = formatter.format(totalPrice)+"đ";
        tvTotalPrice.setText(formattedTotalPrice);

        // Thiết lập RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new AdapterCart(getContext(), listCart);

        recyclerView.setAdapter(adapter);

        // Cập nhật tổng giá tiền khi có sự thay đổi trong danh sách sản phẩm
        adapter.setOnItemClickListener(new AdapterCart.OnItemClickListener() {
            @Override
            public void onItemClick() {
                updateTotalPrice();
                String formattedTotalPrice = formatter.format(totalPrice)+"đ";
                tvTotalPrice.setText(formattedTotalPrice);
            }
        });

        imgBackCart.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
//        btnContinue.setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), OrderPaymentActivity.class));
//        });

        btnContinue.setOnClickListener(v -> {
            ArrayList<ModelCart> selectedProducts = new ArrayList<>();

            // Lấy các sản phẩm đã được chọn
            for (ModelCart cart : listCart) {
                if (cart.getQuantity() > 0) {
                    selectedProducts.add(cart);
                }
            }

            // Kiểm tra nếu không có sản phẩm nào được chọn
            if (selectedProducts.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển danh sách sản phẩm đã chọn sang màn hình thanh toán
            Intent intent = new Intent(getContext(), OrderPaymentActivity.class);
//            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
            intent.putExtra("selectedProducts", selectedProducts);
            startActivity(intent);
        });

    }

    private double updateTotalPrice() {
        totalPrice = 0;
        for (ModelCart cart : listCart) {
            totalPrice += cart.getAmount();
        }
        return totalPrice;
    }
}