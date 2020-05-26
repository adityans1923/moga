package ResearchPaper;

import java.util.ArrayList;
import java.util.Collections;

public class SCH extends Chromosome {

    // chromosome size : 1 number between [-1000,1000]
    // obj_count : 2 [x^2 ,(x-2)^2]

    SCH() {
        super();
        data.add(Constant.rand.nextInt(1001));
    }
    SCH(ArrayList<Integer> initial_array){
        super();
        data = initial_array;
    }

    @Override
    void calculate_objective_value() {
        Double a1 = Math.pow(this.data.get(0), 2);
        Double a2 = Math.pow((this.data.get(0) - 2), 2);
        this.objective_values.set(0, a1);
        this.objective_values.set(1, a2);
    }


    @Override
    Chromosome crossover(Chromosome other){
        int pos = Constant.rand.nextInt(10);
        int child = this.data.get(0);
        int parent2 = other.data.get(0);
        parent2 = parent2 & (1 << (pos + 1) -1);
        child = (child & ((1 << (11) -1) & (1 << pos)) ^ parent2);
        ArrayList<Integer> newdata = new ArrayList<>(Collections.singletonList(child));
        return new SCH(newdata);
    }

    @Override
    void mutate(){
        this.data.set(0, this.data.get(0) ^ (1 << (Constant.rand.nextInt(10))));
    }

}