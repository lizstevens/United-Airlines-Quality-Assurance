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
    private Map<Integer, String[]> subMap;
    private Integer currentSection;
    private Integer totalSizeFor8 = 21;

    private ChecklistDataObject obj;
    RecyclerView checklistSectionRecyclerView;
    LinearLayoutManager layoutManager;
    ChecklistQuestionAdapter questionAdapter;

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
        section_questions = new ArrayList<>();
        section2_questions = new ArrayList<>();
        section3_questions = new ArrayList<>();
        section4_questions = new ArrayList<>();
        section5_questions = new ArrayList<>();
        section6_questions = new ArrayList<>();
        section7_questions = new ArrayList<>();
        section8_questions = new ArrayList<>();
        section9_questions = new ArrayList<>();
        section10_questions = new ArrayList<>();
        section11_questions = new ArrayList<>();
        section12_questions = new ArrayList<>();
        section13_questions = new ArrayList<>();
        section14_questions = new ArrayList<>();
        section15_questions = new ArrayList<>();
        section16_questions = new ArrayList<>();
        section17_questions = new ArrayList<>();
        section18_questions = new ArrayList<>();
        section19_questions = new ArrayList<>();
        section20_questions = new ArrayList<>();
        section21_questions = new ArrayList<>();


        for (int i = 1; i <= totalSizeFor8; i++) {
            currentSection = i;
            for (Map.Entry<Integer, String[]> entry : obj.get(i).entrySet()) {
                if (entry.getKey() == 0) {
                    section_list.add("Section " + i + ": " + entry.getValue()[0]);

                } else {
                    if (i == 1) {
                        section_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 2) {
                        section2_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 3) {
                        section3_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 4) {
                        section4_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 5) {
                        section5_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 6) {
                        section6_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 7) {
                        section7_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 8) {
                        section8_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 9) {
                        section9_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 10) {
                        section10_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 11) {
                        section11_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 12) {
                        section12_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 13) {
                        section13_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 14) {
                        section14_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 15) {
                        section15_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 16) {
                        section16_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 17) {
                        section17_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 18) {
                        section18_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 19) {
                        section19_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 20) {
                        section20_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                    if (i == 21) {
                        section21_questions.add(entry.getKey() - 1, entry.getValue()[0]);
                    }
                }
            }
        }
        questionAdapter = new ChecklistQuestionAdapter(context, 1, section_questions, this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, section_list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Currently section 21 is at position 0
                Toast.makeText(context, "Item selected:" + position, Toast.LENGTH_LONG).show();

                if (position == 0) {
                    questionAdapter.setQuestionList(section_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 1) {
                    questionAdapter.setQuestionList(section2_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 2) {
                    questionAdapter.setQuestionList(section3_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 3) {
                    questionAdapter.setQuestionList(section4_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 4) {
                    questionAdapter.setQuestionList(section5_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 5) {
                    questionAdapter.setQuestionList(section6_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 6) {
                    questionAdapter.setQuestionList(section7_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 7) {
                    questionAdapter.setQuestionList(section8_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 8) {
                    questionAdapter.setQuestionList(section9_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 9) {
                    questionAdapter.setQuestionList(section10_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 10) {
                    questionAdapter.setQuestionList(section11_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 11) {
                    questionAdapter.setQuestionList(section12_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 12) {
                    questionAdapter.setQuestionList(section13_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 13) {
                    questionAdapter.setQuestionList(section14_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 14) {
                    questionAdapter.setQuestionList(section15_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 15) {
                    questionAdapter.setQuestionList(section16_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 16) {
                    questionAdapter.setQuestionList(section17_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 17) {
                    questionAdapter.setQuestionList(section18_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 18) {
                    questionAdapter.setQuestionList(section19_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 19) {
                    questionAdapter.setQuestionList(section20_questions);
                    questionAdapter.notifyDataSetChanged();
                }
                if (position == 20) {
                    questionAdapter.setQuestionList(section21_questions);
                    questionAdapter.notifyDataSetChanged();
                }

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

    public ChecklistDataObject getCheclistDataObject() { return this.obj; }
    public void setChecklistDataObject(final ChecklistDataObject checklist) { this.obj = checklist; }

}