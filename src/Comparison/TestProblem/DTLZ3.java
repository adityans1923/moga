package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class DTLZ3 extends Chromosome {

    @SafeVarargs
    public DTLZ3( ArrayList<Double>... initial_array){
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
        gx *= 100;
        for(int i = 0; i < Constant.obj_count; i++){
            double fx = (1 + gx) ;
            for(int j = 0; j < Constant.obj_count - i - 1; j++){
                fx *= Math.cos( this.data.get(j) * Math.PI / 2 );
            }
            if( i != 0){
                fx *=  Math.sin(this.data.get(Constant.obj_count - i -1) * Math.PI / 2);
            }

            this.objective_values.set(i, fx);
        }
    }
}
