package ResearchPaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public abstract class Chromosome implements Serializable {
    protected ArrayList<Double> objective_values = new ArrayList<Double>(Collections.nCopies(Constant.obj_count , 0.0));;
    protected ArrayList<Double> data = new ArrayList<>(Constant.chromosome_size);

    public Chromosome(){ }

    void display(){
        System.out.println(this.data + " " + this.objective_values);
    }
    void mutate(){
        int n = this.data.size();
        if(Constant.rand.nextInt(100) < Constant.mutation_probability)
            Collections.swap(this.data,Constant.rand.nextInt(n) , Constant.rand.nextInt(n));
    }

    abstract Chromosome crossover(Chromosome other);
    abstract void calculate_objective_value();

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
