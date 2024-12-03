package fpoly.dungnm.duan1.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.fragments.fragment_admin.Order_Admin_Fragment;
import fpoly.dungnm.duan1.fragments.fragment_admin.Product_Admin_Fragment;
import fpoly.dungnm.duan1.fragments.ProfileFragment;
import fpoly.dungnm.duan1.fragments.fragment_admin.Thong_Ke_Fragment;
import fpoly.dungnm.duan1.fragments.fragment_admin.Manager_User_Fragment;

public class MainAdminActivity extends AppCompatActivity {

    private LinearLayout nav_product, nav_order_admin, nav_thongke, nav_user_admin, nav_bar_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();

        loadFragment(new Product_Admin_Fragment());

        // Xử lý sự kiện click trên nav
        nav_product.setOnClickListener(v -> loadFragment(new Product_Admin_Fragment()));
        nav_order_admin.setOnClickListener(v -> loadFragment(new Order_Admin_Fragment()));
        nav_thongke.setOnClickListener(v -> loadFragment(new Thong_Ke_Fragment()));
        nav_user_admin.setOnClickListener(v -> loadFragment(new Manager_User_Fragment()));

        // Lắng nghe thay đổi fragment
        getSupportFragmentManager().addOnBackStackChangedListener(this::onFragmentChanged);
    }

    private void anhXa() {
        nav_product = findViewById(R.id.nav_product);
        nav_order_admin = findViewById(R.id.nav_order_admin);
        nav_thongke = findViewById(R.id.nav_thongke);
        nav_user_admin = findViewById(R.id.nav_user_admin);
        nav_bar_admin = findViewById(R.id.nav_bar_admin);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_admin, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void onFragmentChanged() {
        // Lấy fragment hiện tại
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof ProfileFragment) {
            // Ẩn navigation bar nếu là ProfileFragment
            nav_bar_admin.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof Thong_Ke_Fragment) {
            nav_bar_admin.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof Manager_User_Fragment) {
            nav_bar_admin.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof Order_Admin_Fragment) {
            nav_bar_admin.setVisibility(LinearLayout.GONE);
        }else {
            // Hiển thị navigation bar nếu không phải ProfileFragment
            nav_bar_admin.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container_admin);
    }
}