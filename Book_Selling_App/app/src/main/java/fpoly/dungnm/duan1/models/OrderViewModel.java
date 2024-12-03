package fpoly.dungnm.duan1.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class OrderViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Order>> orders = new MutableLiveData<>();

    public LiveData<ArrayList<Order>> getOrders() {
        return orders;
    }

    public void updateOrders(ArrayList<Order> updatedOrders) {
        orders.setValue(updatedOrders);
    }
}
