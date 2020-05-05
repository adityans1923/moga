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
    public double distance;

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
        for(int i=0;i<initial_array.size();i++)
            data.set(i,initial_array.get(i));
        objective_values = new ArrayList<Double>(Collections.nCopies(this.obj_count + 1, 0.0));
        rank = Constant.inf;
        distance = Constant.inf;
        fitness_value = Constant.inf;
        this.calculate_objective_value();
        this.calculate_fitness_value();
    }
    double calc_obj1(){
        for(int i=0;i<this.data.size();i++)
            objective_values.set(0, (double)this.data.get(i) + objective_values.get(0));
        return objective_values.get(0);
    }
    double calc_obj2(){
        double tdouble= 0.0;
        for(int i=0;i<this.data.size();i++) {
            tdouble = 1 / ((double)this.data.get(i)+1);
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
        if(rand.nextInt() < Constant.mutation_probability)
            // mutate
            this.rank=Constant.inf;
    }
    Chromosome crossover(Chromosome other){
        Chromosome child = new Chromosome(10);
        return child;
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
