import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

/**
 * SAP
 */
public class SAP {
    private int len;
    private int anc;
    private Digraph dg;
    private BreadthFirstDirectedPaths bfss;
    private BreadthFirstDirectedPaths bfsd;
 
    public SAP(Digraph dg) {
        if (dg == null) throw new IllegalArgumentException();
        this.dg = dg;
        len = Integer.MAX_VALUE;
        anc = -1;
    }

    private void bfs(Digraph dg, int s, int d) {
        len = Integer.MAX_VALUE;
        anc = -1;
    }

    public int length(int v, int w) {
        ancestor(v, w);
        if (len == Integer.MAX_VALUE) {
            return -1;
        }
        return len;
    }

    public int ancestor(int v, int w)  {
        if (v < 0 || v >= dg.V()) {
            throw new IllegalArgumentException();
        }
        if (w < 0 || w >= dg.V()) {
            throw new IllegalArgumentException();
        }

        if (v == w) {
            len = 0;
            return v;
        }
        
        len = Integer.MAX_VALUE;
        anc = -1;

        try {
            bfss = new BreadthFirstDirectedPaths(dg, v);
            bfsd = new BreadthFirstDirectedPaths(dg, w);
        }catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return -1;
        }

        for (int i = 0; i < dg.V(); i++ ) {
            if (bfss.hasPathTo(i) && bfsd.hasPathTo(i)) {
                int temp = bfss.distTo(i) + bfsd.distTo(i);
                if ( temp < len ) {
                    len = temp;
                    anc = i;
                }
            }
        }
        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        ancestor(v, w);
        
        if (len == Integer.MAX_VALUE) len = -1;
        return len;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        len = Integer.MAX_VALUE;
        anc = -1;
        
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer i : v) {
            for (Integer j : w) {
                if((i < 0 || i >= dg.V()) || (j < 0 || j >= dg.V())) throw new IllegalArgumentException();
                if(i == null || j == null){
                    throw new IllegalArgumentException();
                }
                try {
                    bfss = new BreadthFirstDirectedPaths(dg, i);
                    bfsd = new BreadthFirstDirectedPaths(dg, j);
                } catch (IllegalArgumentException e) { return -1; }

                for (int k = 0; k < dg.V(); k++) {
                    if (bfss.hasPathTo(k) && bfsd.hasPathTo(k)) {
                        int temp = bfss.distTo(k) + bfsd.distTo(k);
                        if ( temp < len ) {
                            len = temp;
                            anc = k;
                        }
                    }
                }
            }
        }return anc;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph dg1 = new Digraph(in);
        SAP sap = new SAP(dg1);
        // System.out.println(dg1.V());
        // System.out.println(dg1.E());
        ArrayList<Integer> v = new ArrayList<>();
        v.add(4);
        v.add(7);
        v.add(-1);
        v.add(12);
        // v.add(e);
        ArrayList<Integer> w = new ArrayList<>();
        // w = null;
        w.add(2);
        w.add(3);
        w.add(6);
        w.add(9);
        w.add(10);
        w.add(11);
        // while (!StdIn.isEmpty()) {
            // int v = StdIn.readInt();
            // int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            System.out.println(length + " + " + ancestor);
        // }
    }
}