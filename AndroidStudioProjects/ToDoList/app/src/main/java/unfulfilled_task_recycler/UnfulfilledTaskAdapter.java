package unfulfilled_task_recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskData;
import recycler_for_task.TaskDataHelper;

public class UnfulfilledTaskAdapter extends RealmRecyclerViewAdapter<TaskData, UnfulfilledTaskAdapter.ViewHolder> {
    Context context;
    Realm realm;
    public UnfulfilledTaskAdapter(@Nullable OrderedRealmCollection<TaskData> data, Context context) {
        super(data, true);
        this.context=context;
        Realm.init(context);
        realm= Realm.getInstance(DataUpdate.getDefaultConfig());
        setHasStableIds(true);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.card_unfulfilled,parent,false);

         return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TaskData taskData = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu =  new PopupMenu(context,holder.option);
                popupMenu.inflate(R.menu.unful_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                TaskDataHelper.deleteItem(realm, holder.getItemId());
                                Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
        assert taskData != null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,taskData.getDay());
        calendar.set(Calendar.MONTH,taskData.getMonth());
        calendar.set(Calendar.YEAR,taskData.getYear());
        String time = DateFormat.getDateInstance().format(calendar.getTime());
        holder.task.setText(taskData.getTask());
        holder.date.setText(time);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, task, option;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            option=itemView.findViewById(R.id.option);
            date=itemView.findViewById(R.id.cardDate);
            task=itemView.findViewById(R.id.cardTask);
        }
    }
    @Override
    public long getItemId(int index) {
        return Objects.requireNonNull(getItem(index)).getId();
    }
}
