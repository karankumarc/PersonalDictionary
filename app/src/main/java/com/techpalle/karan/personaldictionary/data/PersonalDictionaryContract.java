package com.techpalle.karan.personaldictionary.data;


import android.provider.BaseColumns;


public final class PersonalDictionaryContract{

    private PersonalDictionaryContract(){
    }

    public static final class MyWordsEntry implements BaseColumns {
        public static final String TABLE_NAME = "my_words";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_MEANING = "meaning";
        public static final String COLUMN_WORD_FORM = "word_form";
        public static final String COLUMN_USAGE = "usage";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_HIGHLIGHT = "highlight";
    }

    public static final class MySourcesEntry implements BaseColumns {
        public static final String TABLE_NAME = "my_sources";
        public static final String COLUMN_SOURCE_NAME = "source_name";
    }
}
