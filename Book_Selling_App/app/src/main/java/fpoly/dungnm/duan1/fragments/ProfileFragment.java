package fpoly.dungnm.duan1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.activities.LoginActivity;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageView btnBackProfile;
    private TextInputEditText edtFulName, edtPhone, edtAddress, edtBalanceProfile;
    private Button btnSave;
    private TextView tvDangXuat;
    private String username, password;
    private SharedPreferences preferences;
    private HttpRequest request;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnBackProfile = view.findViewById(R.id.btnBackProfile);
        tvDangXuat = view.findViewById(R.id.tvDangXuat);
        edtFulName = view.findViewById(R.id.edtFullNameProfile);
        edtPhone = view.findViewById(R.id.edtPhoneProfile);
        edtAddress = view.findViewById(R.id.edtAddressProfile);
        btnSave = view.findViewById(R.id.btnSaveProfile);
        edtBalanceProfile = view.findViewById(R.id.edtBalanceProfile);

        request = new HttpRequest();
        preferences = getActivity().getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");

        if (username.equals("admin")) {
            edtBalanceProfile.setVisibility(View.GONE);
        }
        request.callAPI().login(username, password).enqueue(callbackGetUser);

        btnSave.setOnClickListener(v -> {
            String fullname = edtFulName.getText().toString();
            String address = edtAddress.getText().toString();
            String phone = edtPhone.getText().toString();
            String balance = edtBalanceProfile.getText().toString();
            if (fullname.isEmpty() || address.isEmpty() || phone.isEmpty() || balance.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!phone.matches("\\d{10}")) {
                Toast.makeText(getContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!balance.matches("\\d+")) {
                Toast.makeText(getContext(), "Số dư không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            float balanceFloat = Float.parseFloat(balance);
            User user = new User();
            user.setUsername(username);
            user.setFullname(fullname);
            user.setAddress(address);
            user.setPhone(phone);
            user.setBalance(balanceFloat);
            request.callAPI().updateUser(user).enqueue(new Callback<ResponeData<User>>() {
                @Override
                public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                User userNew = response.body().getData();
                                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                preferences.edit().putFloat("balance", (float) userNew.getBalance()).apply();
                            } else if (response.body().getStatus() == 400) {
                                Toast.makeText(getContext(), "Lỗi: trống username hoặc password", Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(getContext(), "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponeData<User>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối updateUser", Toast.LENGTH_SHORT).show();
                    Log.e("er", "Lỗi kết nối updateUser: " + t.getMessage());
                }
            });
        });

        tvDangXuat.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn thoát không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username","0");
                        editor.putString("password","0");
                        editor.putFloat("balance", 0);
                        editor.apply();
                        // Chuyển sang màn hình LoginActivity
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish(); // Kết thúc activity hiện tại nếu cần
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        // Đóng dialog nếu người dùng chọn "Không"
                        dialog.dismiss();
                    })
                    .show();
        });

        btnBackProfile.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        return view;
    }

    Callback<ResponeData<User>> callbackGetUser = new Callback<ResponeData<User>>() {
        @Override
        public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        User user = response.body().getData();
                        if (user.getStatus().equals("Banned")) {
                            Toast.makeText(getContext(), "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                        } else {
                            edtFulName.setText(user.getFullname());
                            edtPhone.setText(user.getPhone());
                            edtAddress.setText(user.getAddress());
                            edtBalanceProfile.setText(String.valueOf((int) user.getBalance()));
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putFloat("blance", (float) user.getBalance());
                        }
                    } else if (response.body().getStatus() == 400) {
                        Toast.makeText(getContext(), "Lỗi: trống username hoặc password", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 404) {
                        Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        @Override
        public void onFailure(Call<ResponeData<User>> call, Throwable t) {
            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            Log.e("er", "Lỗi kết nối login: "+ t.getMessage());
        }
    };
}