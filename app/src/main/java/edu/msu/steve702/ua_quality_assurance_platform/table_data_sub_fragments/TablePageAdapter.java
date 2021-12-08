package edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * TablePageAdapter Class
 * The page adapter for the page viewer for the subfragments within the Table Data fragment.
 * Apart of the Audit Activity Class.
 */
public class TablePageAdapter extends FragmentPagerAdapter {
    /** the number of tabs for this adapter **/
    private int numOfTabs;

    /** Instances of the data objects for each table characteristic **/
    private TechDataTableFragment techDataFragment;
    private ROMTableFragment romTableFragment;
    private CalibrationTableFragment calTableFragment;
    private TrainingTableFragment trainTableFragment;
    private TraceabilityTableFragment traceTableFragment;
    private ShelfLifeTableFragment shelfTableFragment;

    /** Getters for each table characteristic data object **/
    public TechDataTableFragment getTechDataFragment() { return this.techDataFragment; }
    public ROMTableFragment getRomTableFragment() { return this.romTableFragment; }
    public CalibrationTableFragment getCalTableFragment() { return this.calTableFragment; }
    public TrainingTableFragment getTrainTableFragment() { return this.trainTableFragment; }
    public TraceabilityTableFragment getTraceTableFragment() { return this.traceTableFragment; }
    public ShelfLifeTableFragment getShelfTableFragment() { return this.shelfTableFragment; }

    /**
     * Constructor
     * @param fm fragment manager
     * @param numOfTabs the number of tabs
     * @param context the application context
     */
    public TablePageAdapter(@NonNull FragmentManager fm, int numOfTabs, Context context) {
        super(fm);
        this.numOfTabs = numOfTabs;

        techDataFragment = new TechDataTableFragment();
        techDataFragment.setContext(context);
        romTableFragment = new ROMTableFragment();
        romTableFragment.setContext(context);
        calTableFragment = new CalibrationTableFragment();
        calTableFragment.setContext(context);
        trainTableFragment = new TrainingTableFragment();
        trainTableFragment.setContext(context);
        traceTableFragment = new TraceabilityTableFragment();
        traceTableFragment.setContext(context);
        shelfTableFragment = new ShelfLifeTableFragment();
        shelfTableFragment.setContext(context);
    }

    /**
     * Function to get the fragment according to the selected tab position
     * @param position the current position
     * @return the Fragment
     */
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

    /**
     * Gets the number of tabs
     * @return integer for the number of tabs
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }

}
