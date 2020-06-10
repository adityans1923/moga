package Comparison;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Generate {

    public ArrayList<Chromosome> population;

    public Generate(){
        this.population = new ArrayList<>(2* Constant.population_size + 1);
        // initializing population
        for(int i = 0; i < Constant.population_size; i++){
            Chromosome new_sol = Constant.get_chromosome();
            if(this.population.contains(new_sol)){
                i--;
                continue;
            }
            new_sol.calculate_objective_value();
            this.population.add(new_sol);
        }
    }

    ArrayList<ArrayList<Integer> > nsga_nondominated_sort() {
        // sort according to the domination into the fronts
        // Original Method
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> dom_by_count = new ArrayList<Integer>();

        int size = this.population.size();
        for (int i = 0; i < size; i++) {
            sol_dom.add(new ArrayList<Integer>());
            dom_by_count.add(0);
            for (int j = 0; j < size; j++) {
                if (this.population.get(i).dominates(this.population.get(j)))
                    sol_dom.get(i).add(j);
                else if (this.population.get(j).dominates(this.population.get(i)))
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
            if(this.population.get(arr.get(i)).dominates(this.population.get(x)))
                return true;
        return false;
    }
    ArrayList<ArrayList<Integer> > proposed_algo(){
        // Proposed Method
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<>();
        ArrayList<Integer> pop_arr = new ArrayList<>();
        int pop_size = this.population.size();
        for(int i = 0; i < pop_size; i++)
            pop_arr.add(i);
        pop_arr.sort((integer, t1) -> {
            Double a=0.0,b=0.0;
            for(int i = 0; i < Constant.obj_count; i++){
                if(!population.get(integer).objective_values.get(i).equals(population.get(t1).objective_values.get(i))){
                    a = population.get(integer).objective_values.get(i);
                    b = population.get(t1).objective_values.get(i);
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

    public Object deepCopy( Object object){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            Object object_copy = (Object) new ObjectInputStream(bais).readObject();
            return object_copy;
        } catch (Exception e){
            System.out.println("Error in deep copy ");
            System.exit(90);
        }
        return null;
    }

    public void run(ArrayList<Double> originalAlgoTime, ArrayList<Double> proposedAlgoTime){

        Generate obj = new Generate();

        long startTime,endTime, timeTaken;
        ArrayList<ArrayList<Integer> > dominated_front_by_original;
        ArrayList<ArrayList<Integer> > dominated_front_by_proposed;

        startTime = System.nanoTime();
        dominated_front_by_original = this.nsga_nondominated_sort();
        endTime = System.nanoTime();
        timeTaken = endTime - startTime;
        originalAlgoTime.add((double) timeTaken);

        startTime = System.nanoTime();
        dominated_front_by_original = this.proposed_algo();
        endTime = System.nanoTime();
        timeTaken = endTime - startTime;
        proposedAlgoTime.add((double)timeTaken);

        obj = null;
        dominated_front_by_original = null;
        dominated_front_by_proposed = null;
    }

    public static void main(String[] args) {
        System.out.println("Program starts here : Hello Everyone");
        ArrayList<Double> originalAlgoTime =new ArrayList<>(), proposedAlgoTime = new ArrayList<>();

        Generate obj = new Generate();
        for(int i = 0; i < Constant.iteration_count; i++){
            System.out.println(i);
            obj.run(originalAlgoTime, proposedAlgoTime);
        }
        double avgTimeByOriginal = 0, avgTimeByProposed = 0;

        for(int i = 0 ; i < Constant.iteration_count; i++){
            avgTimeByOriginal += originalAlgoTime.get(i);
            avgTimeByProposed += proposedAlgoTime.get(i);
        }
        avgTimeByOriginal /= Constant.iteration_count;
        avgTimeByProposed /= Constant.iteration_count;

        System.out.println("Final Original : " + avgTimeByOriginal + " \nProposed : "+ avgTimeByProposed );
    }
}
