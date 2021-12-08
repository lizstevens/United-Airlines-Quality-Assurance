package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

/**
 * AuditPageAdapter Class
 * Class for managing the page adapter for all respective fragments within the AuditActivity.
 */
public class AuditPageAdapter extends FragmentPagerAdapter {

    /** Number of tabs the audit activity contains **/
    private int numOfTabs;

    /** Instances of each audit fragment characteristic **/
    private AuditSpecFragment auditSpecFragment;
    private ChecklistFragment checklistFragment;
    private InProcessFragment inProcessFragment;
    private TableDataFragment tableDataFragment;

    /** Getters for each audit fragment characteristic **/
    public AuditSpecFragment getAuditSpecFragment() { return this.auditSpecFragment; }
    public ChecklistFragment getChecklistFragment() { return this.checklistFragment; }
    public InProcessFragment getInProcessFragment() { return this.inProcessFragment; }
    public TableDataFragment getTableDataFragment() { return this.tableDataFragment; }

    /**
     * Constructor for the audit page adapter
     * @param fm Instance of the fragment manager
     * @param numOfTabs number of tabs the audit activity has
     * @param checklist_name name of the checklist that was selected for this audit
     * @param context Application context
     * @param checklist ChecklistDataObject that was parsed for the respective checklist
     */
    public AuditPageAdapter(@NonNull FragmentManager fm, int numOfTabs, String checklist_name,
                            Context context, ChecklistDataObject checklist) {
        super(fm);
        this.numOfTabs = numOfTabs;

        auditSpecFragment = new AuditSpecFragment();
        checklistFragment = new ChecklistFragment(checklist);
        checklistFragment.setContext(context);
        inProcessFragment = new InProcessFragment();
        tableDataFragment = new TableDataFragment();
        tableDataFragment.setContext(context);

        checklistFragment.setChecklistId(checklist.getChecklistId());
        checklistFragment.setChecklistDataObject(checklist);
    }

    /**
     * Function for toggling the correct fragment view based on tab position.
     * @param position the number position of the tab selected
     * @return The Fragment at the selected position
     */
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

    /**
     * Gets the tab count.
     * @return integer representing tab count.
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }

}
