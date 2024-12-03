package fpoly.dungnm.duan1.fragments.fragment_user;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.ViewPagerHomeAdapter;
import fpoly.dungnm.duan1.fragments.ProfileFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_other_functions.SearchFragment;


public class HomeFragment extends Fragment {

    private ImageView btnProfile, tab_phone, tab_laptop, tab_accessory, btnSearchHome;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private LinearLayout customTabLayout;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lắng nghe sự kiện nút Back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog(); // Hiển thị hộp thoại xác nhận
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa(view);

        ViewPagerHomeAdapter adapter = new ViewPagerHomeAdapter(getActivity());
        viewPager.setAdapter(adapter);

        // Lắng nghe sự kiện chuyển đổi trang của ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Thay đổi trạng thái của các tab (nút bấm)
                tab_phone.setImageResource(position == 0 ? R.drawable.btn_life_on : R.drawable.btn_life_off);
                tab_laptop.setImageResource(position == 1 ? R.drawable.btn_story_on : R.drawable.btn_story_off);
                tab_accessory.setImageResource(position == 2 ? R.drawable.btn_academic_on : R.drawable.btn_academic_off);
            }
        });

        // Lắng nghe sự kiện click trên custom tabs
        tab_phone.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tab_laptop.setOnClickListener(v -> viewPager.setCurrentItem(1));
        tab_accessory.setOnClickListener(v -> viewPager.setCurrentItem(2));
        // animation
//        viewPager.setPageTransformer((page, position) -> {
//            page.setAlpha(1 - Math.abs(position));
//            page.setTranslationX(-position * page.getWidth());
//        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
//                 Chuyển sang ProfileFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, profileFragment) // Thay thế Fragment hiện tại
                    .addToBackStack(null) // Lưu lại trạng thái của HomeFragment
                    .commit();
            }
        });

        btnSearchHome.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void anhXa(View view) {
        btnProfile = view.findViewById(R.id.btnProfile);
        viewPager = view.findViewById(R.id.viewPager);
        customTabLayout = view.findViewById(R.id.customTabLayout);
        tab_phone = view.findViewById(R.id.tab_phone);
        tab_laptop = view.findViewById(R.id.tab_laptop);
        tab_accessory = view.findViewById(R.id.tab_accessory);
        btnSearchHome = view.findViewById(R.id.btnSearchHome);
    }
    private void showExitConfirmationDialog() {
        // Tạo hộp thoại xác nhận
        new AlertDialog.Builder(requireContext())
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Thoát ứng dụng
                    requireActivity().finish();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss(); // Đóng hộp thoại
                })
                .show();
    }
}