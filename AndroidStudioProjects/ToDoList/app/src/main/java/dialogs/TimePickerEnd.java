package dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.to_dolist.R;

import java.util.Calendar;
import java.util.Objects;

public class TimePickerEnd extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int hour ;
    private int minute;

    public TimePickerEnd(int hour, int minute){
        this.hour=hour;
        this.minute=minute;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.MyDialogTheme, this, hour, minute, true);
        return timePickerDialog;

    }
    public interface TimeEnd{
        void timeEnd(int hour, int minute);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimeEnd timeEnd = (TimeEnd) getActivity();
        if (timeEnd != null) {
            timeEnd.timeEnd(hourOfDay, minute);
        } else Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
    }
}
