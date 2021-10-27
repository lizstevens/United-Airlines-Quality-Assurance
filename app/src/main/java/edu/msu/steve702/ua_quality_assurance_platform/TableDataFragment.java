package edu.msu.steve702.ua_quality_assurance_platform;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableDataFragment extends Fragment implements View.OnClickListener {

    private ViewGroup layout;
    private int numRows_table1 = 1;
    private int numRows_table2 = 1;
    private int numRows_table3 = 1;
    private int numRows_table4 = 1;
    private int numRows_table5 = 1;
    private int numRows_table6 = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableDataFragment newInstance(String param1, String param2) {
        TableDataFragment fragment = new TableDataFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = (ViewGroup) view.getParent();
        Button addButton_table1 = view.findViewById(R.id.buttonTable1);
        addButton_table1.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_tabular_data, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTable1:
                Toast.makeText(getContext(), "ID:" + v.getId(), Toast.LENGTH_LONG).show();
        }
    }
}