import java.util.ArrayList;

public class KdTree {

    private static class Node{
        public int data, left, right;
        public Node(){
            this.right = this.left = this.data = -1;
        }
    }
    private static Node[] tree= new Node[40000];
    public static int pos = 0;
    private static final int kValue = Constant.obj_count;
    public static ArrayList<Chromosome> curr_pop;
    public static void init(){
        for(int i = 0 ; i < 40000; i++)
            tree[i] = new Node();
        System.out.println("Array of Node is Initialized successfully");
    }

    public int startIndex = 0;
    public int root = -1;

    public KdTree(int chromo_index, int startIndex){
        KdTree.tree[KdTree.pos].data = chromo_index;
        this.root = KdTree.pos;
        KdTree.pos++;
        this.startIndex = startIndex;
    }
    public void add(int chromo_index){
        int index = this.startIndex;
        int root = this.root;
        while (true){
            if(KdTree.curr_pop.get(KdTree.tree[root].data).objective_values.get(index) < KdTree.curr_pop.get(chromo_index).objective_values.get(index)){
                if(KdTree.tree[root].left != -1){
                    index = (index+1) % KdTree.kValue;
                    root = KdTree.tree[root].left;
                }
                else{
                    KdTree.tree[root].left = KdTree.pos;
                    KdTree.tree[KdTree.pos++].data=chromo_index;
                    break;
                }
            }
            else{
                if(KdTree.tree[root].right != -1) {
                    index = (index+1) % KdTree.kValue;
                    root = KdTree.tree[root].right;
                }
                else {
                    KdTree.tree[root].right = KdTree.pos;
                    KdTree.tree[KdTree.pos++].data = chromo_index;
                    break;
                }
            }
        }
    }
    public boolean isAnyDominating(int chromo_index) {
        int index = this.startIndex;
        int root = this.root;
        while (root != -1) {
            if (KdTree.curr_pop.get(KdTree.tree[root].data).dominates(KdTree.curr_pop.get(chromo_index)))
                return true;
            else if (KdTree.curr_pop.get(KdTree.tree[root].data).objective_values.get(index) < KdTree.curr_pop.get(chromo_index).objective_values.get(index)){
                index = (index + 1) % KdTree.kValue;
                root = KdTree.tree[root].left;
            }
            else{
                index = (index + 1) % KdTree.kValue;
                root = KdTree.tree[root].right;
            }
        }
        return false;
    }
}
