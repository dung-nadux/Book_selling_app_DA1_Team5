package fpoly.dungnm.assignment_resapi.fragments;

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

import fpoly.dungnm.assignment_resapi.R;
import fpoly.dungnm.assignment_resapi.adapter.ProductHomeAdapter;
import fpoly.dungnm.assignment_resapi.models.Product;
import fpoly.dungnm.assignment_resapi.models.ResponeData;
import fpoly.dungnm.assignment_resapi.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhoneFragment extends Fragment {
    private RecyclerView rvPhone;
    private HttpRequest request;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private ProductHomeAdapter adapter;

    public PhoneFragment() {
        // Required empty public constructor
    }


    public static PhoneFragment newInstance() {
        PhoneFragment fragment = new PhoneFragment();
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
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        rvPhone = view.findViewById(R.id.rvPhone);

        request = new HttpRequest();
        request.callAPI().getListProductAll().enqueue(callbackGetAllProduct);
        adapter = new ProductHomeAdapter(getContext(), listProduct);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvPhone.setLayoutManager(manager);
        rvPhone.setAdapter(adapter);

        adapter.setOnClick(product -> {
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
                        if (product.getCateID().equals("Phone")) {
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