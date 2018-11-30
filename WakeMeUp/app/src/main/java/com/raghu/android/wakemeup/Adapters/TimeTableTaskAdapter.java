package com.raghu.android.wakemeup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.R;
import com.raghu.android.wakemeup.Activities.TimeTableActivity;
import com.raghu.android.wakemeup.Utilities.Helper;

import java.util.ArrayList;
import java.util.List;

import static com.raghu.android.wakemeup.Activities.TaskActivity.EXTRA_TASK_ID;

public class TimeTableTaskAdapter extends RecyclerView.Adapter<TimeTableTaskAdapter.TimeTableTaskViewHolder> {
    private List<TimeTableTask> timeTableTaskList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    private List<TimeTableTask> timeTableTasks;

    public TimeTableTaskAdapter(Context context, List<TimeTableTask> timeTableTaskList) {
        layoutInflater = LayoutInflater.from(context);
        this.timeTableTaskList = timeTableTaskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeTableTaskAdapter.TimeTableTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.timetable_task_list_item, parent, false);
        return new TimeTableTaskViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableTaskAdapter.TimeTableTaskViewHolder holder, int position) {
        TimeTableTask timeTableTask = timeTableTaskList.get(position);
        holder.timeTableTaskText.setText(timeTableTask.getTimeTableTask());
        holder.timeTableTaskStartTime.setText(Helper.timeFormatter(timeTableTask.getStartTime()));
        holder.timeTableTaskEndTime.setText(Helper.timeFormatter(timeTableTask.getEndTime()));
    }

    @Override
    public int getItemCount() {
        return timeTableTaskList.size();
    }

    class TimeTableTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView timeTableTaskText, timeTableTaskStartTime, timeTableTaskEndTime;
        public TimeTableTaskAdapter timeTableTaskAdapter;
        public CardView cardView;

        public TimeTableTaskViewHolder(View itemView, TimeTableTaskAdapter adapter) {
            super(itemView);
            timeTableTaskText = (TextView) itemView.findViewById(R.id.card_timetable_task_title);
            timeTableTaskStartTime = (TextView) itemView.findViewById(R.id.card_timetable_task_start_time);
            timeTableTaskEndTime = (TextView) itemView.findViewById(R.id.card_timetable_task_end_time);
            cardView = (CardView) itemView.findViewById(R.id.task_card_view);
            this.timeTableTaskAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            TimeTableTask timeTableTask = timeTableTaskList.get(position);
            int timeTableTaskId = timeTableTask.getTimeTableTaskId();
            Intent intent = new Intent(v.getContext(), TimeTableActivity.class);
            intent.putExtra(EXTRA_TASK_ID, timeTableTaskId);
            v.getContext().startActivity(intent);
        }
    }

    public void setTimeTableTaskList(List<TimeTableTask> timeTableTaskList) {
        try {
            if (this.timeTableTaskList != null) {
                this.timeTableTaskList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.timeTableTasks = timeTableTaskList;
            this.timeTableTaskList.addAll(this.timeTableTasks);
            notifyDataSetChanged();
        }
    }
}