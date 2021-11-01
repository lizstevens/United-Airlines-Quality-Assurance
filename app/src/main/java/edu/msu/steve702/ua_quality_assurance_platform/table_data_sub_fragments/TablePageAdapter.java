package edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TablePageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    //Instances of each audit characteristic
    private TechDataTableFragment techDataFragment;
    //private ROMTableFragment romTableFragment;
    //private CalTableFragment calTableFragment;
    //private TrainTableFragment trainTableFragment;
    //private TraceTableFragment traceTableFragment;
    //private ShelfTableFragment shelfTableFragment;

    //Getters for each audit characteristic


    public TablePageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

        techDataFragment = new TechDataTableFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new TechDataTableFragment();
            case 1:
                return new TechDataTableFragment();
            case 2:
                return new TechDataTableFragment();
            case 3:
                return new TechDataTableFragment();
            case 4:
                return new TechDataTableFragment();
            case 5:
                return new TechDataTableFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
