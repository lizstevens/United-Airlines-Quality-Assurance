package edu.msu.steve702.ua_quality_assurance_platform.main_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.msu.steve702.ua_quality_assurance_platform.R;

public class ChecklistSectionAdapter extends RecyclerView.Adapter<ChecklistSectionAdapter.CheckListSectionViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Integer currentSection;
    private List<String> questionList;
    private Context mCtx;

    public Integer getCurrentSection() { return this.currentSection; }
    public void setCurrentSection(final Integer currentSection) { this.currentSection = currentSection; }

    public List<String> getQuestionList() { return this.questionList; }
    public void setQuestionList(final List<String> questionList) { this.questionList = questionList; }

    public ChecklistSectionAdapter(Context mCtx, Integer currentSection, List<String> questionList) {
        this.mCtx = mCtx;
        this.currentSection = currentSection;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public CheckListSectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.view_checklist_question, viewGroup, false);
        return new CheckListSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListSectionViewHolder checklistSectionViewHolder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(checklistSectionViewHolder.questionRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(questionList.size());

        //ChecklistQuestionAdapter checkListQuestionAdapter = new ChecklistQuestionAdapter(mCtx, questionList);
        checklistSectionViewHolder.questionRecyclerView.setLayoutManager(layoutManager);
        //checklistSectionViewHolder.questionRecyclerView.setAdapter(checkListQuestionAdapter);
        checklistSectionViewHolder.questionRecyclerView.setRecycledViewPool(viewPool);
    }

    class CheckListSectionViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView questionRecyclerView;

        CheckListSectionViewHolder(final View itemView){
            super(itemView);

            //questionRecyclerView = itemView.findViewById(R.id.recyclerView_questions);
        }
    }

    @Override
    public int getItemCount()
    {
        return 1;
    }
}
