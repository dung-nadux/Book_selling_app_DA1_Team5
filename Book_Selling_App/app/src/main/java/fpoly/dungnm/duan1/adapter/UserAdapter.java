package fpoly.dungnm.duan1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.models.User;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> listUser;
    private HttpRequest request;

    public UserAdapter(Context mContext, ArrayList<User> listUser) {
        this.mContext = mContext;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = listUser.get(position);
        if(user == null){
            return;
        }
        if (user.getUsername().equals("admin")) {
            holder.item_user.setVisibility(View.GONE);
        }
        request = new HttpRequest();
        holder.tv_fullname.setText(user.getFullname());
        holder.tv_address.setText(user.getAddress());
        holder.tv_numberphone.setText(user.getPhone());
        if (user.getStatus().equals("Banned")) {
            holder.item_user.setBackgroundResource(R.drawable.custom_background_banned);
        } else {
            holder.item_user.setBackgroundResource(R.drawable.custom_background);
        }
        holder.item_user.setOnLongClickListener(v -> {
            if (user.getStatus().equals("Banned")) {
                user.setStatus("Active");
                new AlertDialog.Builder(mContext)
                        .setTitle("Xác nhận hoạt động?")
                        .setMessage("Bạn có chắc chắn muốn unban không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            request.callAPI().updateUser(user).enqueue(new Callback<ResponeData<User>>() {
                                @Override
                                public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus() == 200) {
                                            listUser.set(position, response.body().getData());
                                            notifyItemChanged(position, response.body().getData());
                                            Toast.makeText(mContext, "Unban thành công", Toast.LENGTH_SHORT).show();
                                        } else if (response.body().getStatus() == 404) {
                                            Toast.makeText(mContext, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                                        } else if (response.body().getStatus() == 400) {
                                            Toast.makeText(mContext, "Trống username", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponeData<User>> call, Throwable t) {
                                    Toast.makeText(mContext, "Lỗi kết nối updateUser", Toast.LENGTH_SHORT).show();
                                    Log.e("updateUser","Lỗi kết nối updateUser"+ t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Không", (dialog, which) -> {})
                        .show();
            }else{
                user.setStatus("Banned");
                new AlertDialog.Builder(mContext)
                        .setTitle("Xác nhận ban?")
                        .setMessage("Bạn có chắc chắn muốn ban không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            request.callAPI().updateUser(user).enqueue(new Callback<ResponeData<User>>() {
                                @Override
                                public void onResponse(Call<ResponeData<User>> call, Response<ResponeData<User>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus() == 200) {
                                            listUser.set(position, response.body().getData());
                                            notifyItemChanged(position, response.body().getData());
                                            Toast.makeText(mContext, "Ban thành công", Toast.LENGTH_SHORT).show();
                                        } else if (response.body().getStatus() == 404) {
                                            Toast.makeText(mContext, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                                        } else if (response.body().getStatus() == 400) {
                                            Toast.makeText(mContext, "Trống username", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponeData<User>> call, Throwable t) {
                                    Toast.makeText(mContext, "Lỗi kết nối updateUser", Toast.LENGTH_SHORT).show();
                                    Log.e("updateUser","Lỗi kết nối updateUser"+ t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Không", (dialog, which) -> {})
                        .show();
            }

            return true;
        });

    }

    @Override
    public int getItemCount() {
        if(listUser != null){
            return listUser.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_fullname, tv_address, tv_numberphone;
        private RelativeLayout item_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_numberphone = itemView.findViewById(R.id.tv_numberphone);
            item_user = itemView.findViewById(R.id.item_user);
        }
    }
}
