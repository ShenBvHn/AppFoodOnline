package com.example.appfoodmixi.View.Admin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.appfoodmixi.R;

import java.util.ArrayList;
import java.util.List;

public class ChartBillActivity extends AppCompatActivity {
    private BarChart barChart;
    private int dangxuly = 0, danggiaohang = 0, giaohangthanhcong = 0, huyhang = 0;
    private EditText mEdtInput;
    ArrayList<Integer> arrayListColor = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_bill);
        Toolbar toolbar = findViewById(R.id.toolbar);
        barChart = findViewById(R.id.piechart);
        mEdtInput = findViewById(R.id.edtNhapNgay);
        Button mBtnSearch = findViewById(R.id.btnSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        Legend l = barChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        barChart.getDescription().setEnabled(true);
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            arrayListColor.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            arrayListColor.add(color);
        }

        mBtnSearch.setOnClickListener(v -> {
            String date = mEdtInput.getText().toString().trim();
            if (TextUtils.isEmpty(date)) {
                Toast.makeText(ChartBillActivity.this, "Nhập ngày tháng năm để lọc", Toast.LENGTH_SHORT).show();
                loadData("");
            } else {
                loadData(date);
            }
            hideKeyboard(this);
        });
        loadData("");
    }

    public void loadData(String filterDate) {
        dangxuly = 0;
        danggiaohang = 0;
        giaohangthanhcong = 0;
        huyhang = 0;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("HoaDon").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                boolean haveFilterDate = !TextUtils.isEmpty(filterDate);
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Log.d("testfilter", "Hoa don: " + q.getString("UID") + " - ngay: " + q.getString("ngaydat"));
                    if (haveFilterDate) {
                        if (!q.getString("ngaydat").contains(filterDate)) {
                            continue;
                        } else {
                            Log.d("testfilter", "Hoa don: " + q.getString("UID") + " Pass ngay: " + q.getString("ngaydat"));
                        }
                    }
                    if (q.getLong("trangthai") == 1) {
                        dangxuly++;
                    } else if (q.getLong("trangthai") == 2) {
                        danggiaohang++;
                    } else if (q.getLong("trangthai") == 3) {
                        giaohangthanhcong++;
                    } else {
                        huyhang++;
                    }
                }
                List<String> labels = new ArrayList<>();
                labels.add("Đang xử lý");
                labels.add("Đang giao hàng");
                labels.add("Giao Hàng thành công");
                labels.add("Hủy Hàng");

                List<Integer> entryValues = new ArrayList<>();
                if (dangxuly != 0)
                    entryValues.add(dangxuly);
                if (danggiaohang != 0)
                    entryValues.add(danggiaohang);
                if (giaohangthanhcong != 0)
                    entryValues.add(giaohangthanhcong);
                if (huyhang != 0)
                    entryValues.add(huyhang);

                create_graph(labels, entryValues);
            }
        });
    }

    public void create_graph(List<String> graph_label, List<Integer> userScore) {

        try {
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);

            barChart.setDrawGridBackground(false);


            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int) value);
                }
            });

            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


            yAxis.setGranularity(1f);
            yAxis.setGranularityEnabled(true);

            barChart.getAxisRight().setEnabled(false);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(false);
            xAxis.setDrawGridLines(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-70f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(graph_label));

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < userScore.size(); i++) {
                yVals1.add(new BarEntry(i, userScore.get(i)));
            }


            BarDataSet set1;

            if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                set1.setValueTextSize(15f);
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            } else {
                // create 2 datasets with different types
                set1 = new BarDataSet(yVals1, "Trạng Thái Đơn Hàng");
                set1.setColors(arrayListColor);
                set1.setValueTextSize(15f);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                barChart.setData(data);
            }

            barChart.setFitBars(true);

            Legend l = barChart.getLegend();
            l.setFormSize(15f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
            l.setTextSize(12f);
            l.setTextColor(Color.BLACK);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
            barChart.invalidate();

            barChart.animateY(1000);

        } catch (Exception ignored) {
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
