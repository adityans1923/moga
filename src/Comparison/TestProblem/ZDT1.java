package Comparison.TestProblem;

import Comparison.Chromosome;
import Comparison.Constant;

import java.util.ArrayList;

public class ZDT1 extends Chromosome {

    @SafeVarargs
    public ZDT1(ArrayList<Double>... initial_array){
        super();
        if (initial_array.length == 0) {
            for (int i = 0; i < 30; i++){
                this.data.add(Constant.rand.nextDouble());
            }
        }
        else
            this.data = initial_array[0];
    }

    @Override
    protected void calculate_objective_value() {
        this.objective_values.set(0, this.data.get(0));
        double gx = 0;
        for (int i = 1; i < 30; i++)
            gx += this.data.get(i);
        gx = (9 * gx)/29 + 1;
        gx = gx * (1 - Math.sqrt(this.data.get(0) / gx));
        this.objective_values.set(1, gx);
    }
}
