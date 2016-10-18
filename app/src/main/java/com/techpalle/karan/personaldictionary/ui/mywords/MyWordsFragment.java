package com.techpalle.karan.personaldictionary.ui.mywords;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.adapter.RecyclerAdapter;
import com.techpalle.karan.personaldictionary.adapter.WordMeaningExpandableRecyclerAdapter;
import com.techpalle.karan.personaldictionary.data.MyDatabase;
import com.techpalle.karan.personaldictionary.model.Meaning;
import com.techpalle.karan.personaldictionary.model.Word;
import com.techpalle.karan.personaldictionary.ui.mywords.AddWordActivity;
import com.techpalle.karan.personaldictionary.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWordsFragment extends Fragment {


    private static final int REQ_CODE_NEW_WORD = 1;

    public MyWordsFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton floatingActionButton;
    private ListView listViewMyWords;
    private ArrayList<Word> wordArrayList = new ArrayList<>();
    private MyWordsAdapter wordsAdapter;
    private MyDatabase myDatabase;
    private RecyclerAdapter recyclerAdapter;
    private WordMeaningExpandableRecyclerAdapter adapter;
    private List<Word> parentWordListItems;
    private View mView;


    //modified code
    private ImageView star;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_words, container, false);

        myDatabase = new MyDatabase(getActivity());

        floatingActionButton = (FloatingActionButton) mView.findViewById(R.id.fab_add_word);

        //This is modified code gyanesh
       /* star=(ImageView)mView.findViewById(R.id.image_highlight) ;
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                Toast.makeText(getActivity(),"Start is clicked",Toast.LENGTH_LONG).show();
            }
        });
*/

        //wordsAdapter = new MyWordsAdapter();
        //wordArrayList.addAll(myDatabase.getAllWords());

        setupRecyclerView();

        /*listViewMyWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View mView, int position, long id) {
                Word word = wordArrayList.get(position);
                Snackbar snackbar = Snackbar.make(mView, word.getMeaning(), Snackbar.LENGTH_LONG);
                snackbar.setAction(""+word.getScore(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();
            }
        });*/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWordActivity.class);
                getActivity().startActivityForResult(intent, REQ_CODE_NEW_WORD);
            }
        });
        return mView;
    }

    private void setupRecyclerView() {

        List<Word> subcategoryWordListItems = myDatabase.getAllWords();

        parentWordListItems = new ArrayList<>();

        for (Word word : subcategoryWordListItems) {
            List<Meaning> meaningsList = new ArrayList<>();
            meaningsList.add(new Meaning(word.getId(), word.getMeaning(), word.getScore()));
            word.setChildItemList(meaningsList);
            parentWordListItems.add(word);
        }

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_my_words);
        //recyclerAdapter = new RecyclerAdapter(getActivity(), myDatabase.getAllWords());
        adapter = new WordMeaningExpandableRecyclerAdapter(getActivity(), parentWordListItems);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Items show default animation even if we do not set this
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_NEW_WORD) {
            if (resultCode == getActivity().RESULT_OK) {
                String newWord = data.getExtras().getString(Constants.KEY_BUNDLE_NEW_WORD);
                Snackbar.make(floatingActionButton, newWord + " has been added to your dictionary.", Snackbar.LENGTH_LONG).show();
                refreshWords(newWord);
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Snackbar.make(floatingActionButton, R.string.snack_no_new_words_added, Snackbar.LENGTH_LONG).show();
            }
        }
    }



    private void refreshWords(String newWord) {
        //wordArrayList.clear();
        //wordArrayList.addAll(myDatabase.getAllWords());
        //wordsAdapter.notifyDataSetChanged();

        //recyclerAdapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
        int position = myDatabase.getPositionOfWordAfterArrangingAlphabetically(newWord);
        Word word = myDatabase.getWord(newWord);

        List<Word> subcategoryWordListItems = myDatabase.getAllWords();

        //List<Word> parentWordListItems = new ArrayList<>();
        List<Meaning> meaningsList = new ArrayList<>();
        meaningsList.add(new Meaning(word.getId(), word.getMeaning(), word.getScore()));
        word.setChildItemList(meaningsList);

        parentWordListItems.add(position-1, word);

        adapter.refreshData(position-1, parentWordListItems.size());
    }


    private class MyWordsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wordArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return wordArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Word word = wordArrayList.get(position);

            View view = getLayoutInflater(null).inflate(R.layout.row_word_parent, parent, false);

            TextView textViewName = (TextView) view.findViewById(R.id.text_view_row_word);

            ImageView imageViewHighlight = (ImageView) view.findViewById(R.id.image_highlight);

            textViewName.setText(word.getName());

            if (word.getIsHighlighted()) {
                imageViewHighlight.setBackgroundResource(android.R.drawable.btn_star_big_on);
            }

            return view;
        }
    }





}
