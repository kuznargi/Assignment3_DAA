package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InputJSONGenerator {
    private final JSONArray graphs;
    private Random random;

    public InputJSONGenerator() {
        this.graphs = new JSONArray();
        this.random = new Random();
    }

    private JSONObject generateRandomGraph(int id, int vertexCount, double density) {
        JSONArray edges = new JSONArray();
        for (int i = 1; i < vertexCount; i++) {
            int u = random.nextInt(i);
            int v = i;
            int weight = 1500 + random.nextInt(2501); // 1500 to 4000
            edges.put(new JSONObject().put("u", u).put("v", v).put("weight", weight));
        }

        int maxPossibleEdges = vertexCount * (vertexCount - 1) / 2;
        int targetEdges = Math.min((int) (maxPossibleEdges * density), maxPossibleEdges);
        boolean[][] edgeMatrix = new boolean[vertexCount][vertexCount];
        for (int i = 0; i < edges.length(); i++) {
            JSONObject edge = edges.getJSONObject(i);
            int u = edge.getInt("u");
            int v = edge.getInt("v");
            edgeMatrix[u][v] = edgeMatrix[v][u] = true;
        }

        while (edges.length() < targetEdges) {
            int u = random.nextInt(vertexCount);
            int v = random.nextInt(vertexCount);
            if (u != v && !edgeMatrix[u][v]) {
                int weight = 1500 + random.nextInt(2501);
                edges.put(new JSONObject().put("u", u).put("v", v).put("weight", weight));
                edgeMatrix[u][v] = edgeMatrix[v][u] = true;
            }
        }

        JSONObject graph = new JSONObject();
        graph.put("id", id);
        graph.put("vertices", vertexCount);
        graph.put("edges", edges);
        return graph;
    }

    public void generateAndSave() throws IOException {
        int idCounter = 1;

         int[] smallNodes = {5, 10, 15, 20, 25, 30};
        for (int nodes : smallNodes) {
            graphs.put(generateRandomGraph(idCounter++, nodes, 0.5));
        }

        int[] mediumNodes = {50, 100, 150, 200, 250, 300, 350, 400, 450};
        for (int nodes : mediumNodes) {
            graphs.put(generateRandomGraph(idCounter++, nodes, 0.5));
        }

        int[] largeNodes = {500, 550, 600, 650, 700, 750, 800, 850, 900, 1000};
        for (int nodes : largeNodes) {
            graphs.put(generateRandomGraph(idCounter++, nodes, 0.5));
        }

        int[] extraLargeNodes = {1300, 1500, 2000};
        for (int nodes : extraLargeNodes) {
            graphs.put(generateRandomGraph(idCounter++, nodes, 0.5));
        }

        JSONObject output = new JSONObject();
        output.put("graphs", graphs);

        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        try (FileWriter file = new FileWriter("data/input.json")) {
            file.write(output.toString(4));
        }

        System.out.println("Saved " + graphs.length() + " graphs to data/input.json");
    }

    public static void main(String[] args) {
        InputJSONGenerator generator = new InputJSONGenerator();
        try {
            generator.generateAndSave();
        } catch (IOException e) {
            System.err.println("Failed: " + e.getMessage());
        }
    }
}