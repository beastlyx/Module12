package edu.ser222.m04_02;

import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * Implements an editable graph with sparse vertex support.
 * 
 * @author Borys Banaszkiewicz, Acuna
 * @version 2/24/24
 */
public class BetterDiGraph implements EditableDiGraph {
    
    private int E;
    private int vertex_count;
    private HashMap<Integer, LinkedList<Integer>> adj;
    
    public BetterDiGraph() {
        E = 0;
        vertex_count = 0;
        adj = new HashMap<>();
    }
    /**
     * Adds an edge between two vertices, v and w. If vertices do not exist,
     * adds them first. Does not allow duplicate (parallel) edges to be created.
     *
     * @param v source vertex
     * @param w destination vertex
     */
    @Override
    public void addEdge(int v, int w) {
        if (adj.get(v) != null && adj.get(v).contains(w)) {
            return;
        }
        
        if (!adj.containsKey(v)) {
            addVertex(v);
        }
        if (!adj.containsKey(w)) {
            addVertex(w);
        }
        adj.get(v).add(w);
        E++;
    }

    /**
     * Adds a vertex to the graph. Does not allow duplicate vertices.
     *
     * @param v vertex number
     */
    @Override
    public void addVertex(int v) {
        if (adj.containsKey(v)) {
            return;
        }
        
        adj.put(v, new LinkedList<>());
        vertex_count++;
    }

    /**
     * Returns the direct successors of a vertex v.
     *
     * @param v vertex
     * @return successors of v
     */
    @Override
    public Iterable<Integer> getAdj(int v) {
        if (adj.get(v) == null) return new LinkedList<>();
        return adj.get(v);
    }
    
    /**
     * Number of edges.
     *
     * @return edge count
     */
    @Override
    public int getEdgeCount() {
        return E;
    }
    
    /**
     * Returns the in-degree of a vertex.
     * @param v vertex
     * @return in-degree.
     * @throws NoSuchElementException exception thrown if vertex does not exist.
     */
    @Override
    public int getIndegree(int v) throws NoSuchElementException {
        if (adj.get(v) == null) throw new NoSuchElementException();
        
        int count = 0;

        for (HashMap.Entry<Integer, LinkedList<Integer>> list : adj.entrySet()) {
            if (list.getValue() != null && list.getValue().contains(v) && list.getKey() != v) count++;
        }
        
        return count;
    }
    
    /**
     * Returns number of vertices.
     * @return vertex count
     */
    @Override
    public int getVertexCount() {
        return vertex_count;
    }
    
    /**
     * Removes edge from graph. If vertices do not exist, does not remove edge.
     *
     * @param v source vertex
     * @param w destination vertex
     */
    @Override
    public void removeEdge(int v, int w) {
        if (!adj.containsKey(v)) {
            return;
        }
        
        adj.get(v).remove(Integer.valueOf(w));
        E--;
    }

    /**
     * Removes vertex from graph. If vertex does not exist, does not try to
     * remove it.
     *
     * @param v vertex
     */
    @Override
    public void removeVertex(int v) {
        if (adj.get(v) == null) {
            return;
        }
        for (HashMap.Entry<Integer, LinkedList<Integer>> list : adj.entrySet()) {
            if (list.getValue() != null && !list.getValue().isEmpty() && list.getValue().contains(v)) {
                removeEdge(list.getKey(), v);
            }
        }
        adj.remove(v);
        vertex_count--;
    }

    /**
     * Returns iterable object containing all vertices in graph.
     *
     * @return iterable object of vertices
     */
    @Override
    public Iterable<Integer> vertices() {
        LinkedList<Integer> iter = new LinkedList<>();
        
        for (HashMap.Entry<Integer, LinkedList<Integer>> list : adj.entrySet()) {
            iter.add(list.getKey());
        }
        
        return iter;
    }

    /**
     * Returns false if the graph contains at least one vertex.
     *
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        return vertex_count == 0;
    }
    
    /**
     * Returns true if the graph contains a specific vertex.
     *
     * @param v vertex
     * @return boolean
     */
    @Override
    public boolean containsVertex(int v) {
        return adj.containsKey(v);
    }
}
