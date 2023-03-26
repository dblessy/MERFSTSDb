package com.sjsu.hackathon.merfstsdb.ui.dashboard;

import android.content.Context;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sjsu.hackathon.merfstsdb.DBHandler;
import com.sjsu.hackathon.merfstsdb.Data;
import com.sjsu.hackathon.merfstsdb.DataListener;
import com.sjsu.hackathon.merfstsdb.FetchData;
import com.sjsu.hackathon.merfstsdb.MainActivity;
import com.sjsu.hackathon.merfstsdb.R;
import com.sjsu.hackathon.merfstsdb.databinding.FragmentDashboardBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment implements DataListener {

    private FragmentDashboardBinding binding;
    //final Context context=this;
    private boolean GDP = false;
    private boolean FDII = false;
    private boolean FDIO = false;
    private boolean IEF = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button show = root.findViewById(R.id.mac_show);

        String startYear = "1990";
        String endYear = "2020";
        String country = MainActivity.country;

        System.out.println(country);

        Layer formLayout = root.findViewById(R.id.mac_layer);
        Layer chartLayout = root.findViewById(R.id.mac_chart_layer);
        chartLayout.setVisibility(View.INVISIBLE);

        GraphView graph = root.findViewById(R.id.graph);

        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        //Build Graph
        //Default show 1980 to 2020
        FetchData fd = new FetchData();

        System.out.println("---------------------"+GDP);

        //fd.getData(new DBHandler(this.getContext()),"Fertilizers", startYear, endYear, country, this);

        show.setOnClickListener(view -> {
            CheckBox gdpBox = root.findViewById(R.id.reserves);
            System.out.println("___________"+gdpBox.isChecked());
            GDP = gdpBox.isChecked();
            CheckBox gniBox = root.findViewById(R.id.gni);
            FDII = gniBox.isChecked();
            CheckBox totalBox = root.findViewById(R.id.total_debt);
            FDIO = totalBox.isChecked();
            CheckBox currBox = root.findViewById(R.id.gni_curr);
            IEF = currBox.isChecked();

            if(GDP){
                fd.getData(new DBHandler(this.getContext()),"GDP", startYear, endYear, country, this);
            }

            if(FDII){
                fd.getData(new DBHandler(this.getContext()),"FDI Inflows", startYear, endYear, country, this);
            }

            if(FDIO){
                fd.getData(new DBHandler(this.getContext()),"FDI Outflows", startYear, endYear, country, this);
            }

            formLayout.setVisibility(View.INVISIBLE);
            chartLayout.setVisibility(View.VISIBLE);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDataFinish(ArrayList<Data> dataList) {
        View root = binding.getRoot();
        GraphView graph = root.findViewById(R.id.graph);
        System.out.println(dataList);
        ArrayList<DataPoint> x = new ArrayList<DataPoint>();
        Collections.reverse(dataList);
        DataPoint[] myData = new DataPoint[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
                myData[i] = new DataPoint(Integer.parseInt(dataList.get(i).getYear()), dataList.get(i).getData());

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(myData);
        graph.addSeries(series);

    }

    @Override
    public void onDataFail(String reason) {

        System.out.println(reason);

    }
}