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
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DaHuyFragment extends Fragment {

    private RecyclerView rvDaHuy;
    private HttpRequest request;
    private ArrayList<Order> listOrder = new ArrayList<>();
    private OrderStatusAdapter adapter;
    private String username;

    public DaHuyFragment() {
        // Required empty public constructor
    }

    public static DaHuyFragment newInstance() {
        DaHuyFragment fragment = new DaHuyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("999999999", "onCreate DaHuy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_da_huy, container, false);
        rvDaHuy = view.findViewById(R.id.rvDaHuy);

        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        setDaXacNhan();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new OrderStatusAdapter(getContext(), listOrder, this);
        rvDaHuy.setLayoutManager(manager);
        rvDaHuy.setAdapter(adapter);
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
        request.callAPI().getListOrderByStatus(username, "Đã hủy").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
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
        request.callAPI().getAllOrdersByStatus("Đã hủy").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
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
        Log.e("999999999", "onPause DaHuy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("999999999", "onStop DaHuy");
    }

    @Override
    public void onResume() {
        super.onResume();
        setDaXacNhan();
    }
}