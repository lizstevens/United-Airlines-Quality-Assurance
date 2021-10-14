package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TabularDataActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabular_data);

        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.switch_to_in_process_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                //saveInProcess();
                break;
            case R.id.switch_to_in_process_btn:
                startActivity(new Intent(this, InProcessActivity.class));
                break;
        }

    }


}
