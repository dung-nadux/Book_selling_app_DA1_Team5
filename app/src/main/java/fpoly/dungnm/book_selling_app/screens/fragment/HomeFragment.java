package fpoly.dungnm.book_selling_app.screens.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import fpoly.dungnm.book_selling_app.DAO.ProductDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.adapter.AdapterProducts;
import fpoly.dungnm.book_selling_app.models.ModelProducts;


public class HomeFragment extends Fragment implements Filterable {
    RecyclerView rcvHome;
    AdapterProducts adapter;
    ArrayList<ModelProducts> listProducts = new ArrayList<>();
    ProductDAO productDAO;
    private List<ModelProducts> filteredProducts; // Danh sách sản phẩm đã lọc

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvHome = view.findViewById(R.id.rcvHome);

        productDAO = new ProductDAO(getContext());
        listProducts = productDAO.getAllProducts();
        filteredProducts = new ArrayList<>(listProducts); // Khởi tạo danh sách đã lọc

        // Thiết lập RecyclerView
        // Thiết lập RecyclerView với GridLayoutManager
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2); // Số 2 là số cột
        rcvHome.setLayoutManager(manager);

        // adapter = new AdapterProducts(getContext(), listProducts);
         adapter = new AdapterProducts(getContext(), (ArrayList<ModelProducts>) filteredProducts);

//        rcvHome.setAdapter(adapter);
    }

    // Phương thức filter để lọc danh sách sản phẩm
    public void filter(String text) {
        filteredProducts.clear();
        if (text.isEmpty()) {
            filteredProducts.addAll(listProducts); // Nếu không có văn bản tìm kiếm, hiển thị tất cả
        } else {
            text = text.toLowerCase();
            for (ModelProducts product : listProducts) {
                if (product.getTitle().toLowerCase().contains(text) || product.getCategory().toLowerCase().contains(text)) { // Giả sử bạn có phương thức getTitle() trong ModelProducts
                    filteredProducts.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Cập nhật adapter
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<ModelProducts> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listProducts);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ModelProducts product : listProducts) {
                        if (product.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(product);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProducts.clear();
                filteredProducts.addAll((List<ModelProducts>) results.values);
                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }
        };
    }
}