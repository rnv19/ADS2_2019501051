import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

/**
 * SAP
 */
public class SAP {
    int len = Integer.MAX_VALUE;
    int anc = -1;
    Digraph dg;
    BreadthFirstDirectedPaths bfss;
    BreadthFirstDirectedPaths bfsd;
 
    public SAP(Digraph dg) {
        this.dg = dg;
    }

    public void bfs(Digraph dg, int s, int d) {
        len = Integer.MAX_VALUE;
        anc = -1;
        bfss = new BreadthFirstDirectedPaths(dg, s);
        bfsd = new BreadthFirstDirectedPaths(dg, d);
    }

    public int length(int v, int w) {
        for (int i = 0; i < dg.V(); i++ ) {
            if (bfss.hasPathTo(i) && bfsd.hasPathTo(i)) {
                int temp = bfss.distTo(i) + bfsd.distTo(i);
                if ( temp < len ) {
                    len = temp;
                }
            }
        }
        if (len == Integer.MAX_VALUE) len = -1;
        return len;
    }

    public int ancestor(int v, int w) {
        len = Integer.MAX_VALUE;
        for (int i = 0; i < dg.V(); i++ ) {
            if (bfss.hasPathTo(i) && bfsd.hasPathTo(i)) {
                int temp = bfss.distTo(i) + bfsd.distTo(i);
                if ( temp < len ) {
                    anc = i;
                }
            }
        }
        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        len = Integer.MAX_VALUE;
        for (int i : v) {
            for (int j : w) {
                if (bfss.hasPathTo(i) && bfsd.hasPathTo(j)) {
                    int temp = bfss.distTo(i) + bfsd.distTo(j);
                    if (temp < len) len = temp;
                }
            }
        }
        if ( len == Integer.MAX_VALUE) len = -1;
        return len;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        len = Integer.MAX_VALUE;
        for (Integer i : v) {
            for (Integer j : w) {
                if (bfss.hasPathTo(i) && bfsd.hasPathTo(j)) {
                    int temp = bfss.distTo(i) + bfsd.distTo(j);
                    if ( temp < len ) {
                        anc = i;
                    }
                }
            }
        }return anc;
    }
}