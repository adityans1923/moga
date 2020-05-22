import java.io.*;
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
    public int repeated_gen_count = 0;

    MOGA(){
        // initialize variables
        curr_pop = new ArrayList<Chromosome>(2* this.population_size + 1);
        rand  = new Random();
        // initializing population
        for(int i=0;i< this.population_size; i++){
            Chromosome new_sol = new Chromosome(chromosome_size);
            new_sol.calculate_objective_value();
            if(this.curr_pop.contains(new_sol)){
                i--;
                continue;
            }
            this.curr_pop.add(new_sol);
//            new_sol.display();
        }
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort() {
        // sort according to the domination into the fronts
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> sol_dom = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> dom_by_count = new ArrayList<Integer>();

        for (int i = 0; i < curr_pop.size(); i++) {
            sol_dom.add(new ArrayList<Integer>());
            dom_by_count.add(0);
            for (int j = 0; j < curr_pop.size(); j++) {
                if (this.curr_pop.get(i).dominates(this.curr_pop.get(j)))
                    sol_dom.get(i).add(j);
                else if (this.curr_pop.get(j).dominates(this.curr_pop.get(i)))
                    dom_by_count.set(i, dom_by_count.get(i) + 1);
            }
            if (dom_by_count.get(i) == 0) {
                if (dominated_front.size() == 0)
                    dominated_front.add(new ArrayList<Integer>());
                dominated_front.get(0).add(i);
            }
        }
        int front_count = 0;
        while (dominated_front.get(front_count).size() != 0) {
            ArrayList<Integer> next_front = new ArrayList<Integer>();
            for (int i = 0; i < dominated_front.get(front_count).size(); i++) {
                for (int j = 0; sol_dom.get(dominated_front.get(front_count).get(i)).size() > j; j++) {
                    int cnt = dom_by_count.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                    cnt--;
                    dom_by_count.set(sol_dom.get(dominated_front.get(front_count).get(i)).get(j), cnt);
                    if (cnt == 0) {
                        next_front.add(sol_dom.get(dominated_front.get(front_count).get(i)).get(j));
                        this.curr_pop.get(sol_dom.get(dominated_front.get(front_count).get(i)).get(j)).setRank(front_count + 1);
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
                    if(dd<0) return 1;
                    else if(dd > 0) return -1;
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
            Chromosome child = this.curr_pop.get(index1).crossover(this.curr_pop.get(index2));
            if(this.curr_pop.contains(child))
            {
//                System.out.println("duplidate child produced: \n"+child.data);
                continue;
            }
            child.mutate();
            child.calculate_objective_value();
            // the below condition improves output by reducing the range of solution
            if(!(child.dominates(this.curr_pop.get(index1) ) || child.dominates(this.curr_pop.get(index2)))){
//                System.out.println("Child does not dominates there parents");
                continue;
            }
            this.curr_pop.add(child);
        }
        // when no new child was able to live in current generation
        if(this.curr_pop.size() == this.population_size){
            this.repeated_gen_count++;
            this.next_pop = this.curr_pop;
//            System.out.println("Redundant calling of next generation");
//            next_generation();
            return;
        }
        this.repeated_gen_count = 0;
        // sorting based on dominatinos and return all the list of dominated fronts
        ArrayList<ArrayList<Integer> > dominated_fronts = this.non_dominating_sort();

        // now creating new population from the dominated fronts list using crowding distance
        this.next_pop = new ArrayList<Chromosome>(this.population_size * 2 + 1);
        int index = 0;
        // <= doesnt work because when the size if exactly 100 then it casuing index out of range
        while(this.next_pop.size() + dominated_fronts.get(index).size() < this.population_size){
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
    ArrayList<Chromosome> readObject(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream= new ObjectInputStream(new FileInputStream("./database/gen_"+filename));
        ArrayList<Chromosome> hh = (ArrayList<Chromosome>)inputStream.readObject();
        inputStream.close();
        return hh;
    }
    void writeObject(String filename, ArrayList<Chromosome> obj) throws IOException{
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./database/gen_"+filename));
        outputStream.writeObject(obj);
        outputStream.flush();
        outputStream.close();
    }

    public static void main(String[] args){
        System.out.println("hello");
        MOGA obj = new MOGA();
        ArrayList<ArrayList<Double> > idat = new ArrayList< ArrayList<Double> >();
        for(int i=0;i<obj.population_size;i++)
            idat.add(obj.curr_pop.get(i).objective_values);
        Plot initialChart = new Plot( "Initial Gen","obj1", "obj2"  ,idat );
        for(int i=0;i<Constant.generation_count;i++){
            System.out.println("Generation : "+i);
            obj.next_generation();
            ArrayList<ArrayList<Double> > plotdata = new ArrayList< ArrayList<Double> >();
            for(int j=0;j<obj.population_size;j++)
                plotdata.add(obj.next_pop.get(j).objective_values);
            obj.curr_pop = obj.next_pop;
            obj.next_pop = new ArrayList<>(2*Constant.population_size + 1);
            try{
                obj.writeObject(Integer.toString(i), obj.curr_pop);
            }catch (Exception e){
                System.out.println("Error in writing data of GENERAATION : "+e.getMessage());
            }
            if(i == Constant.generation_count - 1 || obj.repeated_gen_count == 10){
                Plot example = new Plot( "Gen: "+i,"obj1", "obj2"  ,plotdata );
                for(int j=0;j<100;j++)
                    obj.curr_pop.get(j).display();
                return ;
            }
        }
    }
}
