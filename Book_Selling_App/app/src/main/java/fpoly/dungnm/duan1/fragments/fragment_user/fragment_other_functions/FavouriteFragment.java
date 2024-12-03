package fpoly.dungnm.duan1.fragments.fragment_user.fragment_other_functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.FavouriteAdapter;
import fpoly.dungnm.duan1.fragments.fragment_user.ProductDetailFragment;
import fpoly.dungnm.duan1.models.Favourite;
import fpoly.dungnm.duan1.models.Product;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouriteFragment extends Fragment {

    private ImageView btnBackFavourite;
    private RecyclerView rvFavourite;
    private ArrayList<Favourite> listFavourite = new ArrayList<>();
    private HttpRequest request;
    private FavouriteAdapter adapter;
    private String username;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        btnBackFavourite = view.findViewById(R.id.btnBackFavourite);
        rvFavourite = view.findViewById(R.id.rvFavourite);

        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        loadData();
        adapter = new FavouriteAdapter(getContext(), listFavourite);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFavourite.setLayoutManager(manager);
        rvFavourite.setAdapter(adapter);

        adapter.setOnItemClickListener(new FavouriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
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
            }

            @Override
            public void onItemLongClick(String id) {
                dialogDeleteFavourite(id);
            }
        });

        btnBackFavourite.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return view;
    }

    private void dialogDeleteFavourite(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            request.callAPI().deleteFavourite(id).enqueue(new Callback<ResponeData<Favourite>>() {
                @Override
                public void onResponse(Call<ResponeData<Favourite>> call, Response<ResponeData<Favourite>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                            dialog.dismiss();
                        } else if (response.body().getStatus() == 404) {
                            Toast.makeText(getContext(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Favourite>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối deleteFavourite" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("er", "Lỗi kết nối deleteFavourite" + t.getMessage());
                }
            });
        });
        builder.setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadData() {
        request.callAPI().getListFavourite(username).enqueue(new Callback<ResponeData<ArrayList<Favourite>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Favourite>>> call, Response<ResponeData<ArrayList<Favourite>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listFavourite.clear();
                        listFavourite.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<Favourite>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối getListFavourite" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("er", "Lỗi kết nối getListFavourite"+ t.getMessage());
            }
        });
    }
}