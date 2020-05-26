package ResearchPaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Constant implements Serializable {
    public static int obj_count=2;
    public static final int inf=99999;
    public static int chromosome_size = 1;
    public static int population_size= 100;
    public static int mutation_probability = 7;
    public static int generation_count = 500;
    public static Random rand = new Random();
    public static Chromosome get_chromosome(){
        return new SCH();
    }
    public static Chromosome get_chromosome(ArrayList<Integer> initial_array){
        return new SCH(initial_array);
    }
}

// SCH -- chromosome_size = 1, obj_count = 2