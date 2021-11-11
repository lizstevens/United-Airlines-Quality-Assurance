package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AuditPageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    //Instances of each audit characteristic
    private AuditSpecFragment auditSpecFragment;
    private ChecklistFragment checklistFragment;
    private InProcessFragment inProcessFragment;
    private TableDataFragment tableDataFragment;

    //Getters for each audit characteristic
    public AuditSpecFragment getAuditSpecFragment() { return this.auditSpecFragment; }
    public ChecklistFragment getChecklistFragment() { return this.checklistFragment; }
    public InProcessFragment getInProcessFragment() { return this.inProcessFragment; }
    public TableDataFragment getTableDataFragment() { return this.tableDataFragment; }

    public AuditPageAdapter(@NonNull FragmentManager fm, int numOfTabs, String checklist_name, Context context) {
        super(fm);
        this.numOfTabs = numOfTabs;

        auditSpecFragment = new AuditSpecFragment();
        checklistFragment = new ChecklistFragment();
        checklistFragment.setContext(context);
        inProcessFragment = new InProcessFragment();
        tableDataFragment = new TableDataFragment();
        tableDataFragment.setContext(context);

        checklistFragment.changeChecklistTitleText(checklist_name);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return auditSpecFragment;
            case 1:
                return checklistFragment;
            case 2:
                return inProcessFragment;
            case 3:
                return tableDataFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
