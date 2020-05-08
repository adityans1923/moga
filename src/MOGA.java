import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MOGA {
    private Random rand;
    public final int obj_count  = Constant.obj_count;
    public final int population_size = Constant.population_size;
    public final int chromosome_size = Constant.chromosome_size;
    public ArrayList<Chromosome> curr_pop;
    public ArrayList<Chromosome> next_pop;
    MOGA(){
        // initialize variables
        curr_pop = new ArrayList<Chromosome>(2* this.population_size + 1);
        rand  = new Random();
        // initializing population
        for(int i=0;i< this.population_size; i++) curr_pop.add(new Chromosome(chromosome_size));
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort() {
        // sort according to the domination into the fronts
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom_by = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> count_of_dom_by = new ArrayList<Integer>();

        for (int i = 0; i < curr_pop.size(); i++) {
            sol_dom_by.add(new ArrayList<Integer>());
            count_of_dom_by.add(0);
            for (int j = 0; j < curr_pop.size(); j++) {
                if (this.curr_pop.get(i).dominates(this.curr_pop.get(j)))
                    sol_dom_by.get(i).add(j);
                else if (this.curr_pop.get(j).dominates(this.curr_pop.get(i)))
                    count_of_dom_by.set(i, count_of_dom_by.get(i) + 1);
            }
            if (count_of_dom_by.get(i) == 0) {
                if (dominated_front.size() == 0)
                    dominated_front.add(new ArrayList<Integer>());
                dominated_front.get(0).add(i);
            }
        }
        int front_count = 0;
        while (dominated_front.get(front_count).size() != 0) {
            ArrayList<Integer> next_front = new ArrayList<Integer>();
            for (int i = 0; i < dominated_front.get(front_count).size(); i++) {
                for (int j = 0; sol_dom_by.get(dominated_front.get(front_count).get(i)).size() > j; j++) {
                    int cnt = count_of_dom_by.get(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j));
                    cnt--;
                    count_of_dom_by.set(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j), cnt);
                    if (cnt == 0) {
                        next_front.add(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j));
                        this.curr_pop.get(sol_dom_by.get(dominated_front.get(front_count).get(i)).get(j)).setRank(front_count + 1);
                    }
                }
            }
            front_count++;
            dominated_front.add(next_front);
        }
        return dominated_front;
    }
    void crowding_sort(ArrayList<Integer> front){
        for(Integer x:front)    this.curr_pop.get(x).distance = 0;
        // for each objective
        for(int i=0;i<this.obj_count; i++){
            int finalI = i;
            front.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer integer, Integer t1) {
                    double dd =(curr_pop.get(integer).objective_values.get(finalI) - curr_pop.get(t1).objective_values.get(finalI));
                    if(dd<0) return -1;
                    else if(dd > 0) return 1;
                    return 0;
                }
            });
            this.curr_pop.get(front.get(0)).distance = Constant.inf;
            this.curr_pop.get(front.get(front.size() - 1)).distance = Constant.inf;
            double max = 0;
            double min = Constant.inf;
            for (Integer integer : front) {
                if (max < this.curr_pop.get(integer).objective_values.get(i))
                    max = this.curr_pop.get(integer).objective_values.get(i);
            }
            for(int j=1; j < front.size() - 1 ; j++){
                this.curr_pop.get(front.get(j)).distance += (double)(this.curr_pop.get(front.get(j+1)).objective_values.get(i)
                    - this.curr_pop.get(front.get(j-1)).objective_values.get(i) ) /(max-min);
            }
        }
    }

    void next_generation(){
        // Generating child population and combining P and Q
        for(int i=0;i<this.population_size;i++){
            int index1 = rand.nextInt(this.population_size);
            int index2 = rand.nextInt(this.population_size);
            while(index2 == index1)
                index2 = rand.nextInt(this.population_size);
            this.curr_pop.add(this.curr_pop.get(index1).crossover(this.curr_pop.get(index2)));
            this.curr_pop.get(this.curr_pop.size() - 1).mutate();
        }

        // sorting based on dominatinos and return all the list of dominated fronts
        ArrayList<ArrayList<Integer> > dominated_fronts = this.non_dominating_sort();

        // now creating new population from the dominated fronts list using crowding distance
        this.next_pop = new ArrayList<Chromosome>(this.population_size * 2 + 1);
        int index = 0;
        while(this.next_pop.size() + dominated_fronts.get(index).size() <= this.population_size){
            for(int i = 0;i < dominated_fronts.get(index).size(); i++){
                this.next_pop.add(this.curr_pop.get(dominated_fronts.get(index).get(i)));
            }
            index++;
        }
        // Applying crowding distance sort on last dominated front
        this.crowding_sort(dominated_fronts.get(index));

        //  adding the remaining solutions to population
        for(int i = 0; i < dominated_fronts.get(index).size() && this.next_pop.size() < this.population_size; i++){
            this.next_pop.add(this.curr_pop.get(dominated_fronts.get(index).get(i)));
        }
    }

    public static void main(String[] args) {
        System.out.println("hello");
        MOGA obj = new MOGA();
        for(int i=0;i<10;i++)
            obj.curr_pop.get(i).display();
        System.out.println("next gene");
        obj.next_generation();
        for(int i=0;i<10;i++)
            obj.next_pop.get(i).display();
        System.out.println("plotting");
        ArrayList<ArrayList<Double> > plotdata = new ArrayList<ArrayList<Double> >();
        for(int i=0;i<obj.population_size;i++)
            plotdata.add(obj.curr_pop.get(i).objective_values);

        Plot example = new Plot( "curr gen chart",
                "X Axiz", "Y Axis"  ,plotdata );

        System.out.println("plotting next gen");
        ArrayList<ArrayList<Double> > plotdata2 = new ArrayList<ArrayList<Double> >();
        for(int i=0;i<obj.population_size;i++)
            plotdata2.add(obj.next_pop.get(i).objective_values);
        Plot example2 = new Plot( "next gen",
                "X Axiz", "Y Axis"  ,plotdata2 );
    }
}
