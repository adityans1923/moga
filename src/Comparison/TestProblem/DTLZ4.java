package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class DTLZ4 extends Chromosome {

    @SafeVarargs
    public DTLZ4( ArrayList<Double>... initial_array){
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

        double gx = 0 ;
        for( int i = Constant.obj_count - 1; i < Constant.chromosome_size; i++){
            gx += Math.pow(this.data.get(i) - 0.5, 2) ;
        }
        int alpha = 100;
        for(int i = 0; i < Constant.obj_count; i++){
            double fx = (1 + gx) ;
            for(int j = 0; j < Constant.obj_count - i - 1; j++){
                fx *= Math.cos( Math.pow(this.data.get(j), alpha) * Math.PI / 2 );
            }
            if( i != 0){
                fx *=  Math.sin( Math.pow(this.data.get(Constant.obj_count - i -1), alpha) * Math.PI / 2);
            }
            this.objective_values.set(i, fx);
        }
    }
}
