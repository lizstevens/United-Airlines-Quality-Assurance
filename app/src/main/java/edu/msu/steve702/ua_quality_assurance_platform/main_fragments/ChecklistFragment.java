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
import java.util.Map;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.activities.AuditActivity;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments.TablePageAdapter;

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
    private List<String> section_questions, section2_questions, section3_questions, section4_questions, section5_questions, section6_questions
            , section7_questions, section8_questions, section9_questions, section10_questions, section11_questions, section12_questions
            , section13_questions, section14_questions, section15_questions, section16_questions, section17_questions, section18_questions
            , section19_questions, section20_questions, section21_questions;
    private List<List<String>> section_questions_list;
    private Map<Integer, String[]> subMap;
    private Integer currentSection;
    private Integer totalSizeFor8;

    private ChecklistDataObject obj;
    RecyclerView checklistSectionRecyclerView;
    LinearLayoutManager layoutManager;

    ChecklistQuestionAdapter questionAdapter;
    ChecklistSectionAdapter checklistSectionAdapter;

    private View fragmentView;
    public View getFragmentView() { return this.fragmentView; }

    private Context context;
    @Nullable
    @Override
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    public ChecklistDataObject getObj() { return this.obj; }
    public void setObj(final ChecklistDataObject obj) { this.obj = obj; }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChecklistFragment() {
        // Required empty public constructor
    }

    public ChecklistFragment(ChecklistDataObject obj) {
        this.obj = obj;
    }

    public ChecklistQuestionAdapter getQuestionAdapter() { return this.questionAdapter; }

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
        //checklistTitleTextview.setText(checklistTitle);
        //checklistTitleTextview.setText("Checklist " + obj.getChecklistId());
        checklistTitleTextview.setText("Checklist 8");
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
        questionAdapter = new ChecklistQuestionAdapter(context, currentSection, section_questions_list.get(currentSection), this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, section_list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Currently section 21 is at position 0
//                Toast.makeText(context, "Item selected:" + position, Toast.LENGTH_LONG).show();
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
    public void changeChecklistTitleText(String checklist_name) {
        checklistTitle = checklist_name;
    }

    public ChecklistDataObject getChecklistDataObject() { return this.obj; }
    public void setChecklistDataObject(final ChecklistDataObject checklist) { this.obj = checklist; }

}