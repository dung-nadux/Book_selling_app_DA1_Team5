package fpoly.dungnm.assignment_resapi.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import fpoly.dungnm.assignment_resapi.fragments.AccessoryFragment;
import fpoly.dungnm.assignment_resapi.fragments.LaptopFragment;
import fpoly.dungnm.assignment_resapi.fragments.PhoneFragment;

public class ViewPagerHomeAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PhoneFragment();
            case 1:
                return new LaptopFragment();
            case 2:
                return new AccessoryFragment();
            default:
                return new PhoneFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng trang
    }


}
