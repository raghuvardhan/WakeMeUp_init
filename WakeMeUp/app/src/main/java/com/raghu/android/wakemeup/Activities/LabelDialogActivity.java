package com.raghu.android.wakemeup.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Category;
import com.raghu.android.wakemeup.R;

import java.util.ArrayList;
import java.util.List;

public class LabelDialogActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private AppDatabase appDatabase;
    private int padding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_dialog);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        gridLayout = (GridLayout)findViewById(R.id.label_activity_dialog);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        List<Category> categories = appDatabase.categoryDao().getCategories();
        for(Category category : categories){
            TextView textView = new TextView(LabelDialogActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setText(category.getName());
            textView.setPadding(padding,padding,padding,padding);
            gridLayout.addView(textView);
        }
    }
}
