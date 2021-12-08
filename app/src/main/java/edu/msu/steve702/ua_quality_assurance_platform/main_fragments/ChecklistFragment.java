package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

/**
 * AuditSpecFragment subclass
 * A simple {@link Fragment} subclass.
 * Represents the tab view for the checklist fragment in the AuditAcitvity Class.
 */
public class ChecklistFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /** Title of the checklist **/
    private String checklistTitle;
    /** Spinner View for section dropdown **/
    private Spinner spinner;
    /** List of the names of all of the checklist sections **/
    private ArrayList<String> section_list;
    /** questions for the current section **/
    private List<List<String>> section_questions_list;
    /** represents the current section selected **/
    private Integer currentSection;
    /** total checklist sections for a checklist **/
    private Integer totalSizeFor8;
    /** Checklist Data Object **/
    private ChecklistDataObject obj;
    /** Recycler view for the checklist section **/
    RecyclerView checklistSectionRecyclerView;
    /** Linear layout manager **/
    LinearLayoutManager layoutManager;
    /** The adapter for displaying the checklist questions **/
    ChecklistQuestionAdapter questionAdapter;

    /** The fragment view and its getter **/
    private View fragmentView;
    public View getFragmentView() { return this.fragmentView; }

    /** The application context **/
    private Context context;

    /** Context getters and setters **/
    @Nullable
    @Override
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    /** Respective Getters and Setters for the ChecklistDataObject **/
    public ChecklistDataObject getObj() { return this.obj; }
    public void setObj(final ChecklistDataObject obj) { this.obj = obj; }

    private String mParam1;
    private String mParam2;

    /** Empty Constructor **/
    public ChecklistFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor
     * @param obj Checklist data object that we will use for this fragment.
     */
    public ChecklistFragment(ChecklistDataObject obj) {
        this.obj = obj;
    }

    /** Getter for checklist question adapter **/
    public ChecklistQuestionAdapter getQuestionAdapter() { return this.questionAdapter; }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChecklistFragment.
     */
    public static ChecklistFragment newInstance(String param1, String param2) {
        ChecklistFragment fragment = new ChecklistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Function for creating this Fragment
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
     * Function for creating the fragment view.
     * @param inflater layout inflater
     * @param container viewgroup container
     * @param savedInstanceState the saved instance state
     * @return the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    /**
     * Function for when the view of this fragment is created.
     * @param view the fragment view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView checklistTitleTextview = view.findViewById(R.id.checklistTitle);
        checklistTitleTextview.setText("Checklist " + obj.getChecklistId());
        spinner = view.findViewById(R.id.section_spinner);
        totalSizeFor8 = obj.size();


        section_list = new ArrayList<>();
        section_questions_list = new ArrayList<>();

        currentSection = 1;
        for (int i = 1; i <= totalSizeFor8; i++) {
            List<String> section_questions = new ArrayList<>();
            for (Map.Entry<Integer, String[]> entry : obj.get(i).entrySet()) {
                if (entry.getKey() == 0) {
                    section_list.add("Section " + i + ": " + entry.getValue()[0]);

                } else {
                    section_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                }
            }
            section_questions_list.add(i - 1, section_questions);
        }
        questionAdapter = new ChecklistQuestionAdapter(context, currentSection,
                section_questions_list.get(currentSection), this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.support_simple_spinner_dropdown_item, section_list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Currently section 21 is at position 0
                questionAdapter.setCurrentSection(position + 1);
                questionAdapter.setQuestionList(section_questions_list.get(position));
                questionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checklistSectionRecyclerView = view.findViewById(R.id.recyclerView_sections);
        layoutManager = new LinearLayoutManager(context);
        checklistSectionRecyclerView.setHasFixedSize(true);
        checklistSectionRecyclerView.setAdapter(questionAdapter);
        checklistSectionRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Function for setting the Checklist Name in the View
     * @param checklist_name string indicating the name of the checklist chosen
     */
    public void changeChecklistTitleText(String checklist_name) { checklistTitle = checklist_name; }

    /** Getter and setter for the ChecklistDataObject **/
    public ChecklistDataObject getChecklistDataObject() { return this.obj; }
    public void setChecklistDataObject(final ChecklistDataObject checklist) {
        this.obj = checklist;
    }

    /** Getter for the checklistId **/
    public void setChecklistId(final Integer id){this.obj.setChecklistId(id);}

}
