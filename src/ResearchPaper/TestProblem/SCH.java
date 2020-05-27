package ResearchPaper.TestProblem;

import ResearchPaper.Chromosome;
import ResearchPaper.Constant;

import java.util.ArrayList;
import java.util.Collections;

public class SCH extends Chromosome {

    // chromosome size : 1 number between [-1000,1000]
    // obj_count : 2 [x^2 ,(x-2)^2]

    @SafeVarargs
    public SCH(ArrayList<Double>... initial_array) {
        super();
        if (initial_array.length == 0)
            this.data.add( Constant.rand.nextDouble() * 1000 * (Constant.rand.nextBoolean()?1:-1));
        else
            this.data = initial_array[0];
    }

    @Override
    public void calculate_objective_value() {
        Double a1 = Math.pow(this.data.get(0), 2);
        Double a2 = Math.pow((this.data.get(0) - 2), 2);
        this.objective_values.set(0, a1);
        this.objective_values.set(1, a2);
    }


//    @Override
//    public Chromosome crossover(Chromosome other){
//        int pos = Constant.rand.nextInt(10);
//        int child =  this.data.get(0).intValue();
//        int parent2 = other.data.get(0).intValue();
//        parent2 = parent2 & (1 << (pos + 1) -1);
//        child = (child & ((1 << (11) -1) & (1 << pos)) ^ parent2);
//        ArrayList<Double> newdata = new ArrayList<Double>(Collections.singletonList((double)child));
//        return new SCH(newdata);
//    }
}
