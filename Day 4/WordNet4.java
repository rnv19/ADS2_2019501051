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
import java.util.*;
/**
 * WordNet3
 */
public class WordNet4 {
    
    ArrayList<String> nounsList = new ArrayList<>();
    Hashtable<String, ArrayList<Integer>> hts = new Hashtable<>();
    Hashtable<String, ArrayList<Integer>> hth = new Hashtable<>();
    String[] nouns;
    int[] id;
    SAP sapObject;
    Digraph dg;


    public WordNet4(String synsets, String hypernyms) {
        Digraph dg;
        // Digraph dg;
        String[] nouns;
        Integer[] id;
        parseSynsets(synsets);
        dg = new Digraph(hts.size());
        parseHypernyms(dg, hypernyms);
        sapObject = new SAP(dg);
    }

    public boolean isCycle() {
        DirectedCycle cycle = new DirectedCycle(dg);
        if (cycle.hasCycle()) return true;
        else return false;
    }


    private void parseSynsets(String synsets) {
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\wordnet\\"+ synsets + ".txt");
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                temp = line.split(",");
                nounsList.add(temp[1]);
                nouns = temp[1].split(" ");
                ArrayList al;
                for (String string : nouns) {
                    if ( hts.containsKey(string) ) {
                        al = hts.get(string);
                    } else {
                        al = new ArrayList<Integer>();
                        hts.put(string, al);
                    }
                    al.add(temp[0]);
                }
            }
        }
        catch (IOException e) {
            System.out.println("file not found");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
    }

    private void parseHypernyms(Digraph dg, String hypernyms) {
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\wordnet\\"+ hypernyms + ".txt");
        ArrayList<Integer> al;
        int[] id;
        String[] hypernymsTemp;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                temp = line.split(",",2);
                // System.out.println(Arrays.toString(temp));
                if (temp.length != 1) {
                    hypernymsTemp = temp[1].split(",");
                    // System.out.println(Arrays.toString(hypernyms));
                    int in = Integer.parseInt(temp[0]);
                    for (String i : hypernymsTemp) {
                        if (hth.containsKey(in)) {
                            al = hth.get(in);
                        } else {
                            al = new ArrayList<Integer>();
                            hth.put(i,al);
                        }
                        al.add(Integer.parseInt(i));
                        dg.addEdge(Integer.parseInt(temp[0]),Integer.parseInt(i));
                    }    
                }
            } 
        }catch (IOException e) {
            System.out.println("file not found");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
    }
    
    public Iterable<String> nouns() {
        return hts.keySet();
    }

    public boolean isNoun(String word) {
        if (hts.contains(word)) return true;
        else return false;
    }

    public int distance(String nounA, String nounB) {
        try {
            if (nounA.equals(nounB)) {
                return 0;
            }
        return sapObject.length(hts.get(nounA), hts.get(nounB));
        } catch (IllegalArgumentException e) {
        }
        return -1;
    }

    public String sap(String nounA, String nounB) {
        try {
            if (nounA.equals(nounB)) {
                return nounA;
            }
        int anc = sapObject.ancestor(hts.get(nounA),hts.get(nounB));
        if (anc == -1) return "";
        return nounsList.get(anc);
        }catch (IllegalArgumentException e) {}
        return "";
    }


    public static void main(String[] args) {
        WordNet4 wn = new WordNet4(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length   = wn.distance(v, w);
            String ancestor = wn.sap(v, w);
            StdOut.printf("length = %s, ancestor = %s\n", length, ancestor);
        }
}