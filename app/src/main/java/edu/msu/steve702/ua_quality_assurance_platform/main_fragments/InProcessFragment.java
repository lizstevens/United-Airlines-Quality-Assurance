package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

/**
 * InProcessFragment subclass
 * A simple {@link Fragment} subclass.
 * Represents the tab view for the In Process Fragment in the AuditAcitvity Class.
 */
public class InProcessFragment extends Fragment {

    /** The InProcessObject **/
    private InProcessObject inProcessObject;
    /** The fragment view **/
    private View fragmentView;
    /** The recycler view **/
    private RecyclerView recyclerView;
    /** Adapter for the fragment recycler view **/
    private InProcessFragmentAdapter adapter;
    /** List of inprocess sheets currently added to the audit **/
    private List<InProcessObject> inProcessList = new ArrayList<>();
    /** View of the add button for adding an inprocess sheet to the audit **/
    private Button addButton;
    /** Getter and Setter for the inprocess list **/
    public List<InProcessObject> getInProcessList() { return this.inProcessList; }
    public void setInProcessList(final List<InProcessObject> list) { this.inProcessList = list; }

    /** Key for the inprocess **/
    private static final String IN_PROCESS_KEY = "IN_PROCESS_KEY";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /** Empty Constructor **/
    public InProcessFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InProcessFragment.
     */
    public static InProcessFragment newInstance(String param1, String param2) {
        InProcessFragment fragment = new InProcessFragment();
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
     * Function for when the fragment view is created.
     * @param inflater layout inflator
     * @param container viewgroup container
     * @param savedInstanceState the saved instance state
     * @return this fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_in_process, container, false);
    }

    /**
     * Override method for onSavedInstanceState
     * @param outState the outstate
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // store in-process data: key to access the in-process object I store in the second argument
        outState.putSerializable(IN_PROCESS_KEY, (Serializable)inProcessObject);
    }

    /**
     * Function for creating the activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //probably orientation change
            inProcessObject= (InProcessObject) savedInstanceState.getSerializable(IN_PROCESS_KEY);
        }
    }

    /**
     * Function for the fragment view being created
     * @param view the fragment view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.inProcessFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new InProcessFragmentAdapter(this.getContext(), inProcessList, this);

        recyclerView.setAdapter(adapter);

        addButton = view.findViewById(R.id.view_in_process_sheets);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundleObject();
                adapter.notifyDataSetChanged();
            }
        });

        fragmentView = view;
    }

    /**
     * Function for bundling fragment view data into an InProcessObject.
     */
    public void bundleObject() {
        // edit text name
        EditText employeeNameEdit = fragmentView.findViewById(R.id.empNameText);
        // edit text part number
        EditText partNumberEdit = fragmentView.findViewById(R.id.partNumText);
        // edit text serial number
        EditText serialNumberEdit = fragmentView.findViewById(R.id.serialNumText);
        // edit text nomenclature
        EditText nomenclatureEdit = fragmentView.findViewById(R.id.nomenText);
        // edit text task
        EditText taskEdit = fragmentView.findViewById(R.id.taskText);
        // edit text techSpecifications
        EditText techSpecificationsEdit = fragmentView.findViewById(R.id.techSpecText);
        // edit text tooling
        EditText toolingEdit = fragmentView.findViewById(R.id.toolingText);
        // edit text shelfLife
        EditText shelfLifeEdit = fragmentView.findViewById(R.id.shelfLifeText);
        // edit text traceability
        EditText traceEdit = fragmentView.findViewById(R.id.traceText);
        // edit text reqTraining
        EditText reqTrainingEdit = fragmentView.findViewById(R.id.reqTrainingText);
        // edit text trainingDate
        EditText trainingDateEdit = fragmentView.findViewById(R.id.dateText);

        String employeeNameObj = employeeNameEdit.getText().toString();
        String partNumberObj = partNumberEdit.getText().toString();
        String serialNumberObj = serialNumberEdit.getText().toString();
        String nomenclatureObj = nomenclatureEdit.getText().toString();
        String taskObj = taskEdit.getText().toString();
        String techSpecificationsObj = techSpecificationsEdit.getText().toString();
        String toolingObj = toolingEdit.getText().toString();
        String shelfLifeObj = shelfLifeEdit.getText().toString();
        String traceObj = traceEdit.getText().toString();
        String reqTrainingObj = reqTrainingEdit.getText().toString();
        String trainingDateObj = trainingDateEdit.getText().toString();


        InProcessObject inProcess = new InProcessObject(
                employeeNameObj,
                partNumberObj,
                serialNumberObj,
                nomenclatureObj,
                taskObj,
                techSpecificationsObj,
                toolingObj,
                shelfLifeObj,
                traceObj,
                reqTrainingObj,
                trainingDateObj
        );

        inProcessList.add(inProcess);

        employeeNameEdit.setText("");
        partNumberEdit.setText("");
        serialNumberEdit.setText("");
        nomenclatureEdit.setText("");
        taskEdit.setText("");
        techSpecificationsEdit.setText("");
        toolingEdit.setText("");
        shelfLifeEdit.setText("");
        traceEdit.setText("");
        reqTrainingEdit.setText("");
        trainingDateEdit.setText("");
    }

    /**
     * Function for setting the current inprocess data into the table that was selected.
     * @param listPosition position of the inprocess table selected
     * @param object The inprocess object to represent this data
     */
    public void setInProcessTable(Integer listPosition, InProcessObject object) {
        inProcessList.remove(listPosition);

        // edit text name
        EditText employeeNameEdit = fragmentView.findViewById(R.id.empNameText);
        // edit text part number
        EditText partNumberEdit = fragmentView.findViewById(R.id.partNumText);
        // edit text serial number
        EditText serialNumberEdit = fragmentView.findViewById(R.id.serialNumText);
        // edit text nomenclature
        EditText nomenclatureEdit = fragmentView.findViewById(R.id.nomenText);
        // edit text task
        EditText taskEdit = fragmentView.findViewById(R.id.taskText);
        // edit text techSpecifications
        EditText techSpecificationsEdit = fragmentView.findViewById(R.id.techSpecText);
        // edit text tooling
        EditText toolingEdit = fragmentView.findViewById(R.id.toolingText);
        // edit text shelfLife
        EditText shelfLifeEdit = fragmentView.findViewById(R.id.shelfLifeText);
        // edit text traceability
        EditText traceEdit = fragmentView.findViewById(R.id.traceText);
        // edit text reqTraining
        EditText reqTrainingEdit = fragmentView.findViewById(R.id.reqTrainingText);
        // edit text trainingDate
        EditText trainingDateEdit = fragmentView.findViewById(R.id.dateText);

        employeeNameEdit.setText(object.getEmployeeNameObj());
        partNumberEdit.setText(object.getPartNumberObj());
        serialNumberEdit.setText(object.getSerialNumberObj());
        nomenclatureEdit.setText(object.getNomenclatureObj());
        taskEdit.setText(object.getTaskObj());
        techSpecificationsEdit.setText(object.getTechSpecificationsObj());
        toolingEdit.setText(object.getToolingObj());
        shelfLifeEdit.setText(object.getShelfLifeObj());
        traceEdit.setText(object.getTraceObj());
        reqTrainingEdit.setText(object.getReqTrainingObj());
        trainingDateEdit.setText(object.getTrainingDateObj());

        adapter.notifyDataSetChanged();
    }

}
