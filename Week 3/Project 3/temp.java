import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.io.File;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

/**
 * BoggleSolver
 */
public class BoggleSolver {
    private TST<Integer> allWords;
    // ArrayList<String> foundWords = new ArrayList<String>();

    public BoggleSolver(String[] dictionary){
        if(dictionary == null){
            throw new IllegalArgumentException();
        }
        allWords = new TST<Integer>();
        for (int i = 0; i < dictionary.length; i++) {
            allWords.put(dictionary[i], scoreOf(dictionary[i]));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        StringBuffer str = new StringBuffer();
        SET<String> temp = new SET<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                allPossibleWords(i , j,board.getLetter(i,j), board, str, temp, marked);
            }
        }
        return temp;
    }

    private void allPossibleWords(int i, int j, char charater, BoggleBoard board, StringBuffer newstr, SET<String> temp, boolean[][] newMarked){
        if(i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || newMarked[i][j]){
            return;
        }
        newstr.append(charater);
        if(board.getLetter(i, j) == 'Q') {
            newstr.append(Character.toString('U'));
        }

        newMarked[i][j] = true;

        if(newstr.length() >= 3){
            if(allWords.contains(newstr.toString())){
                temp.add(newstr.toString());
            }

            if(!allWords.containsPrefix(newstr.toString())){
                if (board.getLetter(i, j) == 'Q') {
                    newstr.delete(newstr.length() - 2, newstr.length());
                } else {
                    newstr.delete(newstr.length() - 1, newstr.length());
                }
                newMarked[i][j] = false;
                return;
            }
        }
            for (int k = i - 1; k <= i + 1; k++) {
                if((k >= 0 && k < board.rows())){
                    for (int k2 = j - 1; k2 <= j + 1; k2++) {
                        if((k2 >= 0 && k2 < board.cols())){
                            allPossibleWords(k, k2, board.getLetter(k,k2), board, newstr, temp, newMarked);
                        }
                    }
                }
                
            }

            if (board.getLetter(i, j) == 'Q') {
                newstr.delete(newstr.length() - 2, newstr.length());
            } else {
                newstr.delete(newstr.length() - 1, newstr.length());
            }

            newMarked[i][j] = false;
            // return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (!(allWords.contains(word))) {
            return 0;
        }
        if(word.length() == 3 || word.length() == 4){
            return 1;
        }else if(word.length() == 5){
            return 2;
        }else if(word.length() == 6){
            return 3;
        }else if(word.length() == 7){
            return 5;
        }else if(word.length() >= 5){
            return 11;
        }
        return 0;
    }

    // public static void main(String[] args) throws Exception {
    //     File file = new File(args[0]);
    //     Scanner sc = new Scanner(file);
    //     ArrayList<String> al = new ArrayList<>();

    //     while(sc.hasNextLine()) {
    //         al.add(sc.nextLine());
    //     }

    //     String[] s = al.toArray(new String[al.size()]);
    //     BoggleSolver bg = new BoggleSolver(s);
        
    //     BoggleBoard b1 = new BoggleBoard(args[1]);
    //     Iterable<String> it = bg.getAllValidWords(b1);
    //     int score = 0;
    //     for (String st : it) {
    //         System.out.println(st);
    //         score += bg.scoreOf(st);
    //     }
    //     System.out.println("Score :" + score);
    // }
}