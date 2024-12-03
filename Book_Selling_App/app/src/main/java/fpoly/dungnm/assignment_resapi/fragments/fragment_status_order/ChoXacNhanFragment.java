package fpoly.dungnm.assignment_resapi.fragments.fragment_status_order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.assignment_resapi.R;
import fpoly.dungnm.assignment_resapi.adapter.OrderItemChildAdapter;
import fpoly.dungnm.assignment_resapi.adapter.OrderStatusAdapter;
import fpoly.dungnm.assignment_resapi.models.Order;
import fpoly.dungnm.assignment_resapi.models.OrderViewModel;
import fpoly.dungnm.assignment_resapi.models.ResponeData;
import fpoly.dungnm.assignment_resapi.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChoXacNhanFragment extends Fragment {

    private RecyclerView rvChoXacNhan;
    private HttpRequest request;
    private ArrayList<Order> listOrder = new ArrayList<>();
    private OrderStatusAdapter adapter;
    private String username;
    private OrderViewModel orderViewModel; // Thêm ViewModel

    public ChoXacNhanFragment() {
        // Required empty public constructor
    }


    public static ChoXacNhanFragment newInstance() {
        ChoXacNhanFragment fragment = new ChoXacNhanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("999999999", "onCreate ChoXacNhan");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        rvChoXacNhan = view.findViewById(R.id.rvChoXacNhan);

        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        // Khởi tạo ViewModel
//        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);


        setDaXacNhan();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new OrderStatusAdapter(getContext(), listOrder, this);
        rvChoXacNhan.setLayoutManager(manager);
        rvChoXacNhan.setAdapter(adapter);

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
        request.callAPI().getListOrderByStatus(username, "Chờ xác nhận").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Order>>> call, Response<ResponeData<ArrayList<Order>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listOrder.clear();
                        listOrder.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();

                        // Cập nhật dữ liệu trong ViewModel
//                        orderViewModel.updateOrders(new ArrayList<>(listOrder));
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
        request.callAPI().getAllOrdersByStatus("Chờ xác nhận").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<Order>>> call, Response<ResponeData<ArrayList<Order>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listOrder.clear();
                        listOrder.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();

                        // Cập nhật dữ liệu trong ViewModel
//                        orderViewModel.updateOrders(new ArrayList<>(listOrder));
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
        Log.e("999999999", "onPause ChoXacNhan");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("999999999", "onStop ChoXacNhan");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("999999999", "onResume ChoXacNhan");
        setDaXacNhan();
    }
}