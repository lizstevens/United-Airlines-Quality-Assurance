package edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TraceabilityTableDataObject;

/**
 * TraceabilityTableFragment Class
 * A simple {@link Fragment} subclass.
 * A fragment that represents a traceability table tab within the table data fragment in the AuditAcitvity Class.
 * This fragment view is a subfragment.
 */
public class TraceabilityTableFragment extends Fragment {

    /** the application context and its respective Getter and Setter **/
    private Context context;
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    /** this fragment view and its Getter**/
    private View fragmentView;
    public View getFragmentView() { return this.fragmentView; }

    /** The traceability table data object and its respective Getter and Setter **/
    private TraceabilityTableDataObject traceabilityTableDataObject;
    public TraceabilityTableDataObject getTraceabilityTableDataObject() { return this.traceabilityTableDataObject; }
    public void setTraceabilityTableDataObject(final TraceabilityTableDataObject traceabilityTableDataObject) { this.traceabilityTableDataObject = traceabilityTableDataObject; }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /**
     * Empty Constructor
     */
    public TraceabilityTableFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TraceabilityTableFragment.
     */
    public static TraceabilityTableFragment newInstance(String param1, String param2) {
        TraceabilityTableFragment fragment = new TraceabilityTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Function to create this fragment
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
     * Function to create the view for this fragment
     * @param inflater the layout inflater
     * @param container the viewgroup container
     * @param savedInstanceState the saved instance state
     * @return the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traceability_table, container, false);
    }

    /**
     * Function for when the fragment view is created
     * @param view the fragment view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentView = view;

        if (traceabilityTableDataObject != null) {
            populate();
        }
    }

    /**
     * Function to bundle the data within the fragment view.
     * @return a traceability table data object
     */
    public TraceabilityTableDataObject bundleObject() {
        List<String> row1 = getRow(1);
        List<String> row2 = getRow(2);
        List<String> row3 = getRow(3);
        List<String> row4 = getRow(4);
        List<String> row5 = getRow(5);
        List<String> row6 = getRow(6);
        List<String> row7 = getRow(7);
        List<String> row8 = getRow(8);
        List<String> row9 = getRow(9);
        List<String> row10 = getRow(10);

        TraceabilityTableDataObject newObject = new TraceabilityTableDataObject(
                row1,
                row2,
                row3,
                row4,
                row5,
                row6,
                row7,
                row8,
                row9,
                row10
        );

        return newObject;
    }

    /**
     * Function to get the data from the table row
     * @param rowNum the current row number
     * @return a list of strings of the data from this row
     */
    public List<String> getRow(Integer rowNum) {
        List<String> row = new ArrayList<>();
        for (int i=1; i<6; i++) {
            String rowName = "row" + rowNum + "_col" + i;
            int cellId = context.getResources().getIdentifier(rowName, "id", context.getPackageName());
            EditText cell = fragmentView.findViewById(cellId);
            row.add(cell.getText().toString());
        }
        return row;
    }

    /**
     * Function to set the data in the table row
     * @param rowNum the current row number
     * @param row a list of string containing the data to be populated into the row
     */
    public void setRow(Integer rowNum, List<String> row) {
        for (int i=1; i<6; i++) {
            String rowName = "row" + rowNum + "_col" + i;
            int cellId = context.getResources().getIdentifier(rowName, "id", context.getPackageName());
            EditText cell = fragmentView.findViewById(cellId);
            cell.setText(row.get(i-1));
        }
    }

    /**
     * Function to prepoulate the views
     */
    public void populate() {
        setRow(1, traceabilityTableDataObject.getRow1());
        setRow(2, traceabilityTableDataObject.getRow2());
        setRow(3, traceabilityTableDataObject.getRow3());
        setRow(4, traceabilityTableDataObject.getRow4());
        setRow(5, traceabilityTableDataObject.getRow5());
        setRow(6, traceabilityTableDataObject.getRow6());
        setRow(7, traceabilityTableDataObject.getRow7());
        setRow(8, traceabilityTableDataObject.getRow8());
        setRow(9, traceabilityTableDataObject.getRow9());
        setRow(10, traceabilityTableDataObject.getRow10());
    }

}
