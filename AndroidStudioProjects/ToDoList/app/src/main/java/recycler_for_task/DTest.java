package recycler_for_task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.to_dolist.R;

import io.realm.Realm;

public class DTest extends Dialog {
    Context context;
    Realm realm;
    Activity activity;
    public DTest(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    public DTest(Context context, Activity activity) {
        super(activity);
        this.activity=activity;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        setContentView(R.layout.t_d);
        Realm.init(context);
        realm=Realm.getInstance(DataUpdate.getDefaultConfig());
        final EditText editText=findViewById(R.id.editText);
        final EditText editText2=findViewById(R.id.editText2);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText.getText().toString().trim();
                String task= editText2.getText().toString().trim();


            }
        });


    }
}
