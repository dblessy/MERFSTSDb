package com.sjsu.hackathon.merfstsdb.ui.trade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TradeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TradeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trade fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}