import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Chromosome implements Serializable {
    private Random rand = new Random();
    public ArrayList<Double> objective_values;
    public ArrayList<Integer> data;
    public int rank;
    public String drank;
    public double distance;  // Crowding Distance
    public int id;           // unique Id to distinguish chromosomes

    private void init(){
        rank = Constant.inf;
        distance = Constant.inf;
        objective_values = new ArrayList<Double>(Collections.nCopies(Constant.obj_count , 0.0));
        drank ="";
    }

    Chromosome(int n){
        data = new ArrayList<Integer>(n + 1);
        for(int i=0;i<n;i++)
            data.add(i);
        Collections.shuffle(this.data);
        this.init();
//        this.calculate_objective_value();
    }
    Chromosome(ArrayList<Integer> initial_array){
        data = new ArrayList<Integer>(initial_array.size() + 1);
        data.addAll(initial_array);
        this.init();
//        this.calculate_objective_value();
    }
    double calc_obj1(){
        this.objective_values.set(0,0.0);
        for (int i=0; i< this.data.size() ; i++)
            objective_values.set(0, (double) this.data.get(i) * i + objective_values.get(0));
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
    void display(){
        System.out.println(this.data + " " + this.objective_values);
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

    @Override
    public boolean equals(Object o){
        Chromosome other=(Chromosome)o;
        for(int i=0;i<this.data.size();i++)
            if(!this.data.get(i).equals(other.data.get(i)))
                return false;
        return true;
    }
    int getRank(){
        return this.rank;
    }
    boolean dominates(Chromosome object){
        double a = this.objective_values.get(0), b=object.objective_values.get(0);
        double c = this.objective_values.get(1), d=object.objective_values.get(1);
        return (a<=b && c<=d && (a<b || c<d));
    }
}
