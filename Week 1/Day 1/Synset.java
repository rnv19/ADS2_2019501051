import java.util.ArrayList;


public class Synset {
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> nouns = new ArrayList<String>();
    ArrayList<String> gloss = new ArrayList<String>();
    
    
    public Synset(Integer ip1, String[] ip2, String ip3) {
        this.id = ip1;
        this.nouns = ip2;
        this.gloss = ip3;
    }
}