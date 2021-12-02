package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.ToggleButton;

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

    private EditText date;


    public AuditObject getAuditObject() { return this.auditObject; }

    public void setAuditObject(final AuditObject auditObject) { this.auditObject = auditObject; }
    private AlertDialog pd;


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

//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey(AUDIT_SPECS_KEY)) {
//            auditObject = (AuditObject) bundle.getSerializable(AUDIT_SPECS_KEY);
//        }


        if (auditObject != null) {
            //prepopulate views
            populate();
        }


        date = (EditText) fragmentView.findViewById(R.id.dateEdit);

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start == 0 && count == 0){
                    date.getText().clear();

                }
                else if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel ++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + mmddyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0,2));
                        int day = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = day > cal.getActualMaximum(Calendar.DATE)? cal.getActualMaximum(Calendar.DATE):day
                                < cal.getActualMinimum(Calendar.DATE)? cal.getActualMinimum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());

                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };


        date.addTextChangedListener(tw);




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
        ToggleButton statusbutton = fragmentView.findViewById(R.id.togglebutton);

        String auditNameObj = auditName.getText().toString();
        String auditDateObj = auditDate.getText().toString();
        String locationObj = location.getText().toString();
        String auditTitleObj = auditTitle.getText().toString();
        String auditNumberObj = auditNumber.getText().toString();
        String vendorNameObj = vendorName.getText().toString();
        String vendorNumObj = vendorNum.getText().toString();
        String auditDescripObj = auditDescrip.getText().toString();
        String status;

        if (statusbutton.isChecked()) {
            status = "Open";
        } else {
            status = "Closed";
        }


        AuditObject newObject = new AuditObject(
                auditNameObj,
                auditDateObj,
                locationObj,
                auditTitleObj,
                auditNumberObj,
                vendorNameObj,
                vendorNumObj,
                auditDescripObj,
                status
        );

        auditObject = newObject;
    }

    public void populate() {
        EditText auditName = fragmentView.findViewById(R.id.nameEdit);
        EditText auditDate = fragmentView.findViewById(R.id.dateEdit);
        EditText location = fragmentView.findViewById(R.id.locationEdit);
        EditText auditTitle = fragmentView.findViewById(R.id.auditTitleEdit);
        EditText auditNumber = fragmentView.findViewById(R.id.auditNumberEdit);
        EditText vendorName = fragmentView.findViewById(R.id.vendorNameEdit);
        EditText vendorNum = fragmentView.findViewById(R.id.vendorNumEdit);
        EditText auditDescrip = fragmentView.findViewById(R.id.descripEdit);
        ToggleButton statusbutton = fragmentView.findViewById(R.id.togglebutton);

        auditName.setText(auditObject.getAuditNameObj());
        auditDate.setText(auditObject.getAuditDateObj());
        location.setText(auditObject.getLocationObj());
        auditTitle.setText(auditObject.getAuditTitleObj());
        auditNumber.setText(auditObject.getAuditNumberObj());
        vendorName.setText(auditObject.getVendorNameObj());
        vendorNum.setText(auditObject.getVendorNumObj());
        auditDescrip.setText(auditObject.getAuditDescripObj());

        if (auditObject.getStatus().equals("Open")) {
            statusbutton.setChecked(true);
        } else {
            statusbutton.setChecked(false);
        }
    }
}