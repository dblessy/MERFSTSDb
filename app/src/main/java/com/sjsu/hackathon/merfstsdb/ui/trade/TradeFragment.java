package com.sjsu.hackathon.merfstsdb.ui.trade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sjsu.hackathon.merfstsdb.DBHandler;
import com.sjsu.hackathon.merfstsdb.Data;
import com.sjsu.hackathon.merfstsdb.DataListener;
import com.sjsu.hackathon.merfstsdb.FetchData;
import com.sjsu.hackathon.merfstsdb.MainActivity;
import com.sjsu.hackathon.merfstsdb.R;
import com.sjsu.hackathon.merfstsdb.databinding.FragmentTradeBinding;

import java.util.ArrayList;
import java.util.Collections;

public class TradeFragment extends Fragment implements DataListener {

    private FragmentTradeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TradeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(TradeViewModel.class);

        binding = FragmentTradeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textTrade;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button show = root.findViewById(R.id.trade_show);
        Layer formLayout = root.findViewById(R.id.form_layer);
        Layer chartLayout = root.findViewById(R.id.trade_layer);

        String startYear = "1990";
        String endYear = "2020";
        String country = MainActivity.country;

        chartLayout.setVisibility(View.INVISIBLE);

        GraphView graph = root.findViewById(R.id.graph);

        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        //Build Graph
        //Default show 1980 to 2020
        FetchData fd = new FetchData();

        show.setOnClickListener(view -> {

            CheckBox gdpBox = root.findViewById(R.id.reserves);
            CheckBox gniBox = root.findViewById(R.id.gni);
            CheckBox totalBox = root.findViewById(R.id.total_debt);
            CheckBox gniCurrBox = root.findViewById(R.id.gni_curr);

            if(gdpBox.isChecked()){//first box
                fd.getData(new DBHandler(this.getContext()),"Reserves", startYear, endYear, country, this);
            }

            if(gniBox.isChecked()){//second
                fd.getData(new DBHandler(this.getContext()),"GNI", startYear, endYear, country, this);
            }

            if(totalBox.isChecked()){//third box
                fd.getData(new DBHandler(this.getContext()),"Total Debt", startYear, endYear, country, this);
            }

            if(gniCurrBox.isChecked()){
                fd.getData(new DBHandler(this.getContext()),"GNI Current", startYear, endYear, country, this);
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
        //series.setColor(Color.GREEN);
        graph.addSeries(series);

    }

    @Override
    public void onDataFail(String reason) {

        System.out.println(reason);

    }
}