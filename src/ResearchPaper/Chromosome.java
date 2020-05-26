package ResearchPaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public abstract class Chromosome implements Serializable {
    public ArrayList<Double> objective_values = new ArrayList<Double>(Collections.nCopies(Constant.obj_count , 0.0));;
    public ArrayList<Double> data = new ArrayList<>();

    public Chromosome(){}

    void display(){
        System.out.println(this.data + " " + this.objective_values);
    }
    public void mutate(){
        int n = this.data.size();
        if(Constant.rand.nextInt(100) < Constant.mutation_probability)
            Collections.swap(this.data,Constant.rand.nextInt(n) , Constant.rand.nextInt(n));
    }

    protected abstract Chromosome crossover(Chromosome other);
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
