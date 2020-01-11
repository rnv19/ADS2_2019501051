import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
/**
 * Solution
 */
public class Solution {
    Hashtable<Integer, String> hte;
    Hashtable<Integer, ArrayList<Integer>> htel;
    Digraph dg;
    TreeMap<Integer, Integer> temp;

    public void parseEmails (String filename) {
        hte = new Hashtable<>();
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\ADS - 2 Exam - 1\\"+ filename + ".txt");
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                String em;
                temp = line.split(";");
                if (hte.containsKey(temp[0])) {
                    em = hte.get(temp[0]);
                }else {
                    em = "";
                    em = em + temp[1];
                    hte.put(Integer.parseInt(temp[0]), em);
                }
            }
        }catch (IOException e) {
            System.out.println("file not found");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
    } 

    public void parseEmailLogs(Digraph dg, String filename) {
        htel = new Hashtable<>();
        temp = new TreeMap<>();
        File file = new File("D:\\MSIT\\ADS - 2\\ADS2_2019501051\\ADS - 2 Exam - 1\\"+ filename + ".txt");
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] temp;
                temp = line.split(",");
                int i = Integer.parseInt(temp[0].split(" ")[1]);
                int j = Integer.parseInt(temp[1].split(" ")[2]);
                ArrayList<Integer> list;
                if (htel.containsKey(j)) {
                    list = htel.get(j);
                }else {
                    list = new ArrayList<Integer>();
                    htel.put(j, list);
                }
                list.add(i);
                dg.addEdge(i,j);
            }
            
        }catch (IOException e) {
            System.out.println("file not found");
        }

        catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }
        Set<Integer> s = htel.keySet();
        int count = 0;
        for (int i : s) {
            int tem = htel.get(i).size();
            // System.out.println(tem);
            temp.put(tem, i);
            count++;
        }
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        s.parseEmails(args[0]);
        Digraph dg = new Digraph(s.hte.size());
        s.parseEmailLogs(dg, args[1]);
        System.out.println("Enter number of output people");
        int c = StdIn.readInt();
        Set<Integer> setTemp = s.temp.keySet();
        int countt = 0;
        while (countt != c) {
            // System.out.println(s.temp.lastKey());
            // System.out.println(s.hte.get(s.temp.lastKey()));
            System.out.println(s.hte.get(s.temp.get(s.temp.lastKey())) + "," + s.temp.lastKey());
            s.temp.remove(s.temp.lastKey());
            countt++;
        }
    }
}