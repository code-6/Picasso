package org.novinomad.picasso.domain.dto.permissions;

import lombok.*;

import java.io.Serializable;
import java.util.*;

/**
 * Permissions DTO that converts permission into VIS JS data to draw permissions as nodes and edges directed graph
 * */
@Getter
@Setter
@NoArgsConstructor
public class VisJsDataSet implements Serializable {

    private final Set<Node> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();

    public void addNode(String id) {
        nodes.add(new Node(id));
    }

    public void addNode(String id, String title) {
        nodes.add(new Node(id,title));
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Node from, Node to) {
        edges.add(new Edge(from.getId(), to.getId()));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private String id;
        private String label;
        private String title;

        public Node(String id) {
            this.id = id;
            label = id;
        }

        public Node(String id, String title) {
            this(id);
            this.title = id + ": " + title;
        }

        public Node(Long id, String label, String title) {
            this.id = String.valueOf(id);
            this.label = label;
            this.title = id + ": " + title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return id.equals(node.id) && Objects.equals(label, node.label) && Objects.equals(title, node.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, label, title);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Edge {
        private String from;
        private String to;

        public Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return from.equals(edge.from) && to.equals(edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}
