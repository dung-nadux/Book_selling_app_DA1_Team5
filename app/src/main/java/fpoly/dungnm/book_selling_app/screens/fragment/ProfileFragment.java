package fpoly.dungnm.book_selling_app.screens.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fpoly.dungnm.book_selling_app.MainActivity;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.pages.crud_frofile.crud_adress.AdressActivity;
import fpoly.dungnm.book_selling_app.screens.ScreensActivity;

public class ProfileFragment extends Fragment {
    ImageView imgBackProfile;
    LinearLayout llAdress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgBackProfile = view.findViewById(R.id.imgBackProfile);
        llAdress = view.findViewById(R.id.llAdress);

        imgBackProfile.setOnClickListener(v -> {
//            requireActivity().onBackPressed(); // Quay lại màn hình trước
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        llAdress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdressActivity.class);
            startActivity(intent);
        });

    }

}