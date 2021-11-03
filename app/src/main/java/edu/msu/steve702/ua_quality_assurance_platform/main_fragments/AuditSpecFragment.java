package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;

import java.io.Serializable;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuditSpecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuditSpecFragment extends Fragment {


    private AuditObject auditObject;
    private View fragmentView;

    public AuditObject getAuditObject() { return this.auditObject; }

    public void setAuditObject(final AuditObject auditObject) { this.auditObject = auditObject; }


    private static final String AUDIT_SPECS_KEY = "AUDIT_SPECS_KEY";

    //getter for audit object

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuditSpecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuditSpecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuditSpecFragment newInstance(String param1, String param2) {
        AuditSpecFragment fragment = new AuditSpecFragment();
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
        return inflater.inflate(R.layout.fragment_audit_specs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentView = view;

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(AUDIT_SPECS_KEY)) {
            auditObject = (AuditObject) bundle.getSerializable(AUDIT_SPECS_KEY);
        }

        if (auditObject != null) {
            //prepopulate views
            //view.findViewById()
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        bundleObject();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // store audit specs data: key to access the audit specs object I store in the second argument
        outState.putSerializable(AUDIT_SPECS_KEY, (Serializable)auditObject);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //probably orientation change
            auditObject = (AuditObject) savedInstanceState.getSerializable(AUDIT_SPECS_KEY);
        }
    }

    public void bundleObject(){
        EditText auditName = fragmentView.findViewById(R.id.nameEdit);
        EditText auditDate = fragmentView.findViewById(R.id.dateEdit);
        EditText location = fragmentView.findViewById(R.id.locationEdit);
        EditText auditTitle = fragmentView.findViewById(R.id.auditTitleEdit);
        EditText auditNumber = fragmentView.findViewById(R.id.auditNumberEdit);
        EditText vendorName = fragmentView.findViewById(R.id.vendorNameEdit);
        EditText vendorNum = fragmentView.findViewById(R.id.vendorNumEdit);
        EditText auditDescrip = fragmentView.findViewById(R.id.descripEdit);

        String auditNameObj = auditName.getText().toString();
        String auditDateObj = auditDate.getText().toString();
        String locationObj = location.getText().toString();
        String auditTitleObj = auditTitle.getText().toString();
        String auditNumberObj = auditNumber.getText().toString();
        String vendorNameObj = vendorName.getText().toString();
        String vendorNumObj = vendorNum.getText().toString();
        String auditDescripObj = auditDescrip.getText().toString();


        AuditObject newObject = new AuditObject(
                auditNameObj,
                auditDateObj,
                locationObj,
                auditTitleObj,
                auditNumberObj,
                vendorNameObj,
                vendorNumObj,
                auditDescripObj
        );

        auditObject = newObject;
    }
}