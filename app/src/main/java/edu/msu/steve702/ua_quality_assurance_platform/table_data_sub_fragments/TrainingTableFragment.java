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
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TechnicalTableDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.TrainingTableDataObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainingTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingTableFragment extends Fragment {
    private Context context;
    public Context getContext() { return this.context; }
    public void setContext(final Context context) { this.context = context; }

    private View fragmentView;
    public View getFragmentView() { return this.fragmentView; }

    private TrainingTableDataObject trainingTableDataObject;
    public TrainingTableDataObject getTrainingTableDataObject() { return this.trainingTableDataObject; }
    public void setTrainingTableDataObject(final TrainingTableDataObject trainingTableDataObject) { this.trainingTableDataObject = trainingTableDataObject; }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrainingTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrainingTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainingTableFragment newInstance(String param1, String param2) {
        TrainingTableFragment fragment = new TrainingTableFragment();
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
        return inflater.inflate(R.layout.fragment_training_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentView = view;
    }

    public TrainingTableDataObject bundleObject() {
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

        TrainingTableDataObject newObject = new TrainingTableDataObject(
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

    public List<String> getRow(Integer rowNum) {
        List<String> row = new ArrayList<>();
        for (int i=1; i<5; i++) {
            String rowName = "row" + rowNum + "_col" + i;
            int cellId = context.getResources().getIdentifier(rowName, "id", context.getPackageName());
            EditText cell = fragmentView.findViewById(cellId);
            row.add(cell.getText().toString());
        }
        return row;
    }
}