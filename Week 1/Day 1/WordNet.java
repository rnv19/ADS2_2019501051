import edu.princeton.cs.algs4.StdOut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import java.util.*;

/**
 * WordNet
 */
public class WordNet {
    Hashtable<String, ArrayList<Integer>> hts = new Hashtable<>();
    String[] nouns;
    Integer[] id;
    
    private void parseSynsets(String filename) {
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\wordnet\\"+ filename + ".txt");
        try (Scanner sc = new Scanner(file)) {
            int size = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                temp = line.split(",");
                nouns = temp[1].split(" ");
                ArrayList al;
                for (String string : nouns) {
                    if ( hts.containsKey(string) ) {
                        al = hts.get(string);
                    } else {
                        al = new ArrayList<>();
                        hts.put(string, al);
                    }
                    al.add(temp[0]);
                }
                size++;
            }System.out.println(size);
        }
        catch (IOException e) {
            System.out.println("file not found");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
    }

    private void parseHypernyms(String filename) {

        Hashtable<Integer, ArrayList<Integer>> hth = new Hashtable<>();
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\Day 1\\wordnet\\"+ filename + ".txt");
        ArrayList<Integer> al;
        int[] id;
        String[] hypernyms;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                temp = line.split(",",2);
                // System.out.println(Arrays.toString(temp));
                if (temp.length > 1) {
                    hypernyms = temp[1].split(",");
                    // System.out.println(Arrays.toString(hypernyms));
                    int in = Integer.parseInt(temp[0]);
                    for (String i : hypernyms) {
                        if (hth.containsKey(in)) {
                            al = hth.get(in);
                        } else {
                            al = new ArrayList<>();
                            hth.put(in,al);
                        }
                        al.add(Integer.parseInt(i));
                        
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
    
    

    public static void main(String[] args) {
        Digraph dg;
        WordNet wn = new WordNet();
        wn.parseSynsets(args[0]);
        dg = new Digraph(wn.hts.size());
        System.out.println(dg.V());
        // System.out.println(dg.E());
        // wn.parseHypernyms(args[0]);
    }
}