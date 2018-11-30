package com.raghu.android.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HelpActivity extends AppCompatActivity {

    TextView todoQuestion, timeTableQuestion, progressQuestion;
    TextView todoAnswer, timeTableAnswer, progressAnswer;

    Toolbar helpToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpToolBar = (Toolbar) findViewById(R.id.help_toolbar);
        setSupportActionBar(helpToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        todoQuestion = (TextView)findViewById(R.id.create_todo_help_question);
        todoAnswer = (TextView)findViewById(R.id.create_todo_help_answer);
        todoQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoAnswer.setVisibility(View.VISIBLE);
            }
        });

        timeTableQuestion = (TextView)findViewById(R.id.create_timetable_help_question);
        timeTableAnswer = (TextView)findViewById(R.id.create_timetable_help_answer);
        timeTableQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTableAnswer.setVisibility(View.VISIBLE);
            }
        });

        progressQuestion = (TextView)findViewById(R.id.create_progress_help_question);
        progressAnswer = (TextView)findViewById(R.id.create_progress_help_answer);
        progressQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressAnswer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
