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


public class DangGiaoHangFragment extends Fragment {

    private RecyclerView rvDangGiaoHang;
    private HttpRequest request;
    private ArrayList<Order> listOrder = new ArrayList<>();
    private OrderStatusAdapter adapter;
    private String username;

    public DangGiaoHangFragment() {
        // Required empty public constructor
    }

    public static DangGiaoHangFragment newInstance() {
        DangGiaoHangFragment fragment = new DangGiaoHangFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("999999999", "onCreate DangGiaoHang");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dang_giao_hang, container, false);
        rvDangGiaoHang = view.findViewById(R.id.rvDangGiaoHang);

        request = new HttpRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");

        setDaXacNhan();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new OrderStatusAdapter(getContext(), listOrder, this);
        rvDangGiaoHang.setLayoutManager(manager);
        rvDangGiaoHang.setAdapter(adapter);
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
        request.callAPI().getListOrderByStatus(username, "Đang giao hàng").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
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
        request.callAPI().getAllOrdersByStatus("Đang giao hàng").enqueue(new Callback<ResponeData<ArrayList<Order>>>() {
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
        Log.e("999999999", "onPause DangGiaoHang");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("999999999", "onStop DangGiaoHang");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("999999999", "onResume DaXacNhan");
        setDaXacNhan();
    }
}