package fpoly.dungnm.duan1.fragments.fragment_admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.adapter.UserAdapter;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Manager_User_Fragment extends Fragment {

    private RecyclerView rvUser;
    private ImageView btnBackUser;
    private HttpRequest request;
    private UserAdapter adapter;
    private ArrayList<User> listUser = new ArrayList<>();

    public Manager_User_Fragment() {
        // Required empty public constructor
    }


    public static Manager_User_Fragment newInstance() {
        Manager_User_Fragment fragment = new Manager_User_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_user__admin_, container, false);
        rvUser = view.findViewById(R.id.rvUser);
        btnBackUser = view.findViewById(R.id.btnBackUser);

        request = new HttpRequest();
        adapter = new UserAdapter(getContext(),listUser);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        rvUser.setLayoutManager(manager);
        rvUser.setAdapter(adapter);
        request.callAPI().getListUser().enqueue(new Callback<ResponeData<ArrayList<User>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<User>>> call, Response<ResponeData<ArrayList<User>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listUser.clear();
                        listUser.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(getContext(),"Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeData<ArrayList<User>>> call, Throwable t) {
                Toast.makeText(getContext(),"Không có dữ liệu", Toast.LENGTH_SHORT).show();
                Log.e("Error","Lỗi: "+ t.getMessage());
            }
        });

        btnBackUser.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return view;
    }
}