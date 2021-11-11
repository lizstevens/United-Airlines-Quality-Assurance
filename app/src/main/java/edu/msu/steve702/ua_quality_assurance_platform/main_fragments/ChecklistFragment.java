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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditActivity;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChecklistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChecklistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String checklistTitle;

    private Spinner spinner;
    private ArrayList<String> section_list;
    private List<Integer> sectionNums;
    private List<String> section1_questions;
    private ChecklistDataObject obj;
    RecyclerView checklistSectionRecyclerView;
    LinearLayoutManager layoutManager;
    ChecklistQuestionAdapter questionAdapter;

    private Context context;
    @Nullable
    @Override
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChecklistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChecklistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChecklistFragment newInstance(String param1, String param2) {
        ChecklistFragment fragment = new ChecklistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView checklistTitleTextview = view.findViewById(R.id.checklistTitle);
        checklistTitleTextview.setText(checklistTitle);
        spinner = view.findViewById(R.id.section_spinner);
        section_list = new ArrayList<>();
        sectionNums = new ArrayList<>();
        section1_questions = new ArrayList<>();

        section_list.add("Section 1");
        section_list.add("Section 2");

        sectionNums.add(1);
        sectionNums.add(2);

        section1_questions.add("Question 1");
        section1_questions.add("Question 2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, section_list);
        spinner.setAdapter(adapter);

        checklistSectionRecyclerView = view.findViewById(R.id.recyclerView_sections);
        layoutManager = new LinearLayoutManager(context);
        questionAdapter = new ChecklistQuestionAdapter(context, 1, section1_questions);
        checklistSectionRecyclerView.setHasFixedSize(true);
        checklistSectionRecyclerView.setAdapter(questionAdapter);
        checklistSectionRecyclerView.setLayoutManager(layoutManager);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "Item selected:" + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                questionAdapter.setCurrentSection(sectionNums.get(position));
                questionAdapter.setQuestionList(section1_questions);
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Function for setting the Checklist Name in the View
     * @param checklist_name string indicating the name of the checklist chosen
     */
    public void changeChecklistTitleText(String checklist_name) {
        checklistTitle = checklist_name;
    }

    public ChecklistDataObject getCheclistDataObject() { return this.obj; }
    public void setChecklistDataObject(final ChecklistDataObject checklist) { this.obj = checklist; }

}