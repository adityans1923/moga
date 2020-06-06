package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class ZDT4 extends Chromosome {

    @SafeVarargs
    public ZDT4(ArrayList<Double>... initial_array){
        super();
        if (initial_array.length == 0) {
            this.data.add(Constant.rand.nextDouble());
            for (int i = 1; i < 10; i++){
                this.data.add(Constant.rand.nextDouble() * 5 *(Constant.rand.nextBoolean()?1:-1));
            }
        }
        else
            this.data = initial_array[0];
    }

    @Override
    public void calculate_objective_value() {
        this.objective_values.set(0, this.data.get(0));
        double gx = 1 + 10 * (10 - 1);
        for (int i = 1; i < 10; i++)
            gx += Math.pow(this.data.get(i), 2) - 10 * Math.cos(4*Math.PI*this.data.get(i));
        gx = gx * (1 - Math.sqrt(this.data.get(0) / gx) );
        this.objective_values.set(1, gx);
    }
}
