package edu.ser222.m04_02;

/**
 * Stores contents loaded from a data file for data components and kanji into a directed
 * graph data structure, and uses a topological sort to display the kanji characters 
 * in order of most simple to least simple.
 *
 * Completion time: 11 hours
 *
 * @author Borys Banaszkiewicz, Acuna, Buckner
 * @version 02/24/2024
 */

//Note: not all of these packages may be needed.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class CompletedMain implements KanjiMain {

    //Do not add any member variables to this class.

    //TODO: implement interface methods.
    /**
     * Loads a data file for kanji, uses it to populate a hashmap that maps IDs to characters, and
     * adds the IDs as nodes in the graph. See the assignment PDF for file format.
     *
     * @param filename filename to be loaded.
     * @param graph graph to be populated.
     * @return - hashmap of the integer keys and kanji values from the selected file.
     */
    @Override
    public HashMap<Integer, String> loadKanji(String filename, EditableDiGraph graph) {
        HashMap<Integer, String> map = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("\t");
                
                if (info[0].charAt(0) == '#') continue;
                
                int heisignum = Integer.parseInt(info[0]);
                String kanji = info[1];
                
                map.put(heisignum, kanji);
                graph.addVertex(heisignum);
            }
        }
        catch (IOException e) {
            throw new java.lang.UnsupportedOperationException();
        }
        return map;
    }

    /**
     * Loads a data file for components, and uses it to add edges to the graph. See the assignment
     * PDF for file format.
     *
     * @param filename filename to be loaded.
     * @param graph graph to be populated.
     */
    @Override
    public void loadDataComponents(String filename, EditableDiGraph graph) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("\t");
                
                if (info[0].charAt(0) == '#') continue;
                
                int src = Integer.parseInt(info[0]);
                int dst = Integer.parseInt(info[1]);

                graph.addEdge(src, dst);
            }
        }
        catch (IOException e) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    /**
     * Creates a four line string containing the kanji in a valid topological ordering.
     *
     * The first line must contain "Original:", followed by a line containing the original kanji in
     * the graph, a line containing "Sorted:", and then a line containing the sorted kanji. See the
     * assignment PDF for an example. The two kanji lines must contain only the kanji characters, no
     * other formatting or spaces.
     *
     * @param graph a EditableDiGraph object.
     * @param topSort a TopologicalSort object.
     * @param kanjiMap  hashmap of the integer keys and kanji values.
     * @return - String of the printed kanji in a valid topological ordering.
     */
    @Override
    public String buildOrderString(EditableDiGraph graph, TopologicalSort topSort, HashMap<Integer, String> kanjiMap) {
        StringBuilder str = new StringBuilder();
        str.append("Original:").append("\n");
        for (Integer key : kanjiMap.keySet()) {
            str.append(kanjiMap.get(key));
        }
        str.append("\nSorted:\n");
        Iterable<Integer> sorted = topSort.order();
        for (Integer vertex : sorted) {
            str.append(kanjiMap.get(vertex));
        }

        return str.toString();
    }

    public static void main(String[] args) {
        /***************************************************************************
         * START - CORE DRIVER LOGIC, DO NOT MODIFY                                *
         **************************************************************************/
        String FILENAME_KANJI = "data-kanji.txt";
        String FILENAME_COMPONENTS = "data-components.txt";

        KanjiMain driver = new CompletedMain();
        EditableDiGraph graph = new BetterDiGraph();        

        HashMap<Integer, String> kanjiMap = driver.loadKanji(FILENAME_KANJI, graph);
        driver.loadDataComponents(FILENAME_COMPONENTS, graph);
        
        TopologicalSort intuitive = new IntuitiveTopological(graph);
        
        System.out.println(driver.buildOrderString(graph, intuitive, kanjiMap));

        /***************************************************************************
         * END - CORE DRIVER LOGIC, DO NOT MODIFY                                  *
         **************************************************************************/

        //NOTE: feel free to temporarily comment out parts of the above code while
        //you incrementally develop your program. Just make sure all of it is there
        //when you test the final version of your program.

        //OPTIONAL: add code for extra credit here.
        
        EditableDiGraph G = new BetterDiGraph();
        HashMap<Integer, String> G_kanji = driver.loadKanji(FILENAME_KANJI, G);
        driver.loadDataComponents(FILENAME_COMPONENTS, G);
        
        Iterable<Integer> adjList = G.vertices();
        StringBuilder sb = new StringBuilder();
       
        sb.append("digraph G {\n");
        for (Integer v : adjList) {
            Iterable<Integer> list = G.getAdj(v);
            for (Integer l : list) {
                sb.append("    ").append(G_kanji.get(v)).append(" -> ").append(G_kanji.get(l)).append(";\n");
            }
        }
        sb.append("}");
        
        String dotString = sb.toString();

        try {
            String dotPath = "/usr/local/bin/dot";
            String outputFilePath = "graph.png";
            ProcessBuilder pb = new ProcessBuilder(dotPath, "-Tpng", "-o", outputFilePath);
            Process p = pb.start();

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
                out.write(dotString);
            }
            catch (IOException e) {
                throw new java.lang.UnsupportedOperationException();
            }

        } catch (IOException e) {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}