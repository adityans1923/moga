package ResearchPaper.TestProblem;

import ResearchPaper.Chromosome;
import ResearchPaper.Constant;

import java.util.ArrayList;

public class POL extends Chromosome {

    @SafeVarargs
    public POL(ArrayList<Double>... initial_array){
        super();
        if (initial_array.length == 0) {
            for(int i = 0; i < 2; i++)
                this.data.add(Constant.rand.nextDouble()*(Math.PI)*(Constant.rand.nextBoolean()?1:-1));
        }
        else
            this.data = initial_array[0];
    }

    @Override
    protected void calculate_objective_value() {
        double a1 = 0.5*Math.sin(1) - 2*Math.cos(1) + Math.sin(2) - 1.5*Math.cos(2);
        double a2 = 1.5*Math.sin(1) - Math.cos(1) + 2*Math.sin(2) - 0.5*Math.cos(2);
        double x1 = this.data.get(0), x2 = this.data.get(1);
        double b1 = 0.5*Math.sin(x1) - 2*Math.cos(x1) + Math.sin(x2) - 1.5*Math.cos(x2);
        double b2 = 1.5*Math.sin(x1) - Math.cos(x1) + 2*Math.sin(x2) - 0.5*Math.cos(x2);
        double f1 = 1 + Math.pow(a1-b1, 2) + Math.pow(a2 - b2, 2);
        double f2 = Math.pow(x1 + 3, 2) + Math.pow(x2 + 1, 2);
        this.objective_values.set(0, f1);
        this.objective_values.set(1, f2);

    }
}
