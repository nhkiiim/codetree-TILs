import java.util.*;
import java.io.*;

public class Main {

    static HashSet<Integer> set;
    static HashMap<Integer, Node> trees;
    static boolean depthFlag = true;
    static int colorSum = 0;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int Q = Integer.parseInt(st.nextToken());
        trees = new HashMap<>();
        set = new HashSet<>();

        for (int i=1; i<=Q; i++) {
            st = new StringTokenizer(br.readLine());
            String comm = st.nextToken();

            if (comm.equals("100")) {
                int mid = Integer.parseInt(st.nextToken());
                int pid = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                int maxDepth = Integer.parseInt(st.nextToken());

                Node node = new Node(mid, pid, color, maxDepth, new ArrayList<>(), new HashSet<>());
                trees.put(mid, node);

                if (pid == -1) {
                    set.add(mid);
                    continue;
                }

                addNode(node);

            } else if (comm.equals("200")) {
                int mid = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());

                Node node = trees.get(mid);
                changeColor(node, color);

            } else if (comm.equals("300")) {
                int mid = Integer.parseInt(st.nextToken());
                System.out.println(trees.get(mid).color);

            } else if (comm.equals("400")) {
                colorSum = 0;

                for (int root : set) {
                    calcColor(trees.get(root));
                }
                
                System.out.println(colorSum);
            }
        }



    }

    public static void addNode(Node node) {
        depthFlag = true;

        Node parent = trees.get(node.pid);
        checkDepth(parent, 2);

        if (!depthFlag) {
            return;
        }

        parent.childrens.add(node.mid);
    }

    public static void checkDepth(Node nowNode, int depth) {
        if (!depthFlag) {
            return;
        }
       
        if (depth > nowNode.maxDepth) {
            depthFlag = false;
            return;

        } else {
            if (nowNode.pid == -1) return;
            checkDepth(trees.get(nowNode.pid), depth+1);
        }
    }

    public static void changeColor(Node node, int color) {
        node.color = color;

        if (node.childrens.size() == 0) {
            return;
        }

        List<Integer> tempChild = node.childrens;

        for (int i=0; i<tempChild.size(); i++) {
            changeColor(trees.get(tempChild.get(i)), color);
        }
    }

    public static void calcColor(Node node) {
        node.colors.add(node.color);

        if (node.childrens.size() == 0) {
            colorSum += 1;
            return;
        }

        List<Integer> tempChild = node.childrens;
    
        for (int i=0; i<tempChild.size(); i++) {
            Node child = trees.get(tempChild.get(i)); 
            calcColor(child);
            
            for (int c : child.colors) {
                node.colors.add(c);
            }

            child.colors.clear();
        }

        colorSum += (int)(Math.pow(node.colors.size(), 2));

        if (node.pid == -1) {
            node.colors.clear();
        }
    }

    public static class Node {
        int color;
        int maxDepth;
        int pid;
        int mid;
        List<Integer> childrens;
        HashSet<Integer> colors;

        public Node(int mid, int pid, int color, int maxDepth, List<Integer> childrens, HashSet<Integer> colors) {

            this.mid = mid;
            this.pid = pid;
            this.color = color;
            this.maxDepth = maxDepth;
            this.childrens = childrens;
            this.colors = colors;
        }
    }
}