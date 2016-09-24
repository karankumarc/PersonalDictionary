package com.techpalle.karan.personaldictionary.utils;

/**
 * Created by ADMIN on 9/19/2016.
 */
public class Utils {

    public static String formatWordFirstLetterCapital(String word){

        StringBuilder stringBuilderFormattedWord = new StringBuilder();

        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            if(i==0){
                char firstLetter = word.toUpperCase().charAt(0);
                stringBuilderFormattedWord.append(firstLetter);
            } else {
                stringBuilderFormattedWord.append(word.charAt(i));
            }
        }

        return stringBuilderFormattedWord.toString();
    }
}
