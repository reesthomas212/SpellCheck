package com.Spell;

import java.io.*;
import java.lang.*;
import java.util.*;
/**
 * Created by mlsfa on 7/2/2017.
 */

public class SpellCorrector implements ISpellCorrector
{
    public TreeSet<String> ED1 = new TreeSet();
    public TreeSet<String> ED2 = new TreeSet();
    Trie t = new Trie();

    public void useDictionary(String dictionaryFileName) throws IOException
    {
        File temp = new File(dictionaryFileName);
        Scanner file = new Scanner(temp);

        while (file.hasNext())
        {
            String str = file.next();
            t.add(str);
        }

        //System.out.print(t.toString());
    }
    public String suggestSimilarWord(String inputWord)
    {
        if (t.find(inputWord) != null) //if word is in dictionary
        {
            return inputWord.toLowerCase();
        }
        if (inputWord.length() == 0)
        {
            return null;
        }

        String suggestion = new String();
        compileED1(inputWord);
        //create found and unfound sets
        TreeSet<String> found = new TreeSet<>();
        TreeSet<String> unfound = new TreeSet<>();
        while (!(ED1.isEmpty()))
        {
            if (t.find(ED1.first()) == null) //unfound
            {
                unfound.add(ED1.first());
                ED1.remove(ED1.first());
            }
            else //found
            {
                found.add(ED1.first());
                ED1.remove(ED1.first());
            }
        }

        //determine if ED2 is needed
        if (found.isEmpty())
        {
            suggestion = suggestSimilarWord2(unfound, inputWord);
        }
        else
        {
            suggestion = determineSuggestedWord(found);
        }

        return suggestion.toLowerCase();
    }
    public void compileED1(String word)
    {
        TreeSet<String> A = insertion(word);
        TreeSet<String> B = deletion(word);
        TreeSet<String> C = transposition(word);
        TreeSet<String> D = alteration(word);
        ED1.addAll(A);
        ED1.addAll(B);
        ED1.addAll(C);
        ED1.addAll(D);

        //call and add elements from four spelling error methods into ED1

    }
    public void compileED2(TreeSet<String> unfound)
    {
        while (!(unfound.isEmpty()))
        {
            TreeSet<String> A = insertion(unfound.first());
            TreeSet<String> B = deletion(unfound.first());
            TreeSet<String> C = transposition(unfound.first());
            TreeSet<String> D = alteration(unfound.first());

            ED2.addAll(A);
            ED2.addAll(B);
            ED2.addAll(C);
            ED2.addAll(D);

            unfound.remove(unfound.first());
        }
    }
    public TreeSet<String> insertion(String word)
    {
        TreeSet<String> insert = new TreeSet<>();
        StringBuilder S = new StringBuilder();
        S.append(word);
        for (int i = 0; i < word.length()+1; i++)
        {
            for (int j = 0; j < 26; j++)
            {
                //insert every letter into every space and add all possible combinations to insert
                char c = (char)(j+97);
                S.insert(i, c);
                insert.add(S.toString());
                S.deleteCharAt(i);
            }
        }
        return insert;
    }
    public TreeSet<String> deletion(String word)
    {
        TreeSet<String> delete = new TreeSet<>();
        if (word.length() == 0)
        {
            //System.out.println("Nothing to delete");
            return delete;
        }
        StringBuilder S = new StringBuilder();
        S.append(word);
        for (int i = 0; i < word.length(); i++)
        {
            char c = S.charAt(i);
            S.deleteCharAt(i);
            delete.add(S.toString());
            S.replace(0,S.length(),word);
        }
        return delete;
    }
    public TreeSet<String> transposition(String word) {
        TreeSet<String> transpose = new TreeSet<>();

        for (int i = 0; i < word.length() - 1; i++)
        {
            char[] c = word.toCharArray();
            char temp = c[i];
            c[i] = c[i+1];
            c[i+1] = temp;

            String swapped = new String(c);
            transpose.add(swapped);
        }

        return transpose;
    }
    public TreeSet<String> alteration(String word)
    {
        TreeSet<String> alter = new TreeSet<>();

        for (int i = 0; i < word.length(); i++)
        {
            for (int j = 0; j < 26; j++)
            {
                //first, create char that will replace char in word

                char[] C = word.toCharArray();
                char newChar = (char) (j+'a');
                if (C[i] != newChar)
                {
                    C[i] = newChar;
                    String swapped = new String(C);
                    alter.add(swapped);
                }
            }
        }

        return alter;
    }
    public String determineSuggestedWord(TreeSet<String> found)
    {
        //first freq count, then alphabetical
        //alphabetical is just first thing in set
        //determine what the highest freq count is, then add words to a new set. if there isn't a tie, use that word. if not, alphabetically first
        String suggestion = null;

        if (found.size() == 0)
        {
            return suggestion;
        }
        else if (found.size() == 1)
        {
            suggestion = found.first();
        }
        else
        {
            Iterator it;
            it = found.iterator();
            String best = found.first();
            it.next();
            while (it.hasNext())
            {
                String next = (String) it.next();
                if (t.find(best).getValue() < t.find(next).getValue())
                {
                    //System.out.println("Freq. of best: " + best + " is " + t.find(best).getValue());
                    //System.out.println("Freq. of best: " + best + " is " + t.find(best).getValue());
                    best = next;
                }
                else if (t.find(best).getValue() == t.find(next).getValue())
                {
                    if (best.compareTo(next) > 0)
                    {
                        best = next;
                    }
                }
            }
            suggestion = best;
        }

        return suggestion;
    }
    public String suggestSimilarWord2(TreeSet<String> unfound, String word)
    {
        compileED2(unfound);
        TreeSet<String> found2 = new TreeSet<>();
        TreeSet<String> unfound2 = new TreeSet<>();
        while (!(ED2.isEmpty()))
        {
            if (t.find(ED2.first()) == null) //unfound
            {
                unfound2.add(ED2.first());
                ED2.remove(ED2.first());
            }
            else //found
            {
                found2.add(ED2.first());
                ED2.remove(ED2.first());
            }
        }
        if (found2.contains(word))
        {
            found2.remove(word);
        }
        //determine if there are any found words
        return determineSuggestedWord(found2);
    }

    public static void main(String[] args) throws IOException
    {
        SpellCorrector s = new SpellCorrector();
        s.useDictionary(args[0]);
        String str = s.suggestSimilarWord(args[1]);
        if (str != null)
        {
            System.out.println(str);

        }
        else System.out.println("Nope!");
    }

}
