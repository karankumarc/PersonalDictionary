package com.techpalle.karan.personaldictionary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.model.Meaning;
import com.techpalle.karan.personaldictionary.model.Word;

import java.util.List;

/**
 * Created by ADMIN on 9/21/2016.
 */
public class WordMeaningExpandableRecyclerAdapter extends
        ExpandableRecyclerAdapter<WordMeaningExpandableRecyclerAdapter.WordParentViewHolder, WordMeaningExpandableRecyclerAdapter.MeaningChildViewHolder> {

    private LayoutInflater mInflater;
    private List<Word> wordList;
    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public WordMeaningExpandableRecyclerAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    private WordMeaningExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList){
        super(parentItemList);
    }



    @Override
    public WordMeaningExpandableRecyclerAdapter.WordParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflater.inflate(R.layout.row_word_parent, parentViewGroup, false);
        return new WordParentViewHolder(view);
    }

    @Override
    public WordMeaningExpandableRecyclerAdapter.MeaningChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflater.inflate(R.layout.row_meaning_child, childViewGroup, false);
        return new MeaningChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(WordParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        Word word = (Word) parentListItem;
        parentViewHolder.mParentWordTextView.setText(word.getName());
        if (word.getIsHighlighted()) {
            parentViewHolder.mParentHighlight.setImageResource(android.R.drawable.btn_star_big_on);
        }

        if (parentViewHolder.isExpanded()) {
            parentViewHolder.mParentDropDownArrow.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            parentViewHolder.mParentDropDownArrow.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    public void refreshData(int position, int size){
        //new WordMeaningExpandableRecyclerAdapter(wordList);
        //notifyParentItemInserted(position);
        notifyParentItemInserted(position);
        notifyItemRangeChanged(position, size);
    }

    @Override
    public void onBindChildViewHolder(MeaningChildViewHolder childViewHolder, int position, Object childListItem) {
        Meaning meaning = (Meaning) childListItem;
        childViewHolder.mMeaningTextView.setText(meaning.getMeaning());
        childViewHolder.mScoreTextView.setText(""+meaning.getScore());
    }

    public class WordParentViewHolder extends ParentViewHolder {
        public TextView mParentWordTextView;
        public ImageView mParentDropDownArrow, mParentHighlight;

        public WordParentViewHolder(View itemView) {
            super(itemView);

            mParentWordTextView = (TextView) itemView.findViewById(R.id.text_view_row_word);
            mParentDropDownArrow = (ImageView) itemView.findViewById(R.id.image_drop_down_arrow);
            mParentHighlight = (ImageView) itemView.findViewById(R.id.image_highlight);

            mParentWordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    Snackbar.make(v, textView.getText(), Snackbar.LENGTH_LONG).show();
                }
            });

            mParentDropDownArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                        mParentDropDownArrow.setImageResource(android.R.drawable.arrow_down_float);
                    } else {
                        expandView();
                        mParentDropDownArrow.setImageResource(android.R.drawable.arrow_up_float);
                    }
                }
            });

        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }
    }

    public class MeaningChildViewHolder extends ChildViewHolder {

        public TextView mMeaningTextView, mScoreTextView, mViewDetailsTextView;


        public MeaningChildViewHolder(View itemView) {
            super(itemView);

            mMeaningTextView = (TextView) itemView.findViewById(R.id.text_view_row_meaning);
            mScoreTextView = (TextView) itemView.findViewById(R.id.text_view_row_child_score);
            mViewDetailsTextView = (TextView) itemView.findViewById(R.id.text_view_button_view_details);

        }
    }
}
