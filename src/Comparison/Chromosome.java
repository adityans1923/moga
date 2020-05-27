package Comparison;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public abstract class Chromosome implements Serializable {
    public ArrayList<Double> objective_values = new ArrayList<Double>(Collections.nCopies(Constant.obj_count , 0.0));;
    public ArrayList<Double> data = new ArrayList<>();
    public double cDistance = 0;

    public Chromosome(){}

    void display(){
        System.out.println(this.data + " " + this.objective_values);
    }

    public void mutate(){
        if(Constant.mutation_probability >= Constant.rand.nextInt(100)){
            double beta = Constant.rand.nextDouble();
            int pos = Constant.rand.nextInt(this.data.size());
            beta  = this.data.get(pos) * (beta);
            this.data.set(pos, beta);
        }
    }

    public Chromosome crossover(Chromosome other) {
        // RCMX operator
        int swap_length = this.data.size() / 2;
        int start_at = Constant.rand.nextInt(this.data.size() - swap_length);
        int finish_at = start_at + swap_length;
        double alpha = Constant.rand.nextDouble();
        ArrayList<Double> child_arr = new ArrayList<>();
        for(int i = 0;i < this.data.size(); i++) {
            if( i < start_at || i > finish_at){
                child_arr.add(this.data.get(i));
            }else{
                child_arr.add( alpha * this.data.get(i) +(1 - alpha)*other.data.get(i) );
            }
        }
        return Constant.get_chromosome(child_arr);
    }
    protected abstract void calculate_objective_value();

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

    public boolean dominates(Chromosome object){
        boolean isAnyStrictlySmall = false;
        for(int i = 0; i < Constant.obj_count; i++){
            if(this.objective_values.get(i).compareTo(object.objective_values.get(i)) > 0)
                return false;
            if(this.objective_values.get(i).compareTo(object.objective_values.get(i)) < 0)
                isAnyStrictlySmall = true;
        }
        return isAnyStrictlySmall;
    }

}
