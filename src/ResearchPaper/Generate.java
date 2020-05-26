package ResearchPaper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Generate {
    public final int obj_count  = Constant.obj_count;
    public final int population_size = Constant.population_size;
    public ArrayList<Chromosome> curr_pop;

    public Generate(){
        curr_pop = new ArrayList<>(2* this.population_size + 1);
        // initializing population
        for(int i = 0;i < this.population_size; i++){
            Chromosome new_sol = Constant.get_chromosome();
            if(this.curr_pop.contains(new_sol)){
                i--;
                continue;
            }
            new_sol.calculate_objective_value();
            this.curr_pop.add(new_sol);
        }
    }

    ArrayList<ResearchPaper.Chromosome> readObject(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream= new ObjectInputStream(new FileInputStream("./database/gen_"+filename));
        ArrayList<ResearchPaper.Chromosome> hh = (ArrayList<ResearchPaper.Chromosome>)inputStream.readObject();
        inputStream.close();
        return hh;
    }
    void writeObject(String filename, ArrayList<ResearchPaper.Chromosome> obj) throws IOException{
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./database/gen_"+filename));
        outputStream.writeObject(obj);
        outputStream.flush();
        outputStream.close();
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
                    }
                }
            }
            front_count++;
            dominated_front.add(next_front);
        }
        return dominated_front;
    }

    public boolean does_someone_dominates(ArrayList<Integer> arr, Integer x){
        for(int i = arr.size() - 1;i >= 0; i-- )
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

    public static void main(String[] args) throws IOException {
        System.out.println("hello");
        Generate obj = new Generate();

        // adding new generation
//        for(int i = 0;i < obj.population_size; i++) {
//            int index1 = Constant.rand.nextInt(obj.population_size);
//            int index2 = Constant.rand.nextInt(obj.population_size);
//            while (index2 == index1)
//                index2 = Constant.rand.nextInt(obj.population_size);
//            Chromosome child = obj.curr_pop.get(index1).crossover(obj.curr_pop.get(index2));
//            child.mutate();
//            if (obj.curr_pop.contains(child)) {
//                //System.out.println("duplicate child produced: \n"+child.data);
//                continue;
//            }
//            child.calculate_objective_value();
//            // the below condition improves output by reducing the range of solution
//            if (!(child.dominates(obj.curr_pop.get(index1)) || child.dominates(obj.curr_pop.get(index2)))) {
//                //System.out.println("Child does not dominates there parents");
//                continue;
//            }
//            obj.curr_pop.add(child);
//        }

//        for(int i=0;i<obj.curr_pop.size();i++)
//            obj.curr_pop.get(i).display();

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


        // Comparing both algo in terms of TIme Taken for the same input

        System.out.println("That took V1 " + timeTakenByV1 + " Nanoseconds (Original Algo)");
        System.out.println("That took v2 " + timeTakenByV2 + " Nanoseconds");
        System.out.println();
        System.out.println("V1 / V2 := " + timeTakenByV1*1.00 / timeTakenByV2);
        System.out.println();

        System.out.println("Comparing both method v2 -- v1(ORIGIANL) in term of Dominated Fronts Size");
        System.out.println(dominated_frontv.size() + " ---- " + (dominated_fronto.size()-1));

//        System.out.println("domination array v1(Original Algorithm");
//        for(int i=0;i<dominated_fronto.size();i++){
//            Collections.sort(dominated_fronto.get(i));
//            System.out.println(i+" -- "+dominated_fronto.get(i));
//        }
//        System.out.println("Domination Array Proposed Algo");
//        for(int i=0;i<dominated_frontv.size();i++){
//            Collections.sort(dominated_frontv.get(i));
//            System.out.println(i+" -- "+dominated_frontv.get(i));
//        }
        System.out.println("Best Chromosome in dominating front 0: ");
        for(int i = 0; i < dominated_frontv.get(0).size() ;i++)
            obj.curr_pop.get(dominated_frontv.get(0).get(i)).display();
        System.out.println();

    }
}
