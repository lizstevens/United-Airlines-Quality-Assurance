package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.CalibrationTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ROMTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ShelfLifeTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TechnicalTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TraceabilityTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TrainingTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments.TablePageAdapter;

/**
 * TableDataFragment subclass
 * A simple {@link Fragment} subclass.
 * Represents the tab view for the table data fragment in the AuditAcitvity Class.
 * This fragment view has respective subfragments.
 */
public class TableDataFragment extends Fragment {

    /** Tablayout for the subfragments **/
    TabLayout tabs;
    /** PageViewer for the subfragments **/
    ViewPager tableViewPager;
    /** Adapter for the pageviewer **/
    TablePageAdapter tablePageAdapter;
    /** tab items **/
    TabItem tabTech;
    TabItem tabROM;
    TabItem tabCal;
    TabItem tabTrain;
    TabItem tabTrace;
    TabItem tabShelf;

    /** Getter for the table page adapter **/
    public TablePageAdapter getTablePageAdapter() { return this.tablePageAdapter; }

    /** the application context and its respective Getter and Setter **/
    private Context context;
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    /** Data Objects for each data table **/
    private TechnicalTableDataObject technicalTableDataObject;
    private ROMTableDataObject romTableDataObject;
    private CalibrationTableDataObject calibrationTableDataObject;
    private TrainingTableDataObject trainingTableDataObject;
    private TraceabilityTableDataObject traceabilityTableDataObject;
    private ShelfLifeTableDataObject shelfLifeTableDataObject;

    /** Getters and Setters for each data table object **/
    public TechnicalTableDataObject getTechnicalTableDataObject() {
        return this.technicalTableDataObject;
    }
    public void setTechnicalTableDataObject(final TechnicalTableDataObject technicalTableDataObject)
    {
        this.technicalTableDataObject = technicalTableDataObject;
    }
    public ROMTableDataObject getRomTableDataObject() { return this.romTableDataObject; }
    public void setRomTableDataObject(final ROMTableDataObject romTableDataObject) {
        this.romTableDataObject = romTableDataObject;
    }
    public CalibrationTableDataObject getCalibrationTableDataObject() {
        return this.calibrationTableDataObject;
    }
    public void setCalibrationTableDataObject(
            final CalibrationTableDataObject calibrationTableDataObject) {
        this.calibrationTableDataObject = calibrationTableDataObject;
    }
    public TrainingTableDataObject getTrainingTableDataObject() {
        return this.trainingTableDataObject;
    }
    public void setTrainingTableDataObject(final TrainingTableDataObject trainingTableDataObject) {
        this.trainingTableDataObject = trainingTableDataObject;
    }
    public TraceabilityTableDataObject getTraceabilityTableDataObject() {
        return this.traceabilityTableDataObject;
    }
    public void setTraceabilityTableDataObject(
            final TraceabilityTableDataObject traceabilityTableDataObject) {
        this.traceabilityTableDataObject = traceabilityTableDataObject;
    }
    public ShelfLifeTableDataObject getShelfLifeTableDataObject() {
        return this.shelfLifeTableDataObject;
    }
    public void setShelfLifeTableDataObject(final ShelfLifeTableDataObject shelfLifeTableDataObject)
    {
        this.shelfLifeTableDataObject = shelfLifeTableDataObject;
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /** Empty Constructor **/
    public TableDataFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableDataFragment.
     */
    public static TableDataFragment newInstance(String param1, String param2) {
        TableDataFragment fragment = new TableDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Function for creating this fragment.
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Function for when the activity is created.
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabs = (TabLayout) getView().findViewById(R.id.tableTabLayout);
        tabTech = getView().findViewById(R.id.tech_data_tab);
        tabROM = getView().findViewById(R.id.rom_tab);
        tabCal = getView().findViewById(R.id.cal_tab);
        tabTrain = getView().findViewById(R.id.train_tab);
        tabTrace = getView().findViewById(R.id.trace_tab);
        tabShelf = getView().findViewById(R.id.shelf_tab);
        tableViewPager = (ViewPager) getView().findViewById(R.id.viewPager_tables);
    }

    /**
     * Function for when the fragment view is created.
     * @param view the fragment view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Function for the when the fragment view is created.
     * @param inflater layout inflator
     * @param container viewgroup container
     * @param savedInstanceState the saved instance state
     * @return the fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_data_tabs, container,
                false);
        tabs = (TabLayout) view.findViewById(R.id.tableTabLayout);
        tableViewPager = (ViewPager) view.findViewById(R.id.viewPager_tables);
        if (tablePageAdapter == null) {
            tablePageAdapter = new TablePageAdapter(getChildFragmentManager(), 6,
                    context);
        }
        tableViewPager.setAdapter(tablePageAdapter);

        tablePageAdapter.getTechDataFragment().setTechnicalTableDataObject(
                technicalTableDataObject);
        tablePageAdapter.getRomTableFragment().setRomTableDataObject(romTableDataObject);
        tablePageAdapter.getCalTableFragment().setCalibrationTableDataObject(
                calibrationTableDataObject);
        tablePageAdapter.getTrainTableFragment().setTrainingTableDataObject(
                trainingTableDataObject);
        tablePageAdapter.getTraceTableFragment().setTraceabilityTableDataObject(
                traceabilityTableDataObject);
        tablePageAdapter.getShelfTableFragment().setShelfLifeTableDataObject(
                shelfLifeTableDataObject);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tableViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tableViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        return view;
    }

    /**
     * Function for bundling the fragment view data into the respecive table data objects.
     */
    public void bundleObjects() {
        if (tablePageAdapter != null) {
            if (tablePageAdapter.getTechDataFragment() != null) {
                if (tablePageAdapter.getTechDataFragment().getFragmentView() != null) {
                    technicalTableDataObject =
                            tablePageAdapter.getTechDataFragment().bundleObject();
                }
            }
            if (tablePageAdapter.getCalTableFragment() != null) {
                if (tablePageAdapter.getCalTableFragment().getFragmentView() != null) {
                    calibrationTableDataObject =
                            tablePageAdapter.getCalTableFragment().bundleObject();
                }
            }
            if (tablePageAdapter.getRomTableFragment() != null) {
                if (tablePageAdapter.getRomTableFragment().getFragmentView() != null) {
                    romTableDataObject = tablePageAdapter.getRomTableFragment().bundleObject();
                }
            }
            if (tablePageAdapter.getTrainTableFragment() != null) {
                if (tablePageAdapter.getTrainTableFragment().getFragmentView() != null) {
                    trainingTableDataObject =
                            tablePageAdapter.getTrainTableFragment().bundleObject();
                }
            }
            if (tablePageAdapter.getTraceTableFragment() != null) {
                if (tablePageAdapter.getTraceTableFragment().getFragmentView() != null) {
                    traceabilityTableDataObject =
                            tablePageAdapter.getTraceTableFragment().bundleObject();
                }
            }
            if (tablePageAdapter.getShelfTableFragment() != null) {
                if (tablePageAdapter.getShelfTableFragment().getFragmentView() != null) {
                    shelfLifeTableDataObject =
                            tablePageAdapter.getShelfTableFragment().bundleObject();
                }
            }
        }
    }

}
