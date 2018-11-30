package com.raghu.android.wakemeup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.raghu.android.wakemeup.Activities.LabelDialogActivity;
import com.raghu.android.wakemeup.Activities.TaskActivity;
import com.raghu.android.wakemeup.Adapters.TaskAdapter;
import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Task;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;




import com.raghu.android.wakemeup.Database.Entities.Task;

public class TodosFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private TaskAdapter taskAdapter;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FloatingActionButton floatingActionButton;


    private AppDatabase appDatabase;
    // List<Alarm> taskList;

    List<Task> taskList;

    private int priority;
    private String category;

    public TodosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todos, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Todo Get the priority from UI
        priority = 1;

        //Todo Get the priority from UI
        category = "Gym";


        floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());

//        taskList = new ArrayList<Alarm>();
        taskList = new ArrayList<Task>();


        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_home);
        taskAdapter = new TaskAdapter(getActivity(), taskList);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.setAdapter(taskAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        retrieveTasks();


    }


    public void createTask(){
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivity(intent);
    }

    private void retrieveTasks() {

        LiveData<List<Task>> tasks = appDatabase.taskDao().getPendingTasks(false);
        tasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                taskAdapter.setTaskList(tasks);
            }
        });
    }
}
