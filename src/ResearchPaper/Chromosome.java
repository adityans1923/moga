package ResearchPaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public abstract class Chromosome implements Serializable {
    protected ArrayList<Double> objective_values = new ArrayList<Double>(Collections.nCopies(Constant.obj_count , 0.0));;
    protected ArrayList<Integer> data = new ArrayList<>(Constant.chromosome_size);

    public Chromosome(){ }

    abstract void calculate_objective_value();
    void display(){
        System.out.println(this.data + " " + this.objective_values);
    }

    void mutate(){
        int n = this.data.size();
        if(Constant.rand.nextInt(100) < Constant.mutation_probability)
            Collections.swap(this.data,Constant.rand.nextInt(n) , Constant.rand.nextInt(n));
    }

    Chromosome crossover(Chromosome other){
        // RCMX operator
        int swap_length = this.data.size() / 2;
        int start_at = Constant.rand.nextInt(this.data.size() - 1 - swap_length);
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
        return Constant.get_chromosome(child_arr);
    }

    @Override
    public boolean equals(Object o){
        if( !(o instanceof Chromosome))
            return false;
        Chromosome other=(Chromosome)o;
        for(int i = 0;i < this.data.size(); i++)
            if(!this.data.get(i).equals(other.data.get(i)))
                return false;
        return true;
    }

    boolean dominates(Chromosome object){
        boolean isAnyStrictlySmall = false;
        for(int i = 0; i < Constant.obj_count; i++){
            if(this.objective_values.get(i) > object.objective_values.get(i))
                return false;
            if(this.objective_values.get(i) < object.objective_values.get(i))
                isAnyStrictlySmall = true;
        }
        return isAnyStrictlySmall;
    }

}
