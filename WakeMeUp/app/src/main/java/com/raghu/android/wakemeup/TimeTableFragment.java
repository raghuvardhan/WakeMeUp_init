package com.raghu.android.wakemeup;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raghu.android.wakemeup.Activities.TimeTableActivity;
import com.raghu.android.wakemeup.Adapters.TimeTableTaskAdapter;
import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment {

    private FloatingActionButton addFab;
    private RecyclerView mRecyclerView;

    private TimeTableTaskAdapter timeTableTaskAdapter;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FloatingActionButton floatingActionButton;


    private AppDatabase appDatabase;
    // List<Alarm> taskList;

    List<TimeTableTask> timeTableTaskList;

    public TimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_table, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());

        addFab  = (FloatingActionButton)getActivity().findViewById(R.id.add_fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeTableTask(getActivity());
            }
        });

        timeTableTaskList = new ArrayList<TimeTableTask>();


        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.timeTableRecycler_view_home);
        timeTableTaskAdapter = new TimeTableTaskAdapter(getActivity(), timeTableTaskList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.setAdapter(timeTableTaskAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        retrieveTasks();
    }

    private void createTimeTableTask(Context context) {
        Intent intent = new Intent(getActivity(), TimeTableActivity.class);
        startActivity(intent);
    }

    private void retrieveTasks() {

        LiveData<List<TimeTableTask>> timeTableTasks = appDatabase.timeTableTaskDao().getTimeTableTasks();
        timeTableTasks.observe(this, new Observer<List<TimeTableTask>>() {
            @Override
            public void onChanged(@Nullable List<TimeTableTask> timeTableTasks) {
                timeTableTaskAdapter.setTimeTableTaskList(timeTableTasks);
            }
        });
    }
}
