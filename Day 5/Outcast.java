import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
/**
 * Outcast
 */
public class Outcast {
    final private WordNet wordnetObject;
    public Outcast(WordNet wordnet) {            // constructor takes a WordNet object
        wordnetObject = wordnet;
    }         
    public String outcast(String[] nouns) {   // given an array of WordNet nouns, return an outcast
        String outcastNoun = "";
        int tempPathLength = 0;
        int distance = 0;
        for (String string : nouns) {
            for (String string2 : nouns) {
                distance += wordnetObject.distance(string, string2);
            }
            if (tempPathLength < distance) {
                tempPathLength = distance;
                outcastNoun = string;
            }
            distance = 0;
        }
        return outcastNoun;
    }
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            // StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
 }