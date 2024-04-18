package edu.ser222.m04_02;

import java.util.LinkedList;
import java.util.HashMap;


/**
 * Implements TopologicalSort interface, providing a topological sort of some graph.
 *
 * @author Borys Banaszkiewicz, Sedgewick and Wayne, Acuna
 * @version 2/24/24
 */
public class IntuitiveTopological implements TopologicalSort {
    
    private final BetterDiGraph G;
    boolean cycle;
    
    public IntuitiveTopological(EditableDiGraph G) {
        this.G = (BetterDiGraph)G;
        this.cycle = false;
    }

    /**
     * Returns an iterable object containing a topological sort. Returns null if the graph is not a
     * DAG.
     * @return a topological sort.
     */
    @Override
    public Iterable<Integer> order() {
        if (!isDAG()) return null;
        
        Iterable<Integer> vertices = G.vertices();
        LinkedList<Integer> topo_order = new LinkedList<>();
        
        while (!G.isEmpty()) {
            for (Integer vertex : vertices) {
                try {
                    if (G.getIndegree(vertex) == 0) {
                        topo_order.add(vertex);
                        G.removeVertex(vertex);
                    }
                } catch (RuntimeException r) {
                    continue;
                }
            }
        }
        
        return topo_order;
    }
    
    /**
     * Returns true if the graph being sorted is a DAG, false otherwise.
     * @return is graph a DAG
     */
    @Override
    public boolean isDAG() {
        int vertices = G.getVertexCount();
        HashMap<Integer, Boolean> marked = new HashMap<>();
        HashMap<Integer, Integer> edgeTo = new HashMap<>();
        HashMap<Integer, Boolean> onStack = new HashMap<>();
        
        for (int v = 0; v < vertices; v++) {
            if (!marked.containsKey(v) && dfs(G, v, marked, edgeTo, onStack)) return false;
        }
        return !cycle;
    }
    
    private boolean dfs(BetterDiGraph G, int v, HashMap<Integer, Boolean> marked, HashMap<Integer, Integer> edgeTo, HashMap<Integer, Boolean> onStack) {
        onStack.put(v, true);
        marked.put(v, true);
        for (Integer w : G.getAdj(v)) {
            if (cycle) {
                return true;
            }
            else if (!marked.containsKey(w) || marked.get(w) == false) {
                edgeTo.put(w, v);
                if (dfs(G, w, marked, edgeTo, onStack)) return true;
            }
            else if (onStack.containsKey(w) && onStack.get(w) == true) {
                cycle = true;
                return cycle;
            }
        }
        onStack.put(v, false);
        return false;
    }
}
