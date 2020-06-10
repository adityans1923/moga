package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class KUR extends Chromosome {

    @SafeVarargs
    public KUR(ArrayList<Double>... initial_array) {
        super();
        if (initial_array.length == 0){
            for (int i = 0;i < 3; i++)
                this.data.add(Constant.rand.nextDouble()*(5)*(Constant.rand.nextBoolean()?1:-1));
        }
        else
            this.data = initial_array[0];
    }

    @Override
    protected void calculate_objective_value() {
        double o1 = 0.00, o2 = 0.0;
        for (int i = 0; i < 2; i++){
            o1 += -10 * Math.exp(-0.2 * Math.sqrt( this.data.get(i)* this.data.get(i) + this.data.get(i+1)*this.data.get(i+1)));
            o2 += Math.pow(Math.abs(this.data.get(i)), 0.8) + 5*Math.sin(Math.pow(this.data.get(i), 3));
        }
        o2 += Math.pow(Math.abs(this.data.get(2)), 0.8) + 5*Math.sin(Math.pow(this.data.get(2), 3));
        this.objective_values.set(0, o1);
        this.objective_values.set(1, o2);
    }
}
