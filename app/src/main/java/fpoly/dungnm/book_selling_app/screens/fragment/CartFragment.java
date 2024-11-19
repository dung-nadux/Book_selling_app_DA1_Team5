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
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterCart;
import fpoly.dungnm.book_selling_app.adapter.AdapterProducts;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.pages.order_payment.OrderPaymentActivity;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterCart adapter;
    ArrayList<ModelProducts> listCart = new ArrayList<>();
    ImageView imgBackCart;
    CartDAO cartDAO;
    Button btnContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        imgBackCart = view.findViewById(R.id.imgBackCart);
        btnContinue = view.findViewById(R.id.btnCheckout);

        cartDAO = new CartDAO(getContext());
        listCart = cartDAO.getAllCart();

        // Thiết lập RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new AdapterCart(getContext(), listCart);

        recyclerView.setAdapter(adapter);

        imgBackCart.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
//        btnContinue.setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), OrderPaymentActivity.class));
//        });

        btnContinue.setOnClickListener(v -> {
            ArrayList<ModelProducts> selectedProducts = new ArrayList<>();

            // Lấy các sản phẩm đã được chọn
            for (ModelProducts product : listCart) {
                if (product.isSelected()) {
                    selectedProducts.add(product);
                }
            }

            // Kiểm tra nếu không có sản phẩm nào được chọn
            if (selectedProducts.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển danh sách sản phẩm đã chọn sang màn hình thanh toán
            Intent intent = new Intent(getContext(), OrderPaymentActivity.class);
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
            startActivity(intent);
        });

    }
}