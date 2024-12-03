package fpoly.dungnm.duan1.fragments.fragment_user.fragment_other_functions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.SearchAdapter;
import fpoly.dungnm.duan1.fragments.fragment_user.ProductDetailFragment;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private RecyclerView rvSearch;
    private ImageView btnBackSearch, btnSearchProduct;
    private TextInputEditText edtSearchProduct;
    private HttpRequest request;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private SearchAdapter adapter;
    private String search = "";

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        rvSearch = view.findViewById(R.id.rvSearchProduct);
        btnBackSearch = view.findViewById(R.id.btnBackSearch);
        btnSearchProduct = view.findViewById(R.id.btnSearchProduct);
        edtSearchProduct = view.findViewById(R.id.edtSearchProduct);

        request = new HttpRequest();
        adapter = new SearchAdapter(getContext(), listProduct);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        rvSearch.setLayoutManager(manager);
        rvSearch.setAdapter(adapter);
        loadData(search);

        adapter.setOnItemClickSearch(product -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(bundle);

            // Thay thế Fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        btnBackSearch.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        btnSearchProduct.setOnClickListener(v -> {
            search = edtSearchProduct.getText().toString().trim();
            loadData(search);
        });
        return view;
    }

    private void loadData(String search) {
        request.callAPI().getProductByName(search).enqueue(new Callback<ResponeData<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Product>>> call, Response<ResponeData<ArrayList<Product>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listProduct.clear();
                        listProduct.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(getContext(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối search", Toast.LENGTH_SHORT).show();
                Log.e("er", "Lỗi kết nối search"+t.getMessage());
            }
        });
    }


}