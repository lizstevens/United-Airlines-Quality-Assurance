package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import edu.msu.steve702.ua_quality_assurance_platform.InProcessActivity;
import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.InProcessObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InProcessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InProcessFragment extends Fragment {

    InProcessObject inProcessObject;

    private static final String IN_PROCESS_KEY = "IN_PROCESS_KEY";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private InProcessActivity inProcessActivity;

    public InProcessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InProcessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InProcessFragment newInstance(String param1, String param2) {
        InProcessFragment fragment = new InProcessFragment();
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
        return inflater.inflate(R.layout.fragment_in_process, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // store in-process data: key to access the in-process object I store in the second argument
        outState.putSerializable(IN_PROCESS_KEY, (Serializable)inProcessObject);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //probably orientation change
            inProcessObject= (InProcessObject) savedInstanceState.getSerializable(IN_PROCESS_KEY);
        }
    }

}