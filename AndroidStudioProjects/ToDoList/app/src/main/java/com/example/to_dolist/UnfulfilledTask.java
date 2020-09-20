package com.example.to_dolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import inteface.OnRecClick;
import io.realm.Realm;
import recycler_for_task.DataUpdate;
import recycler_for_task.TaskData;
import unfulfilled_task_recycler.UnfulfilledTaskAdapter;

public class UnfulfilledTask extends Fragment {
    RecyclerView recyclerView;
    Realm realm;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle(getResources().getString(R.string.unfulfilledTask));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unfulfilled_task,container,false);
        Realm.init(Objects.requireNonNull(getActivity()));
        realm=Realm.getInstance(DataUpdate.getDefaultConfig());
        UnfulfilledTaskAdapter adapter= new UnfulfilledTaskAdapter(realm.where(TaskData.class).equalTo("check",false)
                .findAll(),getActivity());
        recyclerView=view.findViewById(R.id.unfulfilledRec);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

}
