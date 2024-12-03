package fpoly.dungnm.duan1;

import android.os.Bundle;
import android.widget.LinearLayout;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order.CartFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_other_functions.FavouriteFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.HomeFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order.OrderFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_other_functions.SearchFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_cart_and_order.StartOrderFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.ProductDetailFragment;
import fpoly.dungnm.duan1.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout nav_home, nav_cart, nav_order, nav_favourite, nav_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();

        // Load fragment mặc định
        loadFragment(new HomeFragment());

        // Xử lý sự kiện click trên nav
        nav_home.setOnClickListener(v -> loadFragment(new HomeFragment()));
        nav_cart.setOnClickListener(v -> loadFragment(new CartFragment()));
        nav_order.setOnClickListener(v -> loadFragment(new OrderFragment()));
        nav_favourite.setOnClickListener(v -> loadFragment(new FavouriteFragment()));

        // Lắng nghe thay đổi fragment
        getSupportFragmentManager().addOnBackStackChangedListener(this::onFragmentChanged);


    }

    private void anhXa() {
        nav_home = findViewById(R.id.nav_home);
        nav_cart = findViewById(R.id.nav_cart);
        nav_order = findViewById(R.id.nav_order);
        nav_favourite = findViewById(R.id.nav_favourite);
        nav_bar = findViewById(R.id.nav_bar);
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void onFragmentChanged() {
        // Lấy fragment hiện tại
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof ProfileFragment) {
            // Ẩn navigation bar nếu là ProfileFragment
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof ProductDetailFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof CartFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof StartOrderFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof FavouriteFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof OrderFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else if (currentFragment instanceof SearchFragment) {
            nav_bar.setVisibility(LinearLayout.GONE);
        } else {
            // Hiển thị navigation bar nếu không phải ProfileFragment
            nav_bar.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }
}