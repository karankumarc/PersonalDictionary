package com.techpalle.karan.personaldictionary.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.model.Word;


import java.util.List;

/**
 * Created by ADMIN on 9/20/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Word> mData;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, List<Word> mData) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_word_parent, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Word currentWord = mData.get(position);
        holder.setData(currentWord, position);
        holder.setListeners();
    }

    public void removeWord(int position){
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    public void addWord(int position, Word word){
        mData.add(position, word);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mData.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTextWord;
        private ImageView mImageHighlight, mImageView;
        private int position;
        private Word currentWord;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextWord = (TextView) itemView.findViewById(R.id.text_view_row_word);
            mImageHighlight = (ImageView) itemView.findViewById(R.id.image_highlight);
            //mImageView = (ImageView) itemView.findViewById(R.id.image_view_word);
        }

        public void setData(Word currentWord, int position) {
            this.position = position;
            this.currentWord = currentWord;
            mTextWord.setText(currentWord.getName());
            if(currentWord.getIsHighlighted()){
                mImageHighlight.setBackgroundResource(android.R.drawable.btn_star_big_on);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.text_view_row_word)
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

        public void setListeners() {
            mTextWord.setOnClickListener(MyViewHolder.this);
        }
    }
}
