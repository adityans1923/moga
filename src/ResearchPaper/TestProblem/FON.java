package ResearchPaper.TestProblem;

import ResearchPaper.Chromosome;
import ResearchPaper.Constant;

import java.util.ArrayList;

public class FON extends Chromosome {

    private double root3 = 1 / Math.pow(3.00, 0.5);

    public FON(){
        super();
        for(int i = 0; i < 3; i++)
            this.data.add(Constant.rand.nextDouble()*(4)*(Constant.rand.nextBoolean()?1:-1));
    }

    public FON(ArrayList<Double> initial_array){
        super();
        this.data = initial_array;
    }

    @Override
    public void calculate_objective_value() {
        double a1 = 0.0, a2 = 0.0;
        int n = this.data.size();
        for(int i = 0; i < n; i++){
            a1 = a1 + Math.pow(this.data.get(i) - root3, 2);
            a2 = a2 + Math.pow(this.data.get(i) + root3, 2);
        }
        a1 = a1 * -1;
        a2 = a2 * -1;
        a1 = 1 - Math.exp(a1);
        a2 = 1 - Math.exp(a2);
        this.objective_values.set(0, a1);
        this.objective_values.set(1, a2);
    }

}
