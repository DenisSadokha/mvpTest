package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.to_dolist.R;

public class ChooseNorif extends DialogFragment implements View.OnClickListener {
    Button b5, b15, b30, bhour;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_notification_start, container, false);
        b5 = view.findViewById(R.id.before5);
        b15 = view.findViewById(R.id.before15);
        b30 = view.findViewById(R.id.before30);
        bhour = view.findViewById(R.id.beforeHour);
        b5.setOnClickListener(this);
        b15.setOnClickListener(this);
        b30.setOnClickListener(this);
        bhour.setOnClickListener(this);
        return view;

    }

    public interface Notif {
        void setNotif(int before);
    }


    @Override
    public void onClick(View v) {
        Notif notif;
        switch (v.getId()) {
            case R.id.before5:

                notif = (Notif) getActivity();
                assert notif != null;
                notif.setNotif(5);
                dismiss();
                break;
            case R.id.before15:
                notif = (Notif) getActivity();
                notif.setNotif(15);
                dismiss();
                break;
            case R.id.before30:
                notif = (Notif) getActivity();
                notif.setNotif(30);
                dismiss();

                break;
            case R.id.beforeHour:
                notif = (Notif) getActivity();
                notif.setNotif(60);
                dismiss();

                break;
        }

    }
}
