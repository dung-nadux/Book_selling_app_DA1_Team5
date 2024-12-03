package fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.CartAdapter;
import fpoly.dungnm.duan1.models.Cart;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    private ImageView btnBackCart;
    private TextView tvTotalPrice;
    private RecyclerView rvCart;
    private LinearLayout btnOrder;
    private HttpRequest request;
    private CartAdapter adapter;
    private String username;
    private double totalPrice;
    private ArrayList<Cart> listCart = new ArrayList<>();
    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btnBackCart = view.findViewById(R.id.btnBackCart);
        rvCart = view.findViewById(R.id.rvCart);
        btnOrder = view.findViewById(R.id.btnOrderCart);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        loadData();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        adapter = new CartAdapter(getContext(), listCart);
        rvCart.setLayoutManager(manager);
        rvCart.setAdapter(adapter);
        adapter.setOnItemClick(total -> {
            tvTotalPrice.setText(total);
        });
        btnBackCart.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnOrder.setOnClickListener(v -> {
            ArrayList<Cart> cartSelected = new ArrayList<>();
            for (Cart cart : listCart) {
                if (cart.getStatus() == 1) {
                    cartSelected.add(cart);
                }
            }
            if (cartSelected.size() == 0) {
                Toast.makeText(getContext(), "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                StartOrderFragment fragment = new StartOrderFragment();
                // Truyền dữ liệu qua Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartSelected", cartSelected);
                fragment.setArguments(bundle);
                // Thay thế fragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment) // ID container của layout
                        .addToBackStack(null) // Để quay lại fragment trước
                        .commit();
            }
        });

        return view;
    }

    private void loadData() {
        request.callAPI().getListCart(username).enqueue(getListCart);
    }


    Callback<ResponeData<ArrayList<Cart>>> getListCart = new Callback<ResponeData<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<ResponeData<ArrayList<Cart>>> call, Response<ResponeData<ArrayList<Cart>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listCart.clear();
                    listCart.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else if (response.body().getStatus() == 404) {
                    Toast.makeText(getContext(), "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<ArrayList<Cart>>> call, Throwable t) {
            Toast.makeText(getContext(), "Lỗi kết nối getListCart", Toast.LENGTH_SHORT).show();
            Log.e("er", t.getMessage());
        }
    };
}