package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class DTLZ1 extends Chromosome {

    @SafeVarargs
    public DTLZ1(ArrayList<Double>... initial_array){
        super();
        if (initial_array.length == 0) {
            for (int i = 0; i < Constant.chromosome_size; i++){
                this.data.add(Constant.rand.nextDouble());
            }
        }
        else
            this.data = initial_array[0];
    }

    @Override
    protected void calculate_objective_value() {

        double gx = Constant.chromosome_size - Constant.obj_count + 1;
        for( int i = Constant.obj_count - 1; i < Constant.chromosome_size; i++){
            gx += Math.pow(this.data.get(i) -0.5, 2) - Math.cos( 20*Math.PI*(this.data.get(i) - 0.5));
        }

        for(int i = 0; i < Constant.obj_count; i++){
            double fx = 0.5;
            for(int j = 0; j < Constant.obj_count - i - 1; j++){
                fx *= this.data.get(j);
            }
            if( i!= 0){
                fx *= (1 - this.data.get(Constant.obj_count - i -1));
            }
            fx *= (1 + gx);
            this.objective_values.set(i, fx);
        }
    }
}
