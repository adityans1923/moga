import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Chromosome {
    private Random rand = new Random();
    public ArrayList<Double> objective_values;
    public final int obj_count=Constant.obj_count;
    public ArrayList<Integer> data;
    public double fitness_value;
    public int rank;
    public double distance;  // Crowding Distance
    public int id;           // unique Id to distinguish chromosomes

    Chromosome(int n){
        data = new ArrayList<Integer>(n+1);
        for(int i=0;i<n;i++)
            data.add(i);
        Collections.shuffle(this.data);
        objective_values = new ArrayList<Double>(Collections.nCopies(this.obj_count + 1, 0.0));
        rank = Constant.inf;
        distance = Constant.inf;
        fitness_value = Constant.inf;
        this.calculate_objective_value();
        this.calculate_fitness_value();
    }
    Chromosome(ArrayList<Integer> initial_array){
        data = new ArrayList<Integer>(initial_array.size() + 1);
        data.addAll(initial_array);
        objective_values = new ArrayList<Double>(Collections.nCopies(this.obj_count + 1, 0.0));
        rank = Constant.inf;
        distance = Constant.inf;
        fitness_value = Constant.inf;
        this.calculate_objective_value();
        this.calculate_fitness_value();
    }
    double calc_obj1(){
        this.objective_values.set(0,0.0);
        for (Integer datum : this.data) objective_values.set(0, (double) datum + objective_values.get(0));
        return objective_values.get(0);
    }
    double calc_obj2(){
        this.objective_values.set(1,0.0);
        double tdouble= 0.0;
        for (int i=0; i< this.data.size() ; i++) {
            tdouble = i / ((double) this.data.get(i) + 1);
            objective_values.set(1, tdouble + objective_values.get(1));
        }
        return objective_values.get(1);
    }
    void calculate_objective_value(){
        calc_obj1();
        calc_obj2();
    }
    void calculate_fitness_value(){
        fitness_value = -1;
    }
    void display(){
        System.out.println(this.data);
    }
    void mutate(){
        int n = this.data.size();
        if(rand.nextInt(100) < Constant.mutation_probability)
            Collections.swap(this.data,this.rand.nextInt(n) , this.rand.nextInt(n));
    }
    Chromosome crossover(Chromosome other){
        // RCMX operator
        int swap_length = this.data.size() / 2;
        int start_at = rand.nextInt(this.data.size() - 1 - swap_length);
        int finish_at = start_at + swap_length;
        ArrayList<Integer> child_arr = new ArrayList<Integer>();
        boolean[] visit = new boolean[this.data.size()];
        // copying from first parent
        for(int i=0;i<this.data.size();i++) {
            visit[this.data.get(i)] = false;
            child_arr.add(-1);
            if (i >= start_at && i <= finish_at) {
                child_arr.set(i, this.data.get(i));
                visit[this.data.get(i)] = true;
            }
        }
        // copying from 2nd parent
        int j= 0;
        for(int i=0;i<this.data.size() && j < this.data.size();i++){
            if(i < start_at || i > finish_at){
                while(j<this.data.size() && visit[other.data.get(j)]) j++;
                child_arr.set(i, other.data.get(j));
                j++;
            }
        }
        return new Chromosome(child_arr);
    }

    // Completed Functions
    void setDistance(double distance){
        this.distance=distance;
    }
    double getDistance(){
        return this.distance;
    }
    void setRank(int rank){
        this.rank=rank;
    }
    double getFitness_value(){
        return this.fitness_value;
    }
    int getRank(){
        return this.rank;
    }
    boolean dominates(Chromosome object){
        boolean flag=true;
        for(int i = 0; i<this.obj_count; i++){
            if(this.objective_values.get(i) < object.objective_values.get(i))
                return false;
            if(flag){
                if(this.objective_values.get(i) > object.objective_values.get(i))
                    flag=false;
            }
        }
        return !flag;
    }
}
