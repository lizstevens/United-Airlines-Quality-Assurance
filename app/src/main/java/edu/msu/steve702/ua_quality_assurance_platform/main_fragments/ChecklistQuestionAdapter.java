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

import static android.content.ContentValues.TAG;

public class ChecklistQuestionAdapter extends RecyclerView.Adapter<ChecklistQuestionAdapter.CheckListQuestionViewHolder> {
    private Integer currentSection;
    private List<String> questionList;
    private Context mCtx;

    public FirebaseFirestore db;
    private Intent intent;
    private String this_checklist_id;

    private List<ChecklistDataObject> checklistDataObjects;

    public Integer getCurrentSection() { return this.currentSection; }
    public void setCurrentSection(final Integer currentSection) { this.currentSection = currentSection; }

    public List<String> getQuestionList() { return this.questionList; }
    public void setQuestionList(final List<String> questionList) { this.questionList = questionList; }

    public ChecklistQuestionAdapter(Context mCtx, Integer currentSection, List<String> questionList) {
        this.mCtx = mCtx;
        this.currentSection = currentSection;
        this.questionList = questionList;
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
        checkListQuestionViewHolder.question.setText(questionString);
    }

    class CheckListQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView question;
        CheckBox yes;
        CheckBox no;
        CheckBox na;

        CheckListQuestionViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.textView_question);
            yes = itemView.findViewById(R.id.checkBox_yes);
            no = itemView.findViewById(R.id.checkBox_no);
            na = itemView.findViewById(R.id.checkBox_na);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
            na.setOnClickListener(this);

//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            ChecklistDataObject checklistDataObject = checklistDataObjects.get(getAbsoluteAdapterPosition());
            int currentQuestion = getAbsoluteAdapterPosition() + 1;
            Boolean checkBoxState = false;
            if (view == yes) {
                checkBoxState = yes.isChecked();
            }
            if (view == no) {
                checkBoxState = no.isChecked();
            }
            if (view == na) {
                checkBoxState = na.isChecked();
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
