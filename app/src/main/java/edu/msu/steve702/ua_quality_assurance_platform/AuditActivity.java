package edu.msu.steve702.ua_quality_assurance_platform;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AuditActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    AuditPageAdapter pageAdapter;
    TabItem tabAuditSpecs;
    TabItem tabChecklist;
    TabItem tabInProcess;
    TabItem tabTableData;
    String checklist_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.auditTabLayout);
        tabAuditSpecs = findViewById(R.id.auditSpecsTabItem);
        tabChecklist = findViewById(R.id.checklistTabItem);
        tabInProcess = findViewById(R.id.inProcessTabItem);
        tabTableData = findViewById(R.id.tableDataTabItem);
        viewPager = findViewById(R.id.viewPager);

        //name of the checklist that was selected from the previous view
        checklist_name = getIntent().getStringExtra("checklistName");
        pageAdapter = new AuditPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), checklist_name);
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_audit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String option1 = getString(R.string.save);
        String option2 = getString(R.string.generate_pdf);
        String option3 = getString(R.string.take_photo);
        String option4 = getString(R.string.upload_photo);

        Toast.makeText(this, "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT).show();
        if (option1.equals(item.getTitle().toString())) {

        } else if (option2.equals(item.getTitle().toString())) {

        }
        return true;
    }


}
