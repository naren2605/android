/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
   private HashMap<Integer,ArrayList<String>>sizeToWords =new HashMap<>();
    private int wordLength;
    private List<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;
    public AnagramDictionary(Reader reader) throws IOException {
        wordList=new ArrayList<>();
        wordSet=new HashSet<>();
        lettersToWord=new HashMap<>();
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordLength=DEFAULT_WORD_LENGTH;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            String sortedWord=sortLetters(word);
            wordSet.add(sortedWord);
            if(lettersToWord.get(sortedWord)==null){
                lettersToWord.put(sortedWord,new ArrayList<String>());
            }
            lettersToWord.get(sortedWord).add(word);
            if(sizeToWords.get(word.length())==null){
                sizeToWords.put(word.length(),new ArrayList<String>() );
            }
            sizeToWords.get(word.length()).add(word);
        }
        Collections.sort(wordList);
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(sortLetters(word))&&(!word.contains(base));
    }

    private String  sortLetters(String str){

        char[] tmp=str.toCharArray();
        Arrays.sort(tmp);
        str=new String(tmp);
        return str;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord=sortLetters(targetWord);
        if(wordSet.contains(sortedWord)){
            result.addAll(lettersToWord.get(sortedWord));
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String sword=sortLetters(word);
        for(String anagramWord:lettersToWord.get(sword)){
            for(char c:"abcdefghijklmnopqrstuvwxyz".toCharArray()){
                //upgradeit to sort and binary search
                String append=anagramWord+c;
                String prepend=c+anagramWord;

                if(Collections.binarySearch(wordList,append)>=0){
                    result.add(append);
                }
                if(Collections.binarySearch(wordList,prepend)>=0){
                    result.add(prepend);
                }
            }

        }
        return result;
    }

    public String pickGoodStarterWord() {

        int select;
        while(!((select=random.nextInt(sizeToWords.get(wordLength).size()))>=0&&(sizeToWords.get(wordLength).size()>=1)&&(getAnagramsWithOneMoreLetter(sizeToWords.get(wordLength).get(select)).size()>MIN_NUM_ANAGRAMS)));
        if(wordLength<MAX_WORD_LENGTH-1){
            wordLength++;
        }

        return sizeToWords.get(wordLength).get(select);
    }
}
