package fpoly.dungnm.book_selling_app.screens.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import fpoly.dungnm.book_selling_app.R;


public class AnalytistFragment extends Fragment {
    private BarChart barChart;
    ImageView imgBackAnalytist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analytist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChart = view.findViewById(R.id.barChart);
        imgBackAnalytist = view.findViewById(R.id.imgBackAnalytist);

        imgBackAnalytist.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        loadBarChartData();
    }

    private void loadBarChartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 10)); // 25/9
        entries.add(new BarEntry(1, 8));  // 26/9
        entries.add(new BarEntry(2, 6));  // 27/9

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }
}