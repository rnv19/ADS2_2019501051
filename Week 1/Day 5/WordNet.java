import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.lang.IllegalArgumentException;
/**
 * WordNet3
 */
public class WordNet {
     
    private ArrayList<String> nounsList = new ArrayList<>();
    private Hashtable<String, ArrayList<Integer>> hts = new Hashtable<>();
    private Hashtable<Integer, ArrayList<Integer>> hth = new Hashtable<>();
    private String[] nouns;
    private int[] id;
    private SAP sapObject;
    private Digraph dg;


    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        String[] nouns;
        Integer[] id;
        parseSynsets(synsets);
        dg = new Digraph(hts.size());
        parseHypernyms(hypernyms);
        System.out.println(dg.E());
        sapObject = new SAP(dg);
    }

    private boolean isCycle() {
        DirectedCycle cycle = new DirectedCycle(dg);
        if (cycle.hasCycle()) return true;
        else return false;
    }


    private void parseSynsets(String synsets) {
        try {
            In in = new In(synsets);
            while (in.hasNextLine()) {
                String line = in.readLine();
                String[] temp;
                temp = line.split(",");
                nounsList.add(temp[1]);
                nouns = temp[1].split(" ");
                // ArrayList<Integer> al;
                for (String string : nouns) {
                    if ( hts.containsKey(string) ) {
                        ArrayList<Integer> synal = hts.get(string);
                        synal.add(Integer.parseInt(temp[0]));
                    } else {
                        ArrayList<Integer> synal = new ArrayList<Integer>();
                        synal.add(Integer.parseInt(temp[0]));
                        hts.put(string, synal);
                    }
                }
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void parseHypernyms(String hypernyms) {
        try {
            In in = new In(hypernyms);
            String line;
            String[] temp;
            ArrayList<Integer> temp1;
            while (in.hasNextLine()) {
                line = in.readLine();
                temp = line.split(",");
                temp1 = new ArrayList<>();
                for (String i : temp) {
                    temp1.add(Integer.parseInt(i));
                }
                while (true) {
                    if (temp1.size() > 1) {
                        if (hth.containsKey(temp1.get(0))) {
                            ArrayList<Integer> al = new ArrayList<>();
                            al = hth.get(temp1.get(0));
                            for (int i = 1; i < temp1.size(); i++) {
                                al.add(temp1.get(i));
                                dg.addEdge(temp1.get(0), temp1.get(i));
                            }
                        }else {
                            ArrayList<Integer> al = new ArrayList<>();
                            for (int i = 1; i < temp1.size(); i++) {
                                al.add(temp1.get(i));
                                dg.addEdge(temp1.get(0), temp1.get(i));
                            } hth.put(temp1.get(0), al);
                        }
                    } else {
                        ArrayList<Integer> al = new ArrayList<>();
                        hth.put(temp1.get(0), al);
                    }
                    break;
                }
                temp1.clear();

            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    


    public Iterable<String> nouns() {
        return hts.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (hts.containsKey(word)) return true;
        else return false;
    }

    public int distance(String nounA, String nounB) {
        
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        
        if (nounA.equals(nounB)) return 0;

        try {
            return sapObject.length(hts.get(nounA), hts.get(nounB));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }return -1;
    }

    public String sap(String nounA, String nounB) {

        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        if (nounA.equals(nounB)) return nounA;
        try {
            int anc = sapObject.ancestor(hts.get(nounA),hts.get(nounB));
            if (anc == -1) return "";
            return nounsList.get(anc);
        }catch (IllegalArgumentException e) {}
        return "";
    }


    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        // while (!StdIn.isEmpty()) {
        //     String v = StdIn.readString();
        //     String w = StdIn.readString();
        //     System.out.println(v + w);
        //     int length   = wn.distance(v, w);
        //     String ancestor = wn.sap(v, w);
        //     StdOut.printf("length = %s, ancestor = %s\n", length, ancestor);
        // }
    }
}