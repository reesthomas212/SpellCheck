package com.Spell;

import java.lang.*;

/**
 * Created by mlsfa on 7/2/2017.
 */

public class Trie implements ITrie
{
    Node root = new Node();
    int wordCount = 0;
    int nodeCount = 1;


    public class Node implements INode
    {
        public Node[] array = new Node[26];
        public int freq = 0;
        public void increaseFreq()
        {
            freq = freq + 1;
        }
        public int getValue()
        {
            return freq;
        }
        public Node[] getArray()
        {
            return array;
        }
    }

    public void add(String word)
    {
        //System.out.println("Char being added: " + word.charAt(0));
        if (word.equals("")) {return;}
        word = word.toLowerCase();
        char[] word_array = word.toCharArray();
        for (int i = 0; i < word_array.length; i++)
        {
            if (word_array[i] > 122 && word_array[i] < 97)
            {
                //System.out.println("Invalid word");
                return;
            }
        }
        StringBuilder S = new StringBuilder();
        S.append(word);
        int position = S.charAt(0) - 'a';
        //System.out.println("First position: " + position);
        if (root.getArray()[position] == null) //if node doesn't exist in root's array
        {
            Node n = new Node();
            root.getArray()[position] = n;
            nodeCount++;
            S.deleteCharAt(0);
            if (S.length() != 0)
            {
                addRec(root.getArray()[position], S);
            }
            else
            {
                root.getArray()[position].increaseFreq();
                wordCount++;
                return;
            }
        }
        else
        {
            S.deleteCharAt(0);
            if (S.length() != 0)
            {
                addRec(root.getArray()[position], S);
            }
            else
            {
                root.getArray()[position].increaseFreq();
                wordCount++;
                return;
            }
        }
    }
    public void addRec(Node n, StringBuilder S)
    {
        //System.out.println("Char being added: " + S.charAt(0));
        int position = S.charAt(0) - 'a';
        if (n.getArray()[position] == null) //if node doesn't exist in root's array
        {
            Node m = new Node();
            n.getArray()[position] = m;
            nodeCount++;
            S.deleteCharAt(0);
            if (S.length() != 0)
            {
                addRec(n.getArray()[position], S);
            }
            else
            {
                n.getArray()[position].increaseFreq();
                wordCount++;
            }
        }
        else
        {
            S.deleteCharAt(0);
            if (S.length() != 0)
            {
                addRec(n.getArray()[position], S);
            }
            else
            {
                n.getArray()[position].increaseFreq();
                wordCount++;
            }
        }
    }
    public Node find(String word)
    {
        StringBuilder S = new StringBuilder();
        S.append(word);
        if (S.length() == 0)
        {
            return null;
        }
        else
        {
            int position = S.charAt(0) - 'a';
            if (root.getArray()[position] == null)
            {
                return null;
            }
            else
            {
                S.deleteCharAt(0);
                return findRec(S, root.getArray()[position]);
            }
        }
    }
    public Node findRec(StringBuilder S, Node n)
    {
        if (S.length() == 0)
        {
            if (n.getValue() != 0)
            {
                return n;
            }
            else
            {
                return null;
            }
        }
        else
        {
            int position = S.charAt(0) - 'a';
            if (n.getArray()[position] == null)
            {
                return null;
            }
            else
            {
                S.deleteCharAt(0);
                return findRec(S, n.getArray()[position]);
            }
        }
    }
    public int getWordCount() { return wordCount; }
    public int getNodeCount()
    {
        return nodeCount;
    }
    @Override
    public String toString()
    {
        StringBuilder S = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < root.getArray().length; i++)
        {
            if (root.getArray()[i] != null)
            {
                char c = (char) (i + 97);
                S.append(c);
                if (root.getArray()[i].getValue() != 0)
                {
                    result.append(S);
                    result.append('\n');
                }
                toStringRec(S, result, root.getArray()[i]);
            }
        }
        if (S.length() != 0)
        {
            S.deleteCharAt(S.length()-1);
        }
       //System.out.println(result.toString());

        return result.toString();
    }
    public void toStringRec(StringBuilder S, StringBuilder result, Node n)
    {
        for (int i = 0; i < n.getArray().length; i++)
        {
            if (n.getArray()[i] != null)
            {
                char c = (char) (i + 97);
                S.append(c);
                if (n.getArray()[i].getValue() != 0)
                {
                    result.append(S);
                    result.append('\n');
                }
                toStringRec(S, result, n.getArray()[i]);
            }
        }
        if (S.length() != 0)
        {
            S.deleteCharAt(S.length()-1);
        }
    }
    @Override
    public int hashCode()
    {
        return ((nodeCount * 17) + (wordCount * 72));
    }
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Trie)
        {
            Trie T = (Trie) o;
            if (nodeCount == T.getNodeCount()) {}
            else {return false;}
            if (wordCount == T.getWordCount()) {}
            else {return  false;}
            if (equalsRec(root,T.root) == true) {}
            else {return false;}
        }
        else
        {
            return false;
        }
        return true;
    }
    public boolean equalsRec(Node n1, Node n2)
    {

        if (n1 == null && n2 != null)return false;
        else if (n1 != null && n2 == null)return false;
        else if ((n1 == null && n2 == null)) return true;
        else if (n1 != null && n2 != null)
        {
            if (n1.getValue() != n2.getValue()) {return false;}
            for (int i = 0; i < 26; i++)
            {
                equalsRec(n1.getArray()[i], n2.getArray()[i]);
            }
        }
        return true;
    }
}
