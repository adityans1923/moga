package ResearchPaper;

import java.io.*;
import java.util.*;

public class Generate {
    public final int obj_count  = Constant.obj_count;
    public final int population_size = Constant.population_size;
    public ArrayList<Chromosome> curr_pop, next_pop;
    int repeated_gen_count = 0;
    ArrayList<Long> v1Time, v2Time;

    public Generate(){
        this.curr_pop = new ArrayList<>(2* this.population_size + 1);
        this.next_pop = new ArrayList<>(2* this.population_size + 1);
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
        this.v1Time  = new ArrayList<>();
        this.v2Time  = new ArrayList<>();
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
        // Original Method
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> dom_by_count = new ArrayList<Integer>();

        int size = this.curr_pop.size();
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
            ArrayList<Integer> next_front = new ArrayList<>();
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
        int size = arr.size();
        for(int i = 0; i < size ; i++ )
            if(this.curr_pop.get(arr.get(i)).dominates(this.curr_pop.get(x)))
                return true;
        return false;
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort_v2(){
        // Proposed Method
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<>();
        ArrayList<Integer> pop_arr = new ArrayList<>();
        int pop_size = this.curr_pop.size();
        for(int i = 0; i < pop_size; i++)
            pop_arr.add(i);
        pop_arr.sort((integer, t1) -> {
            Double a=0.0,b=0.0;
            for(int i = 0; i < obj_count; i++){
                if(!curr_pop.get(integer).objective_values.get(i).equals(curr_pop.get(t1).objective_values.get(i))){
                    a = curr_pop.get(integer).objective_values.get(i);
                    b = curr_pop.get(t1).objective_values.get(i);
                    break;
                }
            }
            if(a < b) return -1;
            else if(a > b)    return 1;
            return 0;
        });

        dominated_front.add(new ArrayList<>());
        dominated_front.get(0).add(pop_arr.get(0));
        int size = pop_arr.size(), dsize;
        for(int j = 1; j < size ; j++){
            int front = 0;
            dsize = dominated_front.size();
            while(front < dsize && this.does_someone_dominates(dominated_front.get(front),pop_arr.get(j)))
                front++;
            if(front == dsize)
                dominated_front.add(new ArrayList<>());
            dominated_front.get(front).add(pop_arr.get(j));
        }
        return dominated_front;
    }

    void crowding_sort(ArrayList<Integer> front){
        for (int i = front.size() - 1; i >= 0; i--)
            this.curr_pop.get(front.get(i)).cDistance = 0;

        // for each objective
        for(int i = 0; i < this.obj_count; i++){
            int finalI = i;
            front.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer integer, Integer t1) {
                    double dd =(curr_pop.get(integer).objective_values.get(finalI) - curr_pop.get(t1).objective_values.get(finalI));
                    if(dd < 0) return -1;
                    else if(dd > 0) return 1;
                    return 0;
                }
            });
            this.curr_pop.get(front.get(0)).cDistance += Constant.inf;
            this.curr_pop.get(front.get(front.size() - 1)).cDistance += Constant.inf;

            double max = 0;
            double min = Constant.inf;

            for (int j = front.size() - 1; j >= 0; j--) {
                if (max < this.curr_pop.get(front.get(j)).objective_values.get(i))
                    max = this.curr_pop.get(front.get(j)).objective_values.get(i);
            }

