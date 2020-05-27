package Comparison;

import Comparison.TestProblem.SCH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Constant implements Serializable {
    public static int obj_count=2;
    public static final int inf=99999;
    public static int population_size= 100;
    public static int mutation_probability = 23;
    public static int generation_count = 1000;
    public static Random rand = new Random();

    @SafeVarargs
    public static Chromosome get_chromosome(ArrayList<Double>... initial_array){
        return new SCH(initial_array);
    }
}

// SCH -- chromosome_size = 1, obj_count = 2 range=[0,1000]
// FON -- chromosome_size = 3, obj_count = 2 range=[-4,4]
// POL -- chromosome_size = 2, obj_count = 2 range=[-pi,pi]
// ZDT! -- chromosome_size = 30, obj_count = 2 range = [0,1]
