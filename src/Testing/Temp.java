package Testing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Temp {
    private Random rand;
    public final int obj_count  = Constant.obj_count;
    public final int population_size = Constant.population_size;
    public final int chromosome_size = Constant.chromosome_size;
    public ArrayList<Chromosome> curr_pop;
    public ArrayList<Chromosome> next_pop;
    public int repeated_gen_count = 0;

    Temp(){
        curr_pop = new ArrayList<Chromosome>(2* this.population_size + 1);
        rand  = new Random();
        // initializing population
        for(int i=0;i< this.population_size; i++){
            Chromosome new_sol = new Chromosome(chromosome_size);
            new_sol.calculate_objective_value();
            if(this.curr_pop.contains(new_sol)){
                i--;
                continue;
            }
            this.curr_pop.add(new_sol);
        }
    }
    
    ArrayList<ArrayList<Integer> > non_dominating_sort() {
        // sort according to the domination into the fronts
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> dom_by_count = new ArrayList<Integer>();

        int size = curr_pop.size();
        for (int i = 0; i < size; i++) {
            sol_dom.add(new ArrayList<Integer>());
            dom_by_count.add(0);
            for (int j = 0; j < size; j++) {
                if (this.curr_pop.get(i).dominates(this.curr_pop.get(j)))
                    sol_dom.get(i).add(j);
                else if (this.curr_pop.get(j).dominates(this.curr_pop.get(i)))
                    dom_by_count.set(i, dom_by_count.get(i) + 1);
            }
            if (dom_by_count.get(i) == 0) {
                if (dominated_front.size() == 0)
                    dominated_front.add(new ArrayList<Integer>());
                dominated_front.get(0).add(i);
            }
        }
        int front_count = 0;
        while (dominated_front.get(front_count).size() != 0) {
            ArrayList<Integer> next_front = new ArrayList<Integer>();
            for (int i = 0; i < dominated_front.get(front_count).size(); i++) {
                for (int j = 0; sol_dom.get(dominated_front.get(front_count).get(i)).size() > j; j++) {
                    int cnt = dom_by_count.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                    cnt--;
                    dom_by_count.set(sol_dom.get(dominated_front.get(front_count).get(i)).get(j), cnt);
                    if (cnt == 0) {
                        next_front.add(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                        this.curr_pop.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j)).setRank(front_count + 1);
                    }
                }
            }
            front_count++;
            dominated_front.add(next_front);
        }
        return dominated_front;
    }

    ArrayList<ArrayList<Integer> > non_dominating_sort() {
        // sort according to the domination into the fronts
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> dom_by_count = new ArrayList<Integer>();

        int size = curr_pop.size();
        for (int i = 0; i < size; i++) {
            sol_dom.add(new ArrayList<Integer>());
            dom_by_count.add(0);
            for (int j = 0; j < size; j++) {
                if (this.curr_pop.get(i).dominates(this.curr_pop.get(j)))
                    sol_dom.get(i).add(j);
                else if (this.curr_pop.get(j).dominates(this.curr_pop.get(i)))
                    dom_by_count.set(i, dom_by_count.get(i) + 1);
            }
            if (dom_by_count.get(i) == 0) {
                if (dominated_front.size() == 0)
                    dominated_front.add(new ArrayList<Integer>());
                dominated_front.get(0).add(i);
            }
        }
        int front_count = 0;
        while (dominated_front.get(front_count).size() != 0) {
            ArrayList<Integer> next_front = new ArrayList<Integer>();
            for (int i = 0; i < dominated_front.get(front_count).size(); i++) {
                for (int j = 0; sol_dom.get(dominated_front.get(front_count).get(i)).size() > j; j++) {
                    int cnt = dom_by_count.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                    cnt--;
                    dom_by_count.set(sol_dom.get(dominated_front.get(front_count).get(i)).get(j), cnt);
                    if (cnt == 0) {
                        next_front.add(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                        this.curr_pop.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j)).setRank(front_count + 1);
                    }
                }
            }
            front_count++;
            dominated_front.add(next_front);
        }
        return dominated_front;
    }

    public boolean does_someone_dominates(ArrayList<Integer> arr, Integer x){
        for(int i=0;i<arr.size();i++)
            if(this.curr_pop.get(arr.get(i)).dominates(this.curr_pop.get(x)))
                return true;
        return false;
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort_v2(){
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> pop_arr = new ArrayList<>();
        for(int i = this.curr_pop.size() - 1; i >= 0; i--)
            pop_arr.add(i);
        pop_arr.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                Double a=0.0,b=0.0;
                for(int i=0;i<obj_count;i++){
                    if(!curr_pop.get(integer).objective_values.get(i).equals(curr_pop.get(t1).objective_values.get(i))){
                        a = curr_pop.get(integer).objective_values.get(i);
                        b = curr_pop.get(t1).objective_values.get(i);
                        break;
                    }
                }
                if(a < b) return -1;
                else if(a > b)    return 1;
                return 0;
            }
        });

        dominated_front.add(new ArrayList<>());
        dominated_front.get(0).add(pop_arr.get(0));
        int size = pop_arr.size();
        for(int j = 1; j < size ; j++){
            int front = 0;
            while(front < dominated_front.size() && this.does_someone_dominates(dominated_front.get(front),pop_arr.get(j)))
                front++;
            if(front == dominated_front.size())
                dominated_front.add(new ArrayList<>());
            dominated_front.get(front).add(pop_arr.get(j));
        }
        return dominated_front;
    }

    ArrayList<ArrayList<Integer> > non_dominating_sort_v3() {
        ArrayList<ArrayList<Integer> > dominated_front = new ArrayList<>();
        ArrayList<Integer> pop_arr = new ArrayList<>();
        for(int i = this.curr_pop.size() - 1; i >= 0; i--)
            pop_arr.add(i);
        pop_arr.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                Double a=0.0,b=0.0;
                for(int i=0;i<obj_count;i++){
                    if(!curr_pop.get(integer).objective_values.get(i).equals(curr_pop.get(t1).objective_values.get(i))){
                        a = curr_pop.get(integer).objective_values.get(i);
                        b = curr_pop.get(t1).objective_values.get(i);
                        break;
                    }
                }
                if(a < b) return -1;
                else if(a > b)    return 1;
                return 0;
            }
        });

        // now after this sort no higher index chromosome will be
        // able to dominate any lower index chromosome

        // Now we have to generate the Dominating Front
        dominated_front.add(new ArrayList<>());
        dominated_front.get(0).add(pop_arr.get(0));

        // initializing the Kd-tree array which will of size = sizeof(dominated_front) * obj_count
        ArrayList<ArrayList<KdTree>> treeList = new ArrayList<>();
        treeList.add(new ArrayList<>());
        // inserting first index in kd-tree with each objective as first to compare in kd-tree
        for(int i = 0;i < this.obj_count ; i++)
            treeList.get(0).add(new KdTree(pop_arr.get(0), i));

        // generating other dominating front
        int pop_size = pop_arr.size();
        int front;
        for(int j = 1; j < pop_size ; j++){
            front = 0;
            while (front < dominated_front.size() && this.someone_domiates_it(treeList.get(front) , pop_arr.get(j)))
                front++;
//            while (front < dominated_front.size() && this.does_someone_dominates(dominated_front.get(front), pop_arr.get(j)))
//                front++;
            if(front == dominated_front.size() ){
                dominated_front.add(new ArrayList<>());
                treeList.add(new ArrayList<>());
                for(int i = 0;i < this.obj_count ; i++)
                    treeList.get(front).add(new KdTree(pop_arr.get(j), i));
            }
            else{
                for(int i = 0;i < this.obj_count ; i++)
                    treeList.get(front).get(i).add(pop_arr.get(j));
            }
            dominated_front.get(front).add(pop_arr.get(j));
        }

        return dominated_front;
    }
    private boolean someone_domiates_it(ArrayList<KdTree> arr, int chromosome){
        for( int i = 0;i < this.obj_count; i++)
            if(arr.get(i).isAnyDominating(chromosome))
                return true;
        return false;
    }

    public static void main(String[] args){
        System.out.println("hello");
        Temp obj = new Temp();

        // adding new generation
        for(int i=0;i<obj.population_size;i++) {
            int index1 = obj.rand.nextInt(obj.population_size);
            int index2 = obj.rand.nextInt(obj.population_size);
            while (index2 == index1)
                index2 = obj.rand.nextInt(obj.population_size);
            Chromosome child = obj.curr_pop.get(index1).crossover(obj.curr_pop.get(index2));
            if (obj.curr_pop.contains(child)) {
                //System.out.println("duplidate child produced: \n"+child.data);
                continue;
            }
            child.mutate();
            child.calculate_objective_value();
            // the below condition improves output by reducing the range of solution
            if (!(child.dominates(obj.curr_pop.get(index1)) || child.dominates(obj.curr_pop.get(index2)))) {
                //System.out.println("Child does not dominates there parents");
                continue;
            }
            obj.curr_pop.add(child);
        }

//        for(int i=0;i<obj.curr_pop.size();i++)
//            obj.curr_pop.get(i).display();

        // Initializing KD- Tree memory
        KdTree.init();
        KdTree.curr_pop = obj.curr_pop;
        System.out.println("Kd tree pos : "+ KdTree.pos);
        System.out.println("Population Size is : " + obj.curr_pop.size());
        long startTime, endTime;


        // non-dominating-sort-v1 original code

        startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_fronto = obj.non_dominating_sort();
        endTime = System.nanoTime();
        long timeTakenByV1 = endTime - startTime;


        // non-dominating-sort-v2 simply optimized

        startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_frontv = obj.non_dominating_sort_v2();
        endTime = System.nanoTime();
        long timeTakenByV2 = endTime - startTime;


        // call non-dominating_sort v3 using KD TREE

        startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_fronts = obj.non_dominating_sort_v3();
        endTime = System.nanoTime();
        long timeTakenByV3 = endTime-startTime;


        // Comparing both algo in terms of TIme Taken for the same input

        System.out.println("That took v3 " + timeTakenByV3 + " Nanoseconds");
        System.out.println("That took v2 " + timeTakenByV2 + " Nanoseconds");
        System.out.println("That took V1 " + timeTakenByV1 + " Nanoseconds (Original Algo)");
        System.out.println();
        if(timeTakenByV2 <= timeTakenByV3)  System.out.println("winner is v2 by ratio : " + timeTakenByV3*1.00/timeTakenByV2);
        else                            System.out.println("Winner is v3 by ratio: " + timeTakenByV2*1.00/timeTakenByV3);
        System.out.println();
        System.out.println("V1 / V3 := " + timeTakenByV1*1.000 / timeTakenByV3);
        System.out.println("V1 / V2 := " + timeTakenByV1*1.00 / timeTakenByV2);
        System.out.println();

        System.out.println("Comparing both method v3 -- v2 -- v1(ORIGIANL) in term of Dominated Fronts Size");
        System.out.println(dominated_fronts.size() + " --- " + dominated_frontv.size() + " ---- " + (dominated_fronto.size()-1));

//        System.out.println("domination array v3");
//        for(int i=0;i<dominated_fronts.size();i++){
//            Collections.sort(dominated_fronts.get(i));
//            System.out.println(i+" -- "+dominated_fronts.get(i));
//        }
//        System.out.println("DOmination mine v2");
//        for(int i=0;i<dominated_frontv.size();i++){
//            Collections.sort(dominated_frontv.get(i));
//            System.out.println(i+" -- "+dominated_frontv.get(i));
//        }

    }
}
