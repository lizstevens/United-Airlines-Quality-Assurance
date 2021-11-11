package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;

public class ChecklistQuestionAdapter extends RecyclerView.Adapter<ChecklistQuestionAdapter.CheckListQuestionViewHolder> {
    private Integer currentSection;
    private List<String> questionList;
    private Context mCtx;

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

    class CheckListQuestionViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        CheckBox yes;
        CheckBox no;
        CheckBox na;
        EditText comment;

        CheckListQuestionViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.textView_question);
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
