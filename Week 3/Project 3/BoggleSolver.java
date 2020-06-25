import java.util.Arrays;
// import java.util.Set;

// import edu.princeton.cs.algs4.TST;
// import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

class TST<Value> {
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
    * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }


    public boolean containsPrefix(String string) {
        if (get(root, string, 0) != null) {
            return true;
        } else return false;
    }


    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }


    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
    //  */
    // public static void main(String[] args) {

    //     // build symbol table from standard input
    //     TST<Integer> st = new TST<Integer>();
    //     for (int i = 0; !StdIn.isEmpty(); i++) {
    //         String key = StdIn.readString();
    //         st.put(key, i);
    //     }

    //     // print results
    //     if (st.size() < 100) {
    //         StdOut.println("keys(\"\"):");
    //         for (String key : st.keys()) {
    //             StdOut.println(key + " " + st.get(key));
    //         }
    //         StdOut.println();
    //     }

    //     StdOut.println("longestPrefixOf(\"shellsort\"):");
    //     StdOut.println(st.longestPrefixOf("shellsort"));
    //     StdOut.println();

    //     StdOut.println("longestPrefixOf(\"shell\"):");
    //     StdOut.println(st.longestPrefixOf("shell"));
    //     StdOut.println();

    //     StdOut.println("keysWithPrefix(\"shor\"):");
    //     for (String s : st.keysWithPrefix("shor"))
    //         StdOut.println(s);
    //     StdOut.println();

    //     StdOut.println("keysThatMatch(\".he.l.\"):");
    //     for (String s : st.keysThatMatch(".he.l."))
    //         StdOut.println(s);
    // }
}

/** 
 * BoggleSolver
 */
public class BoggleSolver {
    
    private TST<Integer> words;
    // TrieST<Integer> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        words = new TST<Integer>();
        // TrieST<Integer> words = new TrieST();
        for (String word : dictionary) {
            // System.out.println(word);
            int wordLength = scoreOf(word);
            words.put(word, wordLength);
        }
        // for (String word : words.keys()) {
        //     System.out.println(word);
        // }
        // System.out.println(words.size());
    }

    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boolean[][] markedArray = new boolean[board.rows()][board.cols()];
        SET<String> temp = new SET();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                StringBuffer str = new StringBuffer();
                // System.out.println(board.getLetter(row, col));
                // System.out.println(row + " " + col);
                getAllWords(row, col, board, str, temp, markedArray);
            }
        } 
        // System.out.println(temp.size());
        return temp;
    }

    private void getAllWords(int row, int col, BoggleBoard board, StringBuffer newStr, SET<String> temp, boolean[][] newMarkedArray) {
        if (row < 0 || col < 0 || row >= board.rows() || col >= board.cols()) {
            return;
        }
        // System.out.println(board.getLetter(row, col));
        // System.out.println(row + " " + col);
        if (newMarkedArray[row][col] == true){ return; }   
        // System.out.println(board.getLetter(row, col));

        if (board.getLetter(row,col) == 'Q') {
            newStr.append(board.getLetter(row, col));
            newStr.append(Character.toString('U'));
        } else newStr.append(board.getLetter(row, col));

        // System.out.println(row + " " + col + " " + board.getLetter(row, col) + "=====================" + newStr);
        // System.out.println("==================" + newStr);
        newMarkedArray[row][col] = true;

        if (newStr.length() >= 3) {
            // System.out.println(newStr.toString());
            // System.out.println("check: " + words.get(newStr.toString()));
            // if (words.contains(newStr.toString().substring(0,newStr.length() - 1) )) {
            // System.out.println(words.contains(newStr.toString()));
            if (words.contains(newStr.toString())) {
            // if (words.get(newStr.toString()) != null) {
                // System.out.println("Hi");
                // System.out.println(newStr);
                // System.out.println(temp.size());
                temp.add(newStr.toString());
            }
            // else System.out.println(newStr.toString());

            if (!(words.containsPrefix(newStr.toString()))) {
                if (board.getLetter(row, col) == 'Q') {
                    newStr.delete(newStr.length() - 2, newStr.length());
                } else {
                    newStr.delete(newStr.length() - 1, newStr.length());
                }
                newMarkedArray[row][col] = false;
                return;
            } 
        }
        
        for (int row1 = row - 1; row1 <= row + 1; row1++) {
            if (row1 >= 0 && row1 < board.rows()) {
                for (int col1 = col - 1; col1 <= col + 1; col1++) {
                    if (col1 >= 0 && col1 < board.cols()) {
                        // System.out.println("row1 " + row1 + "col1 " + col1);
                        getAllWords(row1, col1, board, newStr, temp, newMarkedArray);
                    }
                }
            }
        }

        if (board.getLetter(row, col) == 'Q') {
            newStr.delete(newStr.length() - 2, newStr.length());
        } else {
            newStr.delete(newStr.length() - 1, newStr.length());
        }
        newMarkedArray[row][col] = false;
        // return;
    }
    
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
        BoggleBoard board = new BoggleBoard(args[0]);
        In in = new In(args[1]);
        String[] wordsArray = in.readAllStrings();
        BoggleSolver boggleSolver = new BoggleSolver(wordsArray);
        System.out.println(boggleSolver.getAllValidWords(board));
        // for (String s : boggleSolver.getAllValidWords(board)) {
        //     System.out.println(s);
        // }
        // for (int i = 0; i < wordsArray.length; i++) {
        //     System.out.println(boggleSolver.scoreOf(wordsArray[i]));            
        // }
        // Queue<String> q = (Queue<String>) boggleSolver.getAllValidWords(board);
        // Set<String> st = new SET<String>();
        // st = boggleSolver.getAllValidWords(board);
        // System.out.println(q.size());
    }
}