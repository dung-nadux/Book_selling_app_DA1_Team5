package fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.ViewPagerOrderStatusAdapter;
import fpoly.dungnm.duan1.fragments.fragment_status_order.ChoXacNhanFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaGiaoHangFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaHuyFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaXacNhanFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DangGiaoHangFragment;


public class OrderFragment extends Fragment {

    private ImageView btnBackOrderStatus;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        btnBackOrderStatus = view.findViewById(R.id.btnBackOrderStatus);
        tabLayout = view.findViewById(R.id.tabLayoutOrderStatus);
        viewPager2 = view.findViewById(R.id.viewPager2OrderStatus);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChoXacNhanFragment());
        fragments.add(new DaXacNhanFragment());
        fragments.add(new DangGiaoHangFragment());
        fragments.add(new DaGiaoHangFragment());
        fragments.add(new DaHuyFragment());

        ViewPagerOrderStatusAdapter pagerAdapter = new ViewPagerOrderStatusAdapter(getActivity(), fragments);
        viewPager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, i) -> {
            switch (i) {
                case 0: tab.setText("Chờ xác nhận"); break;
                case 1: tab.setText("Đã xác nhận"); break;
                case 2: tab.setText("Đang giao hàng"); break;
                case 3: tab.setText("Đã giao hàng"); break;
                case 4: tab.setText("Đã hủy"); break;
            }
        }).attach();

        btnBackOrderStatus.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return view;
    }
}