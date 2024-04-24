package edu.uncc.wellnesstracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.enums.Anchor;
import com.anychart.data.Set;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.uncc.wellnesstracker.databinding.FragmentVisualizeBinding;


public class VisualizeFragment extends Fragment {

    public VisualizeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentVisualizeBinding binding;

    ArrayList<Entry> mEntries = new ArrayList<>();
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVisualizeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    interface VisualizeListener {
        void cancel();
    }

    VisualizeListener mListener;
    List<DataEntry> seriesData = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (VisualizeListener) context;
    }

    AnyChartView anyChartView1;
    AnyChartView anyChartView2;
    AnyChartView anyChartView3;
    AnyChartView anyChartView4;
//    Cartesian cartesian;
//    Set set;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anyChartView1 = binding.anyChartView1;
        anyChartView2 = binding.anyChartView2;
        anyChartView3 = binding.anyChartView3;
        anyChartView4 = binding.anyChartView4;
//        anyChartView.setProgressBar(binding.progressBar);



        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "entries-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mEntries.clear();
        mEntries.addAll(db.entryDao().getAll());

        seriesData.clear();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        Collections.sort(mEntries, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                if (o1.getTime() == null || o2.getTime() == null)
                    return 0;
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        for (Entry entry : mEntries) {
            seriesData.add(new CustomDataEntry(df.format(entry.getTime()),
                    entry.getSleep(), entry.getExercise(), entry.getQuality(), entry.getWeight()));
        }

        visualize("Sleep Time (Hours)", "value", anyChartView1);
        visualize("Exercise Time (Hours)", "value2", anyChartView2);
        visualize("Sleep Quality (1-5)", "value3", anyChartView3);
        visualize("Weight (lb)", "value4", anyChartView4);

    }

    public void visualize(String label, String val, AnyChartView chart) {
        AnyChartView anyChartView = chart;
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Cartesian cartesian = AnyChart.line();
        Set set = Set.instantiate();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        set.data(seriesData);
        Mapping seriesMapping = set.mapAs("{ x: 'x', value: '" + val + "' }");

        Line series = cartesian.line(seriesMapping);
        series.name(label);
        series.hovered().markers().enabled(true);
        series.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        chart.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
        }
    }

}
