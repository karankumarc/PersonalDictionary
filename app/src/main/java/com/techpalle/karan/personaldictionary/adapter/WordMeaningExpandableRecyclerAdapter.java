package com.techpalle.karan.personaldictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.data.MyDatabase;
import com.techpalle.karan.personaldictionary.model.Meaning;
import com.techpalle.karan.personaldictionary.model.Word;
import com.techpalle.karan.personaldictionary.ui.InDetails;

import java.util.List;

/**
 * Created by ADMIN on 9/21/2016.
 */
public class WordMeaningExpandableRecyclerAdapter extends
        ExpandableRecyclerAdapter<WordMeaningExpandableRecyclerAdapter.WordParentViewHolder, WordMeaningExpandableRecyclerAdapter.MeaningChildViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private List<Word> uwordList;
    private String word;
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
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    private WordMeaningExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList){
        super(parentItemList);
    }



    @Override
    public WordMeaningExpandableRecyclerAdapter.WordParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflater.inflate(R.layout.row_word_parent, parentViewGroup, false);
        /*ImageView imageView=(ImageView)view.findViewById(R.id.image_highlight);

        //code edited here
       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.v("Item _ clicked","Image is clicked");
           }
       });*/



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
        parentViewHolder.setData(word, position);
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

        private MyDatabase myDatabase;

        private Word currentWord;

        public TextView mParentWordTextView;
        public ImageView mParentDropDownArrow, mParentHighlight;

        public WordParentViewHolder(View itemView) {
            super(itemView);




            mParentWordTextView = (TextView) itemView.findViewById(R.id.text_view_row_word);
            mParentDropDownArrow = (ImageView) itemView.findViewById(R.id.image_drop_down_arrow);
            mParentHighlight = (ImageView) itemView.findViewById(R.id.image_highlight);

            //modified by gyanesh
           // word=mParentWordTextView.getText().toString();


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
                    word=mParentWordTextView.getText().toString();
                    if (isExpanded()) {
                        collapseView();
                        mParentDropDownArrow.setImageResource(android.R.drawable.arrow_down_float);
                    } else {
                        expandView();
                        mParentDropDownArrow.setImageResource(android.R.drawable.arrow_up_float);
                    }
                }
            });

            mParentHighlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentWord.getIsHighlighted()){
                        Log.v("get_highlighted","Turning off");
                        mParentHighlight.setImageResource(android.R.drawable.star_off);
                        currentWord.setHighlighted(false);
                        updateHighlightedValue(currentWord.getName(), false);
                    } else {
                        /**
                         * Highlight the word and save the value in the database
                         */
                        mParentHighlight.setImageResource(android.R.drawable.btn_star_big_on);
                        currentWord.setHighlighted(true);

                        updateHighlightedValue(currentWord.getName(), true);
                    }
                }
            });

        }

        public void updateHighlightedValue(String name, boolean newIsHighlightedValue){
            myDatabase = new MyDatabase(context);
            long result = myDatabase.updateHighlightedValue(name, newIsHighlightedValue);

            if(result == 1){
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }

        public void setData(Word word, int position) {

            currentWord = word;

            mParentWordTextView.setText(word.getName());
            if (word.getIsHighlighted()) {
                mParentHighlight.setImageResource(android.R.drawable.btn_star_big_on);
            }

            if (isExpanded()) {
                mParentDropDownArrow.setImageResource(android.R.drawable.arrow_up_float);
            } else {
                mParentDropDownArrow.setImageResource(android.R.drawable.arrow_down_float);
            }
        }
    }

    public class MeaningChildViewHolder extends ChildViewHolder {

        public TextView mMeaningTextView, mScoreTextView, mViewDetailsTextView;


        public MeaningChildViewHolder(View itemView) {
            super(itemView);

            mMeaningTextView = (TextView) itemView.findViewById(R.id.text_view_row_meaning);
            mScoreTextView = (TextView) itemView.findViewById(R.id.text_view_row_child_score);
            mViewDetailsTextView = (TextView) itemView.findViewById(R.id.text_view_button_view_details);

            mViewDetailsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String meaning=mMeaningTextView.getText().toString();
                    Intent i=new Intent(context, InDetails.class);
                    i.putExtra("word",word);
                    i.putExtra("meaning",meaning);
                    context.startActivity(i);
                }
            });


        }
    }
}
