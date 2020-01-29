import java.util.Arrays;

import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.In;

/**
 * BoggleSolver
 */
public class BoggleSolver {
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        TrieST<Integer> trieSt = new TrieST();
        // System.out.println(Arrays.toString(dictionary));
        for (String word : dictionary) {
            int wordLength = scoreOf(word);
            trieSt.put(word, wordLength);
        }
    }

    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    // public Iterable<String> getAllValidWords(BoggleBoard board) {    }

    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int wordLength = word.length();
        
        if (wordLength == 3 || wordLength == 4) return 1;

        else if (wordLength == 5) return 2;

        else if (wordLength == 6) return 3;

        else if (wordLength == 7) return 5;

        else if (wordLength >= 8) return 11;

        else return -1;
    }

    public static void main(String[] args) {
        // String[] wordsArray = new String[] {"hi", "hai", "hello", "abcdef", "abcdefg", "abcdefghssd", "hs"};
        In in = new In(args[0]);
        String[] wordsArray = in.readAllStrings();
        BoggleSolver boggleSolver = new BoggleSolver(wordsArray);
        // for (int i = 0; i < wordsArray.length; i++) {
        //     System.out.println(boggleSolver.scoreOf(wordsArray[i]));            
        // }

    }
}