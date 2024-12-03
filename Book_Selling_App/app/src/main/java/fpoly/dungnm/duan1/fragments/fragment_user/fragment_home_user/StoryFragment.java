package fpoly.dungnm.duan1.fragments.fragment_user.fragment_home_user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.ProductHomeAdapter;
import fpoly.dungnm.duan1.fragments.fragment_user.ProductDetailFragment;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryFragment extends Fragment {
    private RecyclerView rvLaptop;
    private HttpRequest request;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private ProductHomeAdapter adapter;

    public StoryFragment() {
        // Required empty public constructor
    }


    public static StoryFragment newInstance() {
        StoryFragment fragment = new StoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_laptop, container, false);
        rvLaptop = view.findViewById(R.id.rvLaptop);
        request = new HttpRequest();
        request.callAPI().getListProductAll().enqueue(callbackGetAllProduct);
        adapter = new ProductHomeAdapter(getContext(), listProduct);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvLaptop.setLayoutManager(manager);
        rvLaptop.setAdapter(adapter);

        adapter.setOnClick(product -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(bundle);
            fragment.setArguments(bundle);

            // Thay thế Fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    Callback<ResponeData<ArrayList<Product>>> callbackGetAllProduct = new Callback<ResponeData<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<ResponeData<ArrayList<Product>>> call, Response<ResponeData<ArrayList<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listProduct.clear();
                    ArrayList<Product> newList = response.body().getData();
                    for (Product product : newList) {
                        if (product.getCateID().equals("Truyện")) {
                            listProduct.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<ArrayList<Product>>> call, Throwable t) {
            Toast.makeText(getActivity(), "Lỗi kết nối getProduct", Toast.LENGTH_SHORT).show();
            Log.e("er", t.getMessage());
        }
    };
}