package edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TablePageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    //Instances of each audit characteristic
    private TechDataTableFragment techDataFragment;
    private ROMTableFragment romTableFragment;
    private CalibrationTableFragment calTableFragment;
    private TrainingTableFragment trainTableFragment;
    private TraceabilityTableFragment traceTableFragment;
    private ShelfLifeTableFragment shelfTableFragment;

    //Getters for each audit characteristic


    public TablePageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

        techDataFragment = new TechDataTableFragment();
        romTableFragment = new ROMTableFragment();
        calTableFragment = new CalibrationTableFragment();
        trainTableFragment = new TrainingTableFragment();
        traceTableFragment = new TraceabilityTableFragment();
        shelfTableFragment = new ShelfLifeTableFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return techDataFragment;
            case 1:
                return romTableFragment;
            case 2:
                return calTableFragment;
            case 3:
                return trainTableFragment;
            case 4:
                return traceTableFragment;
            case 5:
                return shelfTableFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
