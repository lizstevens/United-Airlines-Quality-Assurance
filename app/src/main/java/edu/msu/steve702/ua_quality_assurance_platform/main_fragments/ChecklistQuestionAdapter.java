package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.AuditObject;
import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;
import edu.msu.steve702.ua_quality_assurance_platform.table_data_sub_fragments.ShelfLifeTableFragment;

import static android.content.ContentValues.TAG;

public class ChecklistQuestionAdapter extends RecyclerView.Adapter<ChecklistQuestionAdapter.CheckListQuestionViewHolder> {
    private Integer currentSection;
    private List<String> questionList;
    private Context mCtx;

    public FirebaseFirestore db;
    private Intent intent;
    private String this_checklist_id;

    private List<ChecklistDataObject> checklistDataObjects;

    private ChecklistFragment fragment;

    public ChecklistFragment getChecklistFragment() { return this.fragment; }

    public Integer getCurrentSection() { return this.currentSection; }
    public void setCurrentSection(final Integer currentSection) { this.currentSection = currentSection; }

    public List<String> getQuestionList() { return this.questionList; }
    public void setQuestionList(final List<String> questionList) { this.questionList = questionList; }

    public ChecklistQuestionAdapter(Context mCtx, Integer currentSection, List<String> questionList, ChecklistFragment fragment) {
        this.mCtx = mCtx;
        this.currentSection = currentSection;
        this.questionList = questionList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CheckListQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.view_checklist_question, viewGroup, false);
        return new CheckListQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListQuestionViewHolder checkListQuestionViewHolder, int position){
        String questionString = questionList.get(position);
        String commentString = fragment.getChecklistDataObject().get(currentSection).get(position + 1)[2];
        checkListQuestionViewHolder.comment.setText(commentString);
        String currentAnswer = fragment.getChecklistDataObject().get(currentSection).get(position + 1)[1];
        checkListQuestionViewHolder.question.setText(questionString);

        checkListQuestionViewHolder.yes.setChecked(false);
        checkListQuestionViewHolder.no.setChecked(false);
        checkListQuestionViewHolder.na.setChecked(false);

        if (currentAnswer.equals("Yes")) {
            checkListQuestionViewHolder.yes.setChecked(true);
        } else if (currentAnswer.equals("No")) {
            checkListQuestionViewHolder.no.setChecked(true);
        } else if (currentAnswer.equals("N/A")) {
            checkListQuestionViewHolder.na.setChecked(true);
        }
    }

    class CheckListQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView question;
        CheckBox yes;
        CheckBox no;
        CheckBox na;
        EditText comment;

        CheckListQuestionViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.textView_question);
            yes = itemView.findViewById(R.id.checkBox_yes);
            yes.setChecked(false);
            no = itemView.findViewById(R.id.checkBox_no);
            no.setChecked(false);
            na = itemView.findViewById(R.id.checkBox_na);
            na.setChecked(false);
            comment = itemView.findViewById(R.id.editText_Comment);


            yes.setOnClickListener(this);
            no.setOnClickListener(this);
            na.setOnClickListener(this);
            comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    int currentQuestion = getAbsoluteAdapterPosition();
                    if (!hasFocus) {
                        fragment.getChecklistDataObject().get(currentSection).get(currentQuestion + 1)[2] = comment.getText().toString();
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int currentQuestion = getAbsoluteAdapterPosition();
            if (view == yes) {
                if (yes.isChecked()) {
                    fragment.getChecklistDataObject().get(currentSection).get(currentQuestion + 1)[1] = "Yes";
                }
            } else {
                yes.setChecked(false);
            }
            if (view == no) {
                if (no.isChecked()) {
                    fragment.getChecklistDataObject().get(currentSection).get(currentQuestion + 1)[1] = "No";
                }
            } else {
                no.setChecked(false);
            }
            if (view == na) {
                if (na.isChecked()) {
                    fragment.getChecklistDataObject().get(currentSection).get(currentQuestion + 1)[1] = "N/A";
                }
            } else {
                na.setChecked(false);
            }
        }

    }

    @Override
    public int getItemCount()
    {
        // This method returns the number
        // of items we have added
        // in the ChildItemList
        // i.e. the number of instances
        // of the ChildItemList
        // that have been created
        return questionList.size();
    }
}
