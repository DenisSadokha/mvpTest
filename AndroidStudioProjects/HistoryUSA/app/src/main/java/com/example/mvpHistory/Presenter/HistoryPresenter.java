package com.example.mvpHistory.Presenter;

import com.example.mvpHistory.HistoryContract;
import com.example.mvpHistory.Model.HistoryModel;

public class HistoryPresenter implements HistoryContract.Presenter {
    HistoryContract.View view;

    public HistoryPresenter(HistoryContract.View view) {
        this.view = view;
    }

    @Override
    public void onButtonWasClicked(String text) {
        HistoryContract.Model model = new HistoryModel(this);
        model.loadCount(text);

    }

    @Override
    public void onChange(String text, int state) {
        if(state==0){
            view.showError();
        } else {
            view.showResult(text);
            view.showSuccess();
        }

    }

}
