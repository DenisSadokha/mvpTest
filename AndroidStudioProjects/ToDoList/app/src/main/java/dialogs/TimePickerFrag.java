package dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.xml.sax.helpers.AttributesImpl;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TimePickerFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int hour;
    private int minute;
    public TimePickerFrag(Context context, int hour, int minute) {
        this.context = context;
        this.hour=hour;
        this.minute=minute;

    }


    public interface EditTimeStart{
        void onFinishTimeDialog(int timeStartHour, int timeStartMinute);
    }

    TextView textView;
    TimePicker timePicker;
    Context context;
    Button bl;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, hour, minute, true);
        Objects.requireNonNull(timePickerDialog.getWindow()).setGravity(Gravity.BOTTOM);
        Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return timePickerDialog;

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditTimeStart editTime = (EditTimeStart) getActivity();
        assert editTime != null;
        editTime.onFinishTimeDialog(hourOfDay,minute);

    }
}
