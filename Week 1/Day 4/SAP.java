import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;

/**
 * SAP
 */
public class SAP {
    int len;
    int anc;
    Digraph dg;
    BreadthFirstDirectedPaths bfss;
    BreadthFirstDirectedPaths bfsd;
 
    public SAP(Digraph dg) {
        this.dg = dg;
        len = Integer.MAX_VALUE;
        anc = -1;
    }

    public void bfs(Digraph dg, int s, int d) {
        len = Integer.MAX_VALUE;
        anc = -1;
        // bfss = new BreadthFirstDirectedPaths(dg, s);
        // bfsd = new BreadthFirstDirectedPaths(dg, d);
    }

    public int length(int v, int w) {
        ancestor(v, w);
        if (len == Integer.MAX_VALUE) {
            return -1;
        }
        return len;
    } 

    public int ancestor(int v, int w) {
        
        if (v == w) {
            len = 0;
            return v;
        }
        
        len = Integer.MAX_VALUE;
        anc = -1;

        if ((v < 0 && v > dg.V()) || (w < 0 && w > dg.V())) return -1;

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
        if (len == Integer.MAX_VALUE) return -1;
        return len;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        len = Integer.MAX_VALUE;
        anc = -1;
        for (Integer i : v) {
            if (i < 0 && i > dg.V()) return -1; }

        for (Integer j : w) {
            if (j < 0 && j > dg.V()) return -1; }

        try {
            if (v == null || w == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {}

        for (Integer i : v) {
            for (Integer j : w) {
                try {
                    bfss = new BreadthFirstDirectedPaths(dg, v);
                    bfsd = new BreadthFirstDirectedPaths(dg, w);
                } catch (IllegalArgumentException e) { return -1; }

                for (int k = 0; k < dg.V(); k++) {
                    if (bfss.hasPathTo(i) && bfsd.hasPathTo(j)) {
                        int temp = bfss.distTo(i) + bfsd.distTo(j);
                        if ( temp < len ) {
                            anc = i;
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
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            sap.len = -1;
            sap.anc = -1;
        }
    }
}