package com.sjsu.hackathon.merfstsdb.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sjsu.hackathon.merfstsdb.R;
import com.sjsu.hackathon.merfstsdb.databinding.FragmentHomeBinding;
import com.sjsu.hackathon.merfstsdb.ui.Constants;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public static String actor = Constants.GOVT_OFFICER;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button b1 = root.findViewById(R.id.button3);
        b1.setOnClickListener(this);
        Button b2 = root.findViewById(R.id.button4);
        b2.setOnClickListener(this);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                HomeFragment.actor = Constants.GOVT_OFFICER;
                break;
            case R.id.button4:
                HomeFragment.actor = Constants.MAC_ECC;
                break;
        }
    }
}