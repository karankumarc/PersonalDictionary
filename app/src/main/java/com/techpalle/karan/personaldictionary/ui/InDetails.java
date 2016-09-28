package com.techpalle.karan.personaldictionary.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.techpalle.karan.personaldictionary.R;

public class InDetails extends AppCompatActivity {

    private TextView word;
    private TextView meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_details);

        //Create by gyanesh
        word=(TextView)findViewById(R.id.txt_detail_word);
        meaning=(TextView)findViewById(R.id.txt_detail_meaning);


        Bundle b=getIntent().getExtras();
        String meaningData=b.getString("meaning");
        String wordData=b.getString("word");

        word.setText(wordData);
        meaning.setText(meaningData);




    }
}
