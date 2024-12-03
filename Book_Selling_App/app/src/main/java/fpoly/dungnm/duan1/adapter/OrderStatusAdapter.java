package fpoly.dungnm.duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.dungnm.duan1.R;
import fpoly.dungnm.duan1.fragments.fragment_user.ProductDetailFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.ChoXacNhanFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaGiaoHangFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaHuyFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DaXacNhanFragment;
import fpoly.dungnm.duan1.fragments.fragment_status_order.DangGiaoHangFragment;
import fpoly.dungnm.duan1.models.Order;
import fpoly.dungnm.duan1.models.OrderDetail;
import fpoly.dungnm.duan1.models.ResponeData;
import fpoly.dungnm.duan1.service.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Order> listOrder;
    private ArrayList<OrderDetail> listOrderDetail = new ArrayList<>();
    private OrderItemChildAdapter adapter;
    private HttpRequest request;
    private String username;


    private Fragment fragment; // Thêm Fragment

    public OrderStatusAdapter(Context mContext, ArrayList<Order> listOrder, Fragment fragment) {
        this.mContext = mContext;
        this.listOrder = listOrder;
        this.fragment = fragment; // Nhận FragmentManager từ ngoài
    }

    private OnItemClickStatus click;
    public interface OnItemClickStatus {
        void onItemClick(Order order);
    }
    public void setOnItemClick(OnItemClickStatus click) {
        this.click = click;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_order_status, parent, false);
        SharedPreferences preferences = mContext.getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        username = preferences.getString("username", "");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = listOrder.get(position);
        if (order == null) {
            return;
        }
        if (username.equals("admin")) {
            request = new HttpRequest();
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(order.getTotalAmount()) + "đ";
            holder.tvStatus.setText(order.getStatus());
            holder.tvTongTien.setText(formattedPrice);


            ArrayList<OrderDetail> details = order.getOrderDetails();
            if (details == null) {
                loadData(order.getId(), position);
            } else {
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                adapter = new OrderItemChildAdapter(mContext, details);
                holder.rvOrderStatus.setLayoutManager(manager);
                holder.rvOrderStatus.setAdapter(adapter);

                holder.tvToggle.setOnClickListener(v -> {
                    if (holder.tvToggle.getText().equals("Thu gọn")) {
                        int i = 0;
                        for (OrderDetail orderDetail : details) {

                            orderDetail.setExpanded(false);
                            details.set(i, orderDetail);

                            i++;
                        }
                        holder.tvToggle.setText("Xem thêm");
                    } else {
                        int i = 0;
                        for (OrderDetail orderDetail : details) {
                            orderDetail.setExpanded(true);
                            details.set(i, orderDetail);
                            i++;
                        }
                        holder.tvToggle.setText("Thu gọn");
                    }
                    notifyItemChanged(position, details);
                });

                if (fragment instanceof ChoXacNhanFragment) {
                    holder.btnHuyDon.setText("Xác nhận đơn hàng");
                    holder.btnHuyDon.setOnClickListener(v -> {
                        dialogUpdateOrder(order.getId(), "Đã xác nhận", listOrder.indexOf(order));
                    });
                } else if (fragment instanceof DaXacNhanFragment) {
                    holder.btnHuyDon.setText("Đang giao hàng");
                    holder.btnHuyDon.setOnClickListener(v -> {
                        dialogUpdateOrder(order.getId(), "Đang giao hàng", listOrder.indexOf(order));
                    });
                } else if (fragment instanceof DangGiaoHangFragment) {
                    holder.btnHuyDon.setText("Đã giao hàng");
                    holder.btnHuyDon.setOnClickListener(v -> {
                        dialogUpdateOrder(order.getId(), "Đã giao hàng", listOrder.indexOf(order));
                    });
                } else if (fragment instanceof DaGiaoHangFragment){
                    holder.btnHuyDon.setVisibility(View.GONE);
                } else if (fragment instanceof DaHuyFragment) {
                    holder.btnHuyDon.setVisibility(View.GONE);
                }
            }
        } else {


            request = new HttpRequest();
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(order.getTotalAmount()) + "đ";
            holder.tvStatus.setText(order.getStatus());
            holder.tvTongTien.setText(formattedPrice);

            if (fragment instanceof DaHuyFragment) {
                holder.btnHuyDon.setVisibility(View.GONE);
            } else if (fragment instanceof DangGiaoHangFragment) {
                holder.btnHuyDon.setVisibility(View.GONE);
            } else if (fragment instanceof DaGiaoHangFragment) {
                holder.btnHuyDon.setVisibility(View.GONE);
            } else {
                holder.btnHuyDon.setVisibility(View.VISIBLE);
            }

            // Lấy danh sách OrderDetail của đơn hàng hiện tại
            ArrayList<OrderDetail> details = order.getOrderDetails();
            if (details == null) {
                loadData(order.getId(), position);
            } else {
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                adapter = new OrderItemChildAdapter(mContext, details);
                holder.rvOrderStatus.setLayoutManager(manager);
                holder.rvOrderStatus.setAdapter(adapter);

                adapter.setOnItemClick(product -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product", product);
                    ProductDetailFragment fragment1 = new ProductDetailFragment();
                    fragment1.setArguments(bundle);

                    fragment.getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment1)
                            .addToBackStack(null)
                            .commit();
                });


                holder.tvToggle.setOnClickListener(v -> {
                    if (holder.tvToggle.getText().equals("Thu gọn")) {
                        int i = 0;
                        for (OrderDetail orderDetail : details) {

                            orderDetail.setExpanded(false);
                            details.set(i, orderDetail);

                            i++;
                        }
                        holder.tvToggle.setText("Xem thêm");
                    } else {
                        int i = 0;
                        for (OrderDetail orderDetail : details) {
                            orderDetail.setExpanded(true);
                            details.set(i, orderDetail);
                            i++;
                        }
                        holder.tvToggle.setText("Thu gọn");
                    }
                    notifyItemChanged(position, details);
                });
            }
            holder.btnHuyDon.setOnClickListener(v -> {
                dialogUpdateOrder(order.getId(), "Đã hủy", listOrder.indexOf(order));
//                new AlertDialog.Builder(mContext)
//                        .setTitle("Xác nhận hủy đơn hàng")
//                        .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
//                        .setPositiveButton("Có", (dialog, which) -> {
//                            request.callAPI().updateOrder(order.getId(), "Đã hủy").enqueue(new Callback<ResponeData<Order>>() {
//                                @Override
//                                public void onResponse(Call<ResponeData<Order>> call, Response<ResponeData<Order>> response) {
//                                    if (response.isSuccessful()) {
//                                        if (response.body().getStatus() == 200) {
//                                            listOrder.remove(position);
//                                            notifyItemRemoved(position);
//                                            Toast.makeText(mContext, "Đơn hàng đã hủy", Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss();
//                                        } else if (response.body().getStatus() == 404) {
//                                            Toast.makeText(mContext, "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponeData<Order>> call, Throwable t) {
//                                    Toast.makeText(mContext, "Lỗi kết nối updateOrder", Toast.LENGTH_SHORT).show();
//                                    Log.e("Lỗi updateOrder", t.getMessage());
//                                }
//                            });
//                        })
//                        .setNegativeButton("Không", null)
//                        .show();
            });
        }

    }

    private void dialogUpdateOrder(String id, String status, int position) {
        String title = "";
        String message = "";
        String toast = "";
        if (status.equals("Đã xác nhận")) {
            title = "Xác nhận đơn hàng?";
            message = "Bạn có chắc chắn muốn duyệt đơn hàng này?";
            toast = "Đơn hàng đã duyệt";
        } else if (status.equals("Đang giao hàng")) {
            title = "Xác nhận giao hàng?";
            message = "Bạn có chắc chắn muốn giao hàng cho đơn hàng này?";
            toast = "Đơn hàng đã bắt đầu giao";
        } else if (status.equals("Đã giao hàng")) {
            title = "Xác nhận đã giao hàng?";
            message = "Bạn có chắc chắn muốn xác nhận đã giao hàng cho đơn hàng này?";
            toast = "Đơn hàng đã giao";
        } else if (status.equals("Đã hủy")) {
            title = "Xác nhận hủy đơn hàng?";
            message = "Bạn có chắc chắn muốn hủy đơn hàng này?";
            toast = "Đơn hàng đã hủy";
        }
        String finalToast = toast;
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Có", (dialog, which) -> {
                    request.callAPI().updateOrder(id, status).enqueue(new Callback<ResponeData<Order>>() {
                        @Override
                        public void onResponse(Call<ResponeData<Order>> call, Response<ResponeData<Order>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    listOrder.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(mContext, finalToast, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (response.body().getStatus() == 404) {
                                    Toast.makeText(mContext, "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponeData<Order>> call, Throwable t) {
                            Toast.makeText(mContext, "Lỗi kết nối updateOrder", Toast.LENGTH_SHORT).show();
                            Log.e("Lỗi updateOrder", t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void loadData(String id, int position) {
        request.callAPI().getOrderDetails(id).enqueue(new Callback<ResponeData<ArrayList<OrderDetail>>>() {
            @Override
            public void onResponse(Call<ResponeData<ArrayList<OrderDetail>>> call, Response<ResponeData<ArrayList<OrderDetail>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        ArrayList<OrderDetail> details = response.body().getData();
                        listOrder.get(position).setOrderDetails(details); // Lưu vào order hiện tại
                        notifyItemChanged(position); // Cập nhật item hiển thị
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponeData<ArrayList<OrderDetail>>> call, Throwable t) {
                Toast.makeText(mContext, "Lỗi kết nối getOrrderDetail", Toast.LENGTH_SHORT).show();
                Log.e("Lỗi getOrrderDetail", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listOrder != null) {
            return listOrder.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus, tvTongTien, btnHuyDon, tvToggle;
        private RecyclerView rvOrderStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            btnHuyDon = itemView.findViewById(R.id.btnHuyDon);
            rvOrderStatus = itemView.findViewById(R.id.rvOrderStatus);
            tvToggle = itemView.findViewById(R.id.tvToggle);
        }
    }
}
