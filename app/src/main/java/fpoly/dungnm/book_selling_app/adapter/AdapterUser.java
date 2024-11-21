package fpoly.dungnm.book_selling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.DAO.CartDAO;
import fpoly.dungnm.book_selling_app.R;
import fpoly.dungnm.book_selling_app.models.ModelProducts;
import fpoly.dungnm.book_selling_app.models.ModelUser;
import fpoly.dungnm.book_selling_app.pages.productdetail.ProductDetailsActivity;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHoder> {
    Context context;
    private ArrayList<ModelUser> userList;

    public AdapterUser(Context context, ArrayList<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        ModelUser user = userList.get(position);
        holder.tvUsername.setText(user.getFullname());
        holder.tvUserEmail.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {
        ImageView menuUser;
        TextView tvUsername, tvUserEmail;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            menuUser = itemView.findViewById(R.id.menuUser);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);

        }
    }
}
