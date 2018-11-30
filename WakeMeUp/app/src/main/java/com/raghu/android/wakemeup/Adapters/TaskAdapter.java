package com.raghu.android.wakemeup.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raghu.android.wakemeup.Activities.TaskActivity;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.R;
import com.raghu.android.wakemeup.Utilities.Constants;
import com.raghu.android.wakemeup.Utilities.Helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.raghu.android.wakemeup.Activities.TaskActivity.EXTRA_TASK_ID;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> taskList) {
        layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(mItemView, this);    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskTime.setText(task.getTime() + "");
        holder.taskDate.setText(Helper.getDateStringFromDate(task.getDate()));
        switch (task.getPriority()){
            case 1: holder.priorityView.setImageResource(R.drawable.priority_bar_critical);
            break;

            case 2: holder.priorityView.setImageResource(R.drawable.priority_bar_high);
            break;

            case 3: holder.priorityView.setImageResource(R.drawable.priority_bar_medium);
            break;

            case 4: holder.priorityView.setImageResource(R.drawable.priority_bar_low);
            break;

        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView taskTitle,taskDate,taskTime;
        public TaskAdapter taskAdapter;
        public CardView cardView;
        public ImageView priorityView;

        public TaskViewHolder(View itemView, TaskAdapter adapter) {
            super(itemView);
            taskTitle = (TextView)itemView.findViewById(R.id.card_task_title);
            taskDate = (TextView)itemView.findViewById(R.id.card_task_date);
            taskTime = (TextView)itemView.findViewById(R.id.card_task_time);
            cardView = (CardView)itemView.findViewById(R.id.task_card_view);
            priorityView = (ImageView)itemView.findViewById(R.id.task_priority_view);
            this.taskAdapter = adapter;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Task task = taskList.get(position);
            int taskId = task.getTaskId();
            Intent intent = new Intent(v.getContext(), TaskActivity.class);
            intent.putExtra(EXTRA_TASK_ID, taskId);
            v.getContext().startActivity(intent);
        }
    }

//    @TargetApi(24)
//    public void notifyAdapterDataSetChanged() {
//        /* do your sorting here */
//        taskList.sort(new Comparator<Task>() {
//            @Override
//            public int compare(Task t1, Task t2) {
//                return t1.getTime().compareTo(t2.getTime());
//            }
//        });
//
//        notifyDataSetChanged();
//    }

    public void setTaskList(List<Task> taskList) {
        try {
            if (this.taskList != null) {
                this.taskList.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.tasks = taskList;
            this.taskList.addAll(this.tasks);
            notifyDataSetChanged();
        }
    }
}
