package fpoly.dungnm.duan1.fragments.fragment_status_order;

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
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.OrderStatusAdapter;
import fpoly.dungnm.duan1.models.Order;
import fpoly.dungnm.duan1.models.OrderViewModel;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DaXacNhanFragment extends Fragment {

    private RecyclerView rvDaXacNhan;
    private HttpRequest request;
    private ArrayList<Order> listOrder = new ArrayList<>();
    private OrderStatusAdapter adapter;
    private String username;
    private OrderViewModel orderViewModel; // Thêm ViewModel

    public DaXacNhanFragment() {
        // Required empty public constructor
    }

    public static DaXacNhanFragment newInstance() {
        DaXacNhanFragment fragment = new DaXacNhanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("999999999", "onCreate DaXacNhan");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_da_xac_nhan, container, false);
        rvDaXacNhan = view.findViewById(R.id.rvDaXacNhan);

        // Khởi tạo ViewModel
//        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);


        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        setDaXacNhan();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new OrderStatusAdapter(getContext(), listOrder, this);
        rvDaXacNhan.setLayoutManager(manager);
        rvDaXacNhan.setAdapter(adapter);

        return view;
    }

    private void setDaXacNhan() {
        if (username.equals("admin")) {
            loadDataAdmin();
        } else {
            loadData();
        }
    }

    private void loadData() {
        request.callAPI().getListOrderByStatus(username, "Đã xác nhận").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Order>>> call, Response<ResponeData<ArrayList<Order>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listOrder.clear();
                        listOrder.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<Order>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối getOrderByStatus", Toast.LENGTH_SHORT).show();
                Log.e("222222222222", t.getMessage());
            }
        });
    }

    private void loadDataAdmin() {
        request.callAPI().getAllOrdersByStatus("Đã xác nhận").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Order>>> call, Response<ResponeData<ArrayList<Order>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listOrder.clear();
                        listOrder.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<Order>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối getOrderByStatus", Toast.LENGTH_SHORT).show();
                Log.e("222222222222", t.getMessage());
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("999999999", "onPause DaXacNhan");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("999999999", "onStop DaXacNhan");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("999999999", "onResume DaXacNhan");
        setDaXacNhan();
    }
}