import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Random;

public class MOGA {
    private Random rand = new Random();
    public final int obj_count  = Constant.obj_count;
    public ArrayList<Chromosome> curr_pop;
    MOGA(){
        // initialize population
        // generating solution
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort() {
        // sort according to the domination into the fronts
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom_by = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> count_of_dom_by = new ArrayList<Integer>();
        for (int i = 0; i < curr_pop.size(); i++) {
            sol_dom_by.add(new ArrayList<Integer>());
            count_of_dom_by.add(0);
            for (int j = 0; j < curr_pop.size(); j++) {
                if (this.curr_pop.get(i).dominates(this.curr_pop.get(j)))
                    sol_dom_by.get(i).add(j);
                else if (this.curr_pop.get(j).dominates(this.curr_pop.get(i)))
                    count_of_dom_by.set(i, count_of_dom_by.get(i) + 1);
            }
            if (count_of_dom_by.get(i) == 0) {
                if (dominated_front.size() == 0)
                    dominated_front.add(new ArrayList<Integer>());
                dominated_front.get(0).add(i);
            }
        }
        int front_count = 0;
        while (dominated_front.get(front_count).size() != 0) {
            ArrayList<Integer> next_front = new ArrayList<Integer>();
            for (int i = 0; i < dominated_front.get(front_count).size(); i++) {
                for (int j = 0; sol_dom_by.get(dominated_front.get(front_count).get(i)).size() > j; j++) {
                    int cnt = count_of_dom_by.get(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j));
                    cnt--;
                    count_of_dom_by.set(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j), cnt);
                    if (cnt == 0) {
                        next_front.add(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j));
                        this.curr_pop.get(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j)).setRank(front_count + 1);
                    }
                }
            }
            front_count++;
            dominated_front.add(next_front);
        }
        return dominated_front;
    }
//    ArrayList<Integer> crowding_sort(){
//
//    }
//
//
//    ArrayList<Chromosome> next_generation(){
//
//    }

    public static void main(String[] args) {
        System.out.println("hello");
        Chromosome obj1 = new Chromosome(5);
        Chromosome obj2 = new Chromosome(5);
        Chromosome obj = obj1.crossover(obj2);
        System.out.println(obj1.data);
        System.out.println(obj2.data);
        System.out.println(obj.data);
        System.out.println(obj.objective_values);
        obj.mutate();
        obj.calculate_objective_value();
        System.out.println(obj.objective_values);
        System.out.println(obj.data);
    }
}
