import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Experiment with a general tree.
 */
public class GeneralTree {


    /**
     * Define a basic general tree node.
     */
    static class GTNode {
        public int          val;
        public List<GTNode> children;

        public GTNode(int val) { this.val = val; this.children = new ArrayList<GTNode>(); }

        public GTNode(int val, List<GTNode> children) { this.val = val; this.children = children; }

        @Override
        public String toString() { 
            StringBuilder sb = new StringBuilder(this.val + " [");
            for (int i = 0; i < this.children.size(); i++) {
                sb.append(this.children.get(i).val);
                if (i + 1 < this.children.size()) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
    }


    /**
     * Populate the general tree.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     */
    static public GTNode populate(List<String> strLst) {

        // **** sanity checks ****
        if (strLst == null || strLst.size() == 0) return null;

        // **** initialization *****
        GTNode root         = null;
        List<GTNode> prev   = new ArrayList<>();

        // **** loop processing one tree level at a time (bottom-top) ****
        for (int l = 0; l < strLst.size(); l++) {

            // **** get current line to parse ****
            String line = strLst.get(l);

            // ???? ????
            System.out.println("<<< l: " + l + " line ==>" + line + "<==");

            // **** parse this line ****
            List<GTNode> curr = parseLine(line);

            // **** populate the root node ****
            if (root == null) {

                // **** populate current level removing node from curr ****
                root = curr.remove(0);

                // **** update prev for next loop ****
                prev.add(root);
            }

            // **** populate this tree level ****
            else {

                // **** reset offset ****
                int offset = 0;

                // **** for each node in prev nodes ***
                for (int p = 0; p < prev.size(); p++) {

                    // **** for ease of use ****
                    GTNode dst = prev.get(p);

                    // **** replace each children ****
                    List<GTNode> dstChildren = dst.children;

                    // **** replace each children node with nodes in curr ****
                    for (int c = 0; c < dstChildren.size(); c++) {

                        // **** for ease of use ****
                        GTNode src = curr.get(c + offset);

                        // **** replace dst with src node ****
                        dstChildren.remove(c);
                        dstChildren.add(c, src);
                    }

                    // **** update offset in curr ****
                    offset += dstChildren.size();
                }

                // **** prev becomes curr ****
                prev = curr;
            }
        }

        // **** return the root of the general tree ****
        return root;
    }


    /**
     * Parse this level of the general tree.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     */
    static public List<GTNode> parseLine(String line) {

        // **** sanity checks ****
        if (line == null || line.length() == 0) return null;

        // **** initialization ****
        List<GTNode> nodeList   = new ArrayList<>();
        List<GTNode> children   = null;
        int j                   = 0;

        // **** traverse the line parsing nodes ****
        for (int i = 0; i < line.length(); ) {

            // **** extract node value ****
            j = line.indexOf(',', i);

            int nodeVal = Integer.parseInt(line.substring(i, j));

            // **** look for `[` ****
            i = line.indexOf('[', i);

            // **** look for `]` ****
            j = line.indexOf("]", i);

            // **** extract string with the node list ****
            String nodeChildren = line.substring(i + 1, j);

            // **** update i for the next pass ****
            i = j + 2;

            // **** generate list of children ****
            if (!nodeChildren.isBlank()) {

                // **** list of child node values ****
                List<Integer> vals = Stream.of(nodeChildren.split(","))
                                        .map(String::trim)
                                        .map(Integer::parseInt)
                                        .collect(Collectors.toList());

                // **** build list of children ****
                children = new ArrayList<>();
                for (int v : vals)
                    children.add(new GTNode(v));
            } else 
                children = new ArrayList<>();

            // **** create and populate node ****
            GTNode node = new GTNode(nodeVal, children);

            // **** append node to list ****
            nodeList.add(node);
        }

        // **** return list of nodes associated with the specified line ****
        return nodeList;
    }


    /**
     * Level order tree traversal.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     */
    static public void levelOrder(GTNode root) {

        // **** ****
        if (root == null) return;
 
        // **** create queue ****
        Queue<GTNode> q = new LinkedList<>();

        // **** prime queue ****
        q.add(root);

        // **** process all the nodes in the tree ****
        while (!q.isEmpty()) {

            // **** number of children ****
            int n = q.size();
    
            // **** loop once per child ****
            while (n > 0) {

                // **** remove next item from the queue ****
                // GTNode p = q.peek();
                // q.remove();
                GTNode p = q.remove();

                // **** print this node ****
                System.out.print(p.val + " ");
    
                // **** enqueue all children of p ****
                for (int i = 0; i < p.children.size(); i++)
                    q.add(p.children.get(i));

                // **** decrement node count ****
                n--;
            }
            
            // **** new line between levels ****
            System.out.println();
        }
    }


    /**
     * In-order tree traversal.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     */
    static public void inOrder(GTNode root) {

        // **** end condition ****
        if (root == null) return;

        // **** visit node ****
        System.out.print(root.val + " ");

        // **** recurse ****
        for (GTNode c : root.children)
            inOrder(c);
    }


    /**
     * Post-order tree traversal.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     */
    static public void postOrder(GTNode root) {

        // **** end condition ****
        if (root == null) return;

        // **** recurse ****
        for (int i = root.children.size() - 1; i >= 0; i--)
            postOrder(root.children.get(i));

        // **** visit node ****
        System.out.print(root.val + " ");
    }


    /**
     * Return string with leaf nodes whose paths from the root add up to sum.
     * 
     * Recursion entry point.
     * 
     * Execution: O(n) - Space: O(n)
     */
    static public String sumNodes(GTNode root, int target) {

        // **** sanity check(s) ****
        if (root == null) return "";
        if (root.children.size() == 0) {
            if (root.val == target) return root.toString();
            return "";
        }

        // **** initialization ****
        Stack<GTNode> stack = new Stack<>();
        StringBuilder sb    = new StringBuilder();
        int[] sum           = new int[] {0};

        // **** start recursion ****
        sumNodes(root, target, stack, sum);

        // **** build string from stack contents ****
        while (!stack.isEmpty()) {
            sb.append(stack.pop().toString());
            if (!stack.isEmpty()) sb.append(", ");
        }

        // **** return string with leaf nodes whose path adds to target ****
        return sb.toString();
    }


    /**
     * Return string with leaf nodes whose paths from the root add up to sum.
     * 
     * Recursive call.
     * 
     * Execution: O(n) - Space: O(1)
     */
    static public void sumNodes(GTNode root, int target, Stack<GTNode> stack, int[] sum) {

        // **** end condition ****
        // if (root == null) return;

        // **** increment sum ****
        sum[0] += root.val;

        // **** recurse ****
        for (GTNode c : root.children)
            sumNodes(c, target, stack, sum);
        
        // **** check if leaf node and the sum matches the target value ****
        if (root.children.size() == 0 && sum[0] == target)
            stack.push(root);

        // **** decrement sum ****
        sum[0] -= root.val;
    }


    /**
     * Test scaffold.
     * 
     * !!! NOT PART OF THE SOLUTION !!!
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // **** open a buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read sum ****
        int sum = Integer.parseInt(br.readLine().trim());

        // **** read the number of input lines ****
        int lines = Integer.parseInt(br.readLine().trim());

        // **** read the data to build a general tree ****
        List<String> strLst = new ArrayList<String>();
        for (int i = 0; i < lines; i++)
            strLst.add(br.readLine().trim());

        // **** close buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< sum: " + sum);
        System.out.println("main <<< lines: " + lines);
        for (int i = 0; i < strLst.size(); i++)
            System.out.println("main <<< strLst(" + i + ") ==>" + strLst.get(i) + "<==");

        // **** populate the tree ****
        GTNode root = populate(strLst);

        // **** display the tree (level order traversal) ****
        System.out.println("main <<< levelOrder root: ");
        levelOrder(root);

        // **** in-order tree traversal ****
        System.out.print("main <<< inOrder: ");
        inOrder(root);
        System.out.println("");

        // // **** post-order tree traversal ****
        // System.out.print("main <<< postOrder: ");
        // postOrder(root);
        // System.out.println("");

        // **** call function of interest and display result ****
        System.out.println("main <<< leaf nodes: " + sumNodes(root, sum));
    }
}