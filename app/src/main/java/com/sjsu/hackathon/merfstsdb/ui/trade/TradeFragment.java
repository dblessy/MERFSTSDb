package com.sjsu.hackathon.merfstsdb.ui.trade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sjsu.hackathon.merfstsdb.R;
import com.sjsu.hackathon.merfstsdb.databinding.FragmentTradeBinding;

public class TradeFragment extends Fragment {

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
        show.setOnClickListener(view -> {
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
}