            int size = front.size();
            for(int j = 1; j < size - 1 ; j++){
                this.curr_pop.get(front.get(j)).cDistance += (this.curr_pop.get(front.get(j+1)).objective_values.get(i)
                        - this.curr_pop.get(front.get(j-1)).objective_values.get(i) ) /(max-min);
            }
        }
    }

    void next_generation(){
        // Generating child population and combining P and Q
        for(int i = 0;i < this.population_size; i++){
            int father = Constant.rand.nextInt(this.population_size);
            int mother = Constant.rand.nextInt(this.population_size);
            while(mother == father)
                mother = Constant.rand.nextInt(this.population_size);
            Chromosome child = this.curr_pop.get(father).crossover(this.curr_pop.get(mother));
            child.mutate();
            if(this.curr_pop.contains(child))
            {
//                System.out.println("duplidate child produced: \n"+child.data);
                continue;
            }
            child.calculate_objective_value();

            // the below condition improves output by reducing the range of solution
            if(!(child.dominates(this.curr_pop.get(father) ) || child.dominates(this.curr_pop.get(mother)))){
//                System.out.println("Child does not dominates there parents");
                continue;
            }
            this.curr_pop.add(child);
        }

        // when no new child was able to live in current generation
        if(this.curr_pop.size() == this.population_size){
            this.repeated_gen_count++;
            this.next_pop = this.curr_pop;
//            System.out.println("Redundant calling of next generation");
//            next_generation();
            return;
        }
        this.repeated_gen_count = 0;
        long startTime, endTime;
        // sorting based on domination and return all the list of dominated fronts

        startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_fronts = this.non_dominating_sort();
        endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
        v1Time.add(timeTaken);

        startTime = System.nanoTime();
         dominated_fronts = this.non_dominating_sort_v2();
        endTime = System.nanoTime();
        timeTaken = endTime - startTime;
        v2Time.add(timeTaken);

        // now creating new population from the dominated fronts list using crowding distance

        int index = 0;
        // <= doesnt work because when the size if exactly 100 then it causing index out of range
        while(this.next_pop.size() + dominated_fronts.get(index).size() < this.population_size){
            for(int i = 0;i < dominated_fronts.get(index).size(); i++){
                this.next_pop.add(this.curr_pop.get(dominated_fronts.get(index).get(i)));
            }
            index++;
        }
        // Applying crowding distance sort on last dominated front
        this.crowding_sort(dominated_fronts.get(index));

        //  adding the remaining solutions to population
        for(int i = 0; i < dominated_fronts.get(index).size() && this.next_pop.size() < this.population_size; i++){
            this.next_pop.add(this.curr_pop.get(dominated_fronts.get(index).get(i)));
        }
    }


    public static void main(String[] args) {
        System.out.println("hello");
        Generate obj = new Generate();

//        for(int i=0;i<obj.curr_pop.size();i++)
//            obj.curr_pop.get(i).display();

        ArrayList<ArrayList<Double> > idat = new ArrayList<>();
        for(int i=0;i<obj.population_size;i++)
            idat.add(obj.curr_pop.get(i).objective_values);
        Plot initialChart = new Plot( "Initial Gen","obj1", "obj2"  ,idat );

        for(int i = 0; i < Constant.generation_count; i++){
            System.out.println("Generation : " + i);
            obj.next_generation();
            obj.curr_pop = obj.next_pop;
            obj.next_pop = new ArrayList<>(2* Constant.population_size + 1);

            if(i == Constant.generation_count - 1 || obj.repeated_gen_count == 10){
                ArrayList<ArrayList<Double> > plotdata = new ArrayList<>();
                for(int j = 0;j < obj.population_size; j++)
                    plotdata.add(obj.curr_pop.get(j).objective_values);
                Plot example = new Plot( "Gen: " + i,"obj1", "obj2"  ,plotdata );
//                for(int j = 0; j < Constant.population_size ; j++)
//                    obj.curr_pop.get(j).display();
//                return ;
                break;
            }
        }
        ArrayList<ArrayList<Double> > plot_data = new ArrayList<>();
        for (int j = 0; j < obj.v1Time.size() ; j++){
            ArrayList<Double> x = new ArrayList<>();
            x.add(obj.v1Time.get(j).doubleValue());
            x.add(obj.v2Time.get(j).doubleValue());
            plot_data.add(x);
        }
        Plot ht = new Plot("V1 vs V2", "V1", "V2", plot_data);

//        System.out.println("Population Size is : " + obj.curr_pop.size());
//        long startTime, endTime;
//
//
//        // non-dominating-sort-v1 original code
//
//        startTime = System.nanoTime();
//        ArrayList<ArrayList<Integer> > dominated_fronto = obj.non_dominating_sort();
//        endTime = System.nanoTime();
//        long timeTakenByV1 = endTime - startTime;
//
//
//        // non-dominating-sort-v2 simply optimized
//
//        startTime = System.nanoTime();
//        ArrayList<ArrayList<Integer> > dominated_frontv = obj.non_dominating_sort_v2();
//        endTime = System.nanoTime();
//        long timeTakenByV2 = endTime - startTime;
//
//
//        // Comparing both algo in terms of TIme Taken for the same input
//
//        System.out.println("That took V1 " + timeTakenByV1 + " Nanoseconds (Original Algo)");
//        System.out.println("That took v2 " + timeTakenByV2 + " Nanoseconds");
//        System.out.println();
//        System.out.println("V1 / V2 := " + timeTakenByV1*1.00 / timeTakenByV2);
//        System.out.println();
//
//        System.out.println("Comparing both method v2 -- v1(ORIGIANL) in term of Dominated Fronts Size");
//        System.out.println(dominated_frontv.size() + " ---- " + (dominated_fronto.size()-1));

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
//        System.out.println("Best Chromosome in dominating front 0: ");
//        for(int i = 0; i < dominated_frontv.get(0).size() ;i++)
//            obj.curr_pop.get(dominated_frontv.get(0).get(i)).display();
//        System.out.println();

    }
}
