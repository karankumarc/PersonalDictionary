package com.techpalle.karan.personaldictionary.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techpalle.karan.personaldictionary.data.PersonalDictionaryContract.MySourcesEntry;
import com.techpalle.karan.personaldictionary.data.PersonalDictionaryContract.MyWordsEntry;
import com.techpalle.karan.personaldictionary.model.Source;
import com.techpalle.karan.personaldictionary.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyDatabase {

    private SQLiteDatabase database;
    private PersonalDictionaryDbHelper helper;

    public MyDatabase(Context context) {
        helper = new PersonalDictionaryDbHelper(context);
    }

    private SQLiteDatabase openReadableDatabaseInstance() {
        return helper.getReadableDatabase();
    }

    private SQLiteDatabase openWritableDatabaseInstance() {
        return helper.getWritableDatabase();
    }

    private void closeDatabaseConnection() {
        database.close();
        helper.close();
    }



    /*public long updateTableName(long id, String column1, int column2){

        database = openWritableDatabaseInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableNameEntry.COLUMN_1_NAME, column1);
        contentValues.put(TableNameEntry.COLUMN_2_NAME, column2);


        String selection = TableNameEntry._ID +" = ? ";
        String[] selectionArgs = {String.valueOf(id)};

        long value = database.update(TableNameEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        closeDatabaseConnection();

        return value;
    }


    public long deleteAllTableDetails() {
        database = openWritableDatabaseInstance();

        long l= database.delete(TableNameEntry.TABLE_NAME, null, null);

        closeDatabaseConnection();

        return l;
    }

    public ArrayList<Table> getTableDataInArrayList() {
        database = openReadableDatabaseInstance();

        Cursor c =  database.query(TableNameEntry.TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Table> arrayListTables = new ArrayList<>();

        if(c.moveToFirst()){
            do{
                //String stringDate = Utils.SIMPLE_DATE_FORMAT.format(date.getTime());
                Table table = new Table(c.getInt(c.getColumnIndex(TableNameEntry._ID)),
                        c.getString(c.getColumnIndex(TableNameEntry.COLUMN_1_NAME)),
                        c.getInt(c.getColumnIndex(TableNameEntry.COLUMN_2_NAME)));
                arrayListTables.add(table);
            }while (c.moveToNext());
        }
        closeDatabaseConnection();

        return arrayListTables;
    }
     */

    public long updateHighlightedValue(String word, boolean isHighlighted) {
        database = openReadableDatabaseInstance();
        //Cursor cursor = database.query(MySourcesEntry.TABLE_NAME, null, null, null, null, null, null);
        ContentValues contentValues = new ContentValues();
        if (isHighlighted)
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 1);
        else
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 0);

        String whereClause = MyWordsEntry.COLUMN_WORD + " = ?";
        String[] whereArgs = {word};
        long result = database.update(MyWordsEntry.TABLE_NAME, contentValues, whereClause, whereArgs);

        closeDatabaseConnection();

        return result;
    }

    public ArrayList<String> getAllSources() {

        database = openReadableDatabaseInstance();

        String[] projections = {MySourcesEntry._ID, MySourcesEntry.COLUMN_SOURCE_NAME};

        String orderBy = MySourcesEntry.COLUMN_SOURCE_NAME+ " ASC";

        Cursor cursor = database.query(MySourcesEntry.TABLE_NAME, projections, null, null, null, null, orderBy);

        ArrayList<String> sourceArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                /*Source source = new Source(cursor.getInt(cursor.getColumnIndex(MySourcesEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(MySourcesEntry.COLUMN_SOURCE_NAME)));*/
                sourceArrayList.add(cursor.getString(cursor.getColumnIndex(MySourcesEntry.COLUMN_SOURCE_NAME)));

            } while (cursor.moveToNext());
        }

        closeDatabaseConnection();

        return sourceArrayList;

    }


    public int getPositionOfWordAfterArrangingAlphabetically(String word) {

        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID, MyWordsEntry.COLUMN_WORD, MyWordsEntry.COLUMN_MEANING,
                MyWordsEntry.COLUMN_SCORE, MyWordsEntry.COLUMN_HIGHLIGHT};
        String orderBy = MyWordsEntry.COLUMN_WORD + " ASC ";

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, null, null, null, null, orderBy);
        ArrayList<Word> wordArrayList = new ArrayList<>();


        int count = 0;
        int finalPosition = -1;
        if (cursor.moveToFirst()) {
            do {
                count++;
                if (cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_WORD)).equals(word)) {
                    finalPosition = count;
                }
            } while (cursor.moveToNext());
        }

        closeDatabaseConnection();

        return finalPosition;
    }


    public ArrayList<Word> getAllWords() {

        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID, MyWordsEntry.COLUMN_WORD, MyWordsEntry.COLUMN_MEANING,
                MyWordsEntry.COLUMN_SCORE, MyWordsEntry.COLUMN_HIGHLIGHT};
        String orderBy = MyWordsEntry.COLUMN_WORD + " ASC ";

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, null, null, null, null, orderBy);
        ArrayList<Word> wordArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word(cursor.getInt(cursor.getColumnIndex(MyWordsEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_WORD)),
                        cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_MEANING)),
                        cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_HIGHLIGHT)));
                wordArrayList.add(word);
            } while (cursor.moveToNext());
        }

        closeDatabaseConnection();

        return wordArrayList;

    }

    public boolean checkIfSourceExists(String source) {

        database = openReadableDatabaseInstance();

        String sql = "SELECT * FROM " + MySourcesEntry.TABLE_NAME + " WHERE " + MySourcesEntry.COLUMN_SOURCE_NAME + " = '" + source + "';";
        Cursor data = database.rawQuery(sql, null);

        boolean sourceExists = false;
        if (data.moveToFirst()) {
            sourceExists = true;
        } else {
            sourceExists = false;
        }

        closeDatabaseConnection();

        return sourceExists;
    }

    public long insertSource(String source) {

        database = openWritableDatabaseInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MySourcesEntry.COLUMN_SOURCE_NAME, source);

        long value = database.insert(MySourcesEntry.TABLE_NAME, null, contentValues);

        closeDatabaseConnection();

        return value;

    }

    public boolean checkIfWordExists(String word) {

        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID};

        String selection = MyWordsEntry.COLUMN_WORD + " = ? ";

        String[] selectionArgs = {word};

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        boolean wordExists = false;

        if (cursor.moveToFirst()) {
            wordExists = true;
        } else {
            wordExists = false;
        }

        closeDatabaseConnection();

        return wordExists;
    }

    public long insertWord(String word, String meaning, String wordForm, String usage, String source, boolean isHighlighted) {

        database = openWritableDatabaseInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyWordsEntry.COLUMN_WORD, word);
        contentValues.put(MyWordsEntry.COLUMN_MEANING, meaning);
        contentValues.put(MyWordsEntry.COLUMN_WORD_FORM, wordForm);
        contentValues.put(MyWordsEntry.COLUMN_USAGE, usage);
        contentValues.put(MyWordsEntry.COLUMN_SOURCE, source);
        contentValues.put(MyWordsEntry.COLUMN_SCORE, 0);

        if (isHighlighted)
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 1);
        else
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 0);


        long value = database.insert(MyWordsEntry.TABLE_NAME, null, contentValues);

        closeDatabaseConnection();

        return value;

    }

    public Word getWordWithId(int id) {

        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID, MyWordsEntry.COLUMN_WORD, MyWordsEntry.COLUMN_MEANING,
                MyWordsEntry.COLUMN_SCORE, MyWordsEntry.COLUMN_HIGHLIGHT, MyWordsEntry.COLUMN_USAGE,
                MyWordsEntry.COLUMN_WORD_FORM, MyWordsEntry.COLUMN_SOURCE};
        String selection = MyWordsEntry._ID + " = ? ";
        String[] selectionArgs = {""+id};

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        Word word = null;

        if (cursor.moveToFirst()) {
            word = new Word(cursor.getInt(cursor.getColumnIndex(MyWordsEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_WORD)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_MEANING)),
                    cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_SCORE)),
                    cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_HIGHLIGHT)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_USAGE)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_SOURCE)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_WORD_FORM))
                    );

        }

        closeDatabaseConnection();

        return word;
    }

    public Word getWord(String newWord) {

        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID, MyWordsEntry.COLUMN_WORD, MyWordsEntry.COLUMN_MEANING,
                MyWordsEntry.COLUMN_SCORE, MyWordsEntry.COLUMN_HIGHLIGHT};
        String selection = MyWordsEntry.COLUMN_WORD + " = ? ";
        String[] selectionArgs = {newWord};

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        Word word = null;

        if (cursor.moveToFirst()) {
            word = new Word(cursor.getInt(cursor.getColumnIndex(MyWordsEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_WORD)),
                    cursor.getString(cursor.getColumnIndex(MyWordsEntry.COLUMN_MEANING)),
                    cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_SCORE)),
                    cursor.getInt(cursor.getColumnIndex(MyWordsEntry.COLUMN_HIGHLIGHT)));

        }

        closeDatabaseConnection();

        return word;
    }

    public long updateWord(int id, String word, String meaning, String partOfSpeech, String usage, String source, boolean isHighlighted) {
        database = openWritableDatabaseInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyWordsEntry.COLUMN_WORD, word);
        contentValues.put(MyWordsEntry.COLUMN_MEANING, meaning);
        contentValues.put(MyWordsEntry.COLUMN_WORD_FORM, partOfSpeech);
        contentValues.put(MyWordsEntry.COLUMN_USAGE, usage);
        contentValues.put(MyWordsEntry.COLUMN_SOURCE, source);
        contentValues.put(MyWordsEntry.COLUMN_SCORE, 0);

        if (isHighlighted)
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 1);
        else
            contentValues.put(MyWordsEntry.COLUMN_HIGHLIGHT, 0);

        String selection = MyWordsEntry._ID + " = ?";
        String[] selectionArgs = {""+id};


        long value = database.update(MyWordsEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        closeDatabaseConnection();

        return value;
    }

    public boolean checkIfEditedWordExists(String word, int id) {
        database = openReadableDatabaseInstance();

        String[] projections = {MyWordsEntry._ID};

        String selection = MyWordsEntry.COLUMN_WORD + " = ? ";

        String[] selectionArgs = {word};

        Cursor cursor = database.query(MyWordsEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        boolean wordExists = false;

        if (cursor.moveToFirst()) {
            int returnedId = cursor.getInt(0);

            if(returnedId == id){
                return false;
            } else {
                return true;
            }
        } else {
            wordExists = false;
        }

        closeDatabaseConnection();

        return wordExists;
    }

    public int deleteWord(int id) {
        database = openWritableDatabaseInstance();

        String selection = MyWordsEntry._ID +" = ?";
        String[] selectionArgs = {""+id};

        int result = database.delete(MyWordsEntry.TABLE_NAME, selection, selectionArgs);

        closeDatabaseConnection();

        return result;

    }

    private class PersonalDictionaryDbHelper extends SQLiteOpenHelper {

        //region SQL Statements
        private static final String SQL_CREATE_MY_WORDS_TABLE = "CREATE TABLE " + MyWordsEntry.TABLE_NAME + "("
                + MyWordsEntry._ID + " INTEGER PRIMARY KEY, "
                + MyWordsEntry.COLUMN_WORD + " TEXT NOT NULL, "
                + MyWordsEntry.COLUMN_MEANING + " TEXT NOT NULL, "
                + MyWordsEntry.COLUMN_WORD_FORM + " TEXT NOT NULL, "
                + MyWordsEntry.COLUMN_USAGE + " TEXT, "
                + MyWordsEntry.COLUMN_SOURCE + " TEXT, "
                + MyWordsEntry.COLUMN_HIGHLIGHT + " BOOLEAN, "
                + MyWordsEntry.COLUMN_SCORE + " INT NOT NULL);";

        private static final String SQL_CREATE_MY_SOURCES_TABLE = "CREATE TABLE " + MySourcesEntry.TABLE_NAME + "("
                + MySourcesEntry._ID + " INTEGER PRIMARY KEY, "
                + MySourcesEntry.COLUMN_SOURCE_NAME + " TEXT NOT NULL);";


        private final String SQL_DROP_MY_WORDS_TABLE = "DROP TABLE " + MyWordsEntry.TABLE_NAME + ";";
        private final String SQL_DROP_MY_SOURCES_TABLE = "DROP TABLE " + MySourcesEntry.TABLE_NAME + ";";
        //endregion

        private static final String DATABASE_NAME = "PersonalDictionary.db";

        private static final int DATABASE_VERSION = 1;

        public PersonalDictionaryDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MY_WORDS_TABLE);
            db.execSQL(SQL_CREATE_MY_SOURCES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                db.execSQL(SQL_DROP_MY_SOURCES_TABLE);
                db.execSQL(SQL_DROP_MY_WORDS_TABLE);
                onCreate(db);
            }
        }
    }


}
