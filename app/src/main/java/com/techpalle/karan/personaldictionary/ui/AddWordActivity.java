package com.techpalle.karan.personaldictionary.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.data.MyDatabase;
import com.techpalle.karan.personaldictionary.data.PersonalDictionaryContract;
import com.techpalle.karan.personaldictionary.model.Source;
import com.techpalle.karan.personaldictionary.utils.Constants;
import com.techpalle.karan.personaldictionary.utils.Utils;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class AddWordActivity extends AppCompatActivity implements AddSourceFragment.NewSourceListener, View.OnClickListener {

    private static final int REQ_CODE_SPEECH_INPUT_NAME = 1;
    private static final int REQ_CODE_SPEECH_INPUT_MEANING = 2;
    private static final int REQ_CODE_SPEECH_INPUT_USAGE = 3;

    private MyDatabase myDatabase;
    private Spinner mSpinnerWordForm, mSpinnerSources;

    private boolean mIsHighlighted = false;
    private MySourceAdapter sourceAdapter;
    private EditText mEditTextWord, mEditTextMeaning, mEditTextUsage;
    private TextInputLayout mTextInputLayoutWord, mTextInputLayoutMeaning;
    private ImageView imageViewSpeakName, imageViewSpeakMeaning, imageViewSpeakUsage;

    ArrayList<Source> sourceArrayList = new ArrayList<>();

    String[] partsOfSpeechArray; //getResources().getStringArray(R.array.parts_of_speech);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        partsOfSpeechArray = getResources().getStringArray(R.array.parts_of_speech);

        myDatabase = new MyDatabase(this);
        sourceAdapter = new MySourceAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinnerWordForm = (Spinner) findViewById(R.id.spinner_word_form);
        mSpinnerSources = (Spinner) findViewById(R.id.spinner_source);
        Button buttonAddSource = (Button) findViewById(R.id.button_add_source);

        mEditTextWord = (EditText) findViewById(R.id.edit_text_word);
        mEditTextMeaning = (EditText) findViewById(R.id.edit_text_meaning);
        mEditTextUsage = (EditText) findViewById(R.id.edit_text_usage);

        mTextInputLayoutWord = (TextInputLayout) findViewById(R.id.input_layout_word);
        mTextInputLayoutMeaning = (TextInputLayout) findViewById(R.id.input_layout_meaning);

        imageViewSpeakName = (ImageView) findViewById(R.id.speak_name);
        imageViewSpeakMeaning = (ImageView) findViewById(R.id.speak_meaning);
        imageViewSpeakUsage = (ImageView) findViewById(R.id.speak_usage);

        imageViewSpeakName.setOnClickListener(this);
        imageViewSpeakMeaning.setOnClickListener(this);
        imageViewSpeakUsage.setOnClickListener(this);

        buttonAddSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSourceFragment addSourceFragment = new AddSourceFragment();
                addSourceFragment.show(getSupportFragmentManager(), Constants.ADD_SOURCE_DIALOG_TAG);
            }
        });

        if (savedInstanceState != null) {
            mIsHighlighted = savedInstanceState.getBoolean(Constants.KEY_BUNDLE_IS_HIGHLIGHTED);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setSubtitle("Add a new word...");

        ArrayAdapter<String> partsOfSpeechAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                partsOfSpeechArray);
        partsOfSpeechAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceArrayList.addAll(myDatabase.getAllSources());

        mSpinnerWordForm.setAdapter(partsOfSpeechAdapter);
        mSpinnerSources.setAdapter(sourceAdapter);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            switch (v.getId()) {
                case R.id.speak_name:
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt_word));
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_NAME);
                    break;
                case R.id.speak_meaning:
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt_meaning));
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_MEANING);
                    break;
                case R.id.speak_usage:
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt_word));
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_USAGE);
                    break;
            }
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
            Snackbar.make(mEditTextMeaning, getString(R.string.error_speech_not_supported), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT_NAME:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditTextWord.setText(result.get(0));
                }
                break;
            case REQ_CODE_SPEECH_INPUT_MEANING:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditTextMeaning.setText(result.get(0));
                }
                break;
            case REQ_CODE_SPEECH_INPUT_USAGE:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditTextUsage.setText(result.get(0));
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.KEY_BUNDLE_IS_HIGHLIGHTED, mIsHighlighted);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_highlight:
                if (mIsHighlighted) {
                    mIsHighlighted = false;
                    item.setIcon(android.R.drawable.btn_star_big_off);
                } else {
                    mIsHighlighted = true;
                    item.setIcon(android.R.drawable.btn_star_big_on);
                }
                break;
            case R.id.action_save:
                validateWordAndSave();
        }
        return true;
    }

    private void validateWordAndSave() {

        String word = Utils.formatWordFirstLetterCapital(mEditTextWord.getText().toString().trim());
        String meaning = mEditTextMeaning.getText().toString();

        if (validateWordAndMeaning(word, meaning)) {
            String partOfSpeech = partsOfSpeechArray[mSpinnerWordForm.getSelectedItemPosition()];
            String usage = mEditTextUsage.getText().toString();
            String source = sourceArrayList.get(mSpinnerSources.getSelectedItemPosition()).getName();
            long result = myDatabase.insertWord(word, meaning, partOfSpeech, usage, source, mIsHighlighted);
            if (result != -1) {
                //Snackbar.make(mEditTextMeaning, word + " has been added to your dictionary. ", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_BUNDLE_NEW_WORD, word);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private boolean validateWordAndMeaning(String word, String meaning) {

        boolean wordValidation = false, meaningValidation = false;

        if (word.length() < Constants.MINIMUM_WORD_LENGTH) {
            mTextInputLayoutWord.setError(getString(R.string.error_message_empty_word));
            wordValidation = false;
        } else if (myDatabase.checkIfWordExists(word)) {
            mTextInputLayoutWord.setError(getString(R.string.error_message_word_exists));
            wordValidation = false;
        } else {
            mTextInputLayoutWord.setErrorEnabled(false);
            wordValidation = true;
        }

        if (meaning.length() < Constants.MINIMUM_MEANING_LENGTH) {
            mTextInputLayoutMeaning.setError(getString(R.string.error_message_empty_meaning));
            meaningValidation = false;
        } else {
            mTextInputLayoutMeaning.setErrorEnabled(false);
            meaningValidation = true;
        }

        if (wordValidation && meaningValidation)
            return true;
        else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_word, menu);
        return true;
    }

    @Override
    public boolean sourceAddedToDatabase(String sourceName) {
        if (myDatabase.checkIfSourceExists(sourceName)) {
            return false;
        } else {
            long result = myDatabase.insertSource(sourceName);
            if (result > -1) {
                Snackbar.make(mEditTextMeaning, sourceName + " added to your list of sources.", Snackbar.LENGTH_LONG).show();
                sourceArrayList.clear();
                sourceArrayList.addAll(myDatabase.getAllSources());
                sourceAdapter.notifyDataSetChanged();
                mSpinnerSources.setSelection((int) result - 1);
                return true;
            } else {
                Snackbar.make(mEditTextMeaning, "Error adding " + sourceName + " to database. ", Snackbar.LENGTH_LONG).show();
                return false;
            }

        }
    }


    private class MySourceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sourceArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return sourceArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Source source = sourceArrayList.get(position);

            // Pending - View Holder Pattern
            View view = getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);

            textView.setText(source.getName());

            return view;
        }
    }


}
