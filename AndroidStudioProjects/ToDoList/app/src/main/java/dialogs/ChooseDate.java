package dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.to_dolist.MainActivity;
import com.example.to_dolist.ToDoList;

import java.util.Calendar;
import java.util.Objects;

public class ChooseDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int day;
    private int month;
    private int year;
    public ChooseDate(int day, int month, int year){
        this.day=day;
        this.month=month;
        this.year=year;


    }
    public interface CheckAdapter{
        void upDateAdapter(boolean check);
    }
    public interface DateChoose{
        void chooseDate(int day, int month,int year);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
      final DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),this,year,month,day);
      return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateChoose dateChoose =(DateChoose) getActivity();
        dateChoose.chooseDate(dayOfMonth,month,year);




    }
}
