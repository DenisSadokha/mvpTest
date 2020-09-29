package com.example.mvpHistory;

public interface HistoryContract {
    interface View {
        void showResult(String result);

        void showSuccess();

        void showError();

    }

    interface Model {

        void loadCount(String text);


    }

    interface Presenter {
        void onButtonWasClicked(String text);
        void onChange(String text, int state);
    }
}
