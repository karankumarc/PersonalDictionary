package com.techpalle.karan.personaldictionary.ui.mywords;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.data.MyDatabase;
import com.techpalle.karan.personaldictionary.model.Word;
import com.techpalle.karan.personaldictionary.utils.Constants;

public class WordDetailsActivity extends AppCompatActivity {

    private TextView textViewMeaning, textViewWordForm, textViewUsage, textViewSource, textViewScore;
    private Word mWord;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        textViewWordForm = (TextView) findViewById(R.id.txt_word_form);
        textViewMeaning = (TextView) findViewById(R.id.txt_meaning);
        textViewUsage = (TextView) findViewById(R.id.txt_usage);
        textViewSource = (TextView) findViewById(R.id.txt_word_source);
        textViewScore = (TextView) findViewById(R.id.txt_word_score);

        database = new MyDatabase(this);

        Bundle b = getIntent().getExtras();
        int id = b.getInt(Constants.KEY_BUNDLE_WORD_ID);

        mWord = database.getWordWithId(id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mWord.getName());

        textViewWordForm.setText(mWord.getWordForm());
        textViewMeaning.setText(mWord.getMeaning());
        textViewUsage.setText("Usage: " + mWord.getUsage());
        textViewSource.setText("Source: " + mWord.getSource());
        textViewScore.setText("Score: " + mWord.getScore());

        //Create by gyanesh
        //word=(TextView)findViewById(R.id.txt_detail_word);
        //meaning=(TextView)findViewById(R.id.txt_detail_meaning);


        //String wordData=b.getString("word");

        /*word.setText(wordData);
        meaning.setText(meaningData);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word_details, menu);

        MenuItem item = menu.getItem(0);
        if (mWord.getIsHighlighted()) {
            item.setIcon(android.R.drawable.btn_star_big_on);
        } else {
            item.setIcon(android.R.drawable.btn_star_big_off);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_highlight:
                handleWordHighlighted(item);
                break;
            case R.id.action_edit:
                /*validateWordAndSave();*/
                Intent intent = new Intent(WordDetailsActivity.this, AddWordActivity.class);
                intent.putExtra(Constants.KEY_BUNDLE_WORD_POJO, mWord);
                startActivity(intent);
                finish();
                break;
            case R.id.action_delete:
                deleteWordFromDatabase();
        }
        return true;
    }

    private void deleteWordFromDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete word permanently...");
        builder.setIcon(android.R.drawable.ic_menu_delete);
        builder.setMessage("Are you sure you want to delete the word permanently from the database?");
        //builder.setCancelable(false); // Set if the dialog can be closed by clicking outside the bounds

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Handle positive click
                int value = database.deleteWord(mWord.getId());
                if(value > 0){
                    Toast.makeText(WordDetailsActivity.this, mWord.getName()+" has been successfully deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(WordDetailsActivity.this, "Error occurred while deleting. Try again. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Don't delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //endregion

        //region Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleWordHighlighted(MenuItem item) {
        long result;
        if(mWord.getIsHighlighted()){
            item.setIcon(android.R.drawable.btn_star_big_off);
            mWord.setHighlighted(false);
            result = database.updateHighlightedValue(mWord.getName(), false);

        } else {
            item.setIcon(android.R.drawable.btn_star_big_on);
            mWord.setHighlighted(true);
            result = database.updateHighlightedValue(mWord.getName(), true);
        }

        if(result > 0){
            if(mWord.getIsHighlighted()){
                Toast.makeText(WordDetailsActivity.this, mWord.getName()+ " added to mastered list.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WordDetailsActivity.this, mWord.getName()+ " removed from mastered list.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
