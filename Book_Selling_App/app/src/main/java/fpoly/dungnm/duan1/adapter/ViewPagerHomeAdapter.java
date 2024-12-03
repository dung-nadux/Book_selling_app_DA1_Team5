package fpoly.dungnm.duan1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import fpoly.dungnm.duan1.fragments.fragment_user.fragment_home_user.AcademicFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_home_user.StoryFragment;
import fpoly.dungnm.duan1.fragments.fragment_user.fragment_home_user.LifeFragment;

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
                return new LifeFragment();
            case 1:
                return new StoryFragment();
            case 2:
                return new AcademicFragment();
            default:
                return new LifeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng trang
    }


}
