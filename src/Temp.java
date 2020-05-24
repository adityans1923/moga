import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Temp {
    private Random rand;
    public final int obj_count  = Constant.obj_count;
    public final int population_size = Constant.population_size;
    public final int chromosome_size = Constant.chromosome_size;
    public ArrayList<Chromosome> curr_pop;
    public ArrayList<Chromosome> next_pop;
    public int repeated_gen_count = 0;

    Temp(){
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
        }
    }

    public boolean someone_dominates(ArrayList<Integer> arr, Integer x){
        for(int i=0;i<arr.size();i++)
            if(this.curr_pop.get(arr.get(i)).dominates(this.curr_pop.get(x)))
                return true;
        return false;
    }
    ArrayList<ArrayList<Integer> > non_dominating_sort_v2(){
        ArrayList<ArrayList<Integer>> dominated_front = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> domination_arr = new ArrayList<>();
        for(int i = 0;i < curr_pop.size(); i++){
            domination_arr.add(i);
        }
        domination_arr.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                Double a=0.0,b=0.0;
                for(int i=0;i<obj_count;i++){
                    if(!curr_pop.get(integer).objective_values.get(i).equals(curr_pop.get(t1).objective_values.get(i))){
                        a = curr_pop.get(integer).objective_values.get(i);
                        b = curr_pop.get(t1).objective_values.get(i);
                        break;
                    }
                }
                if(a < b) return -1;
                else if(a > b)    return 1;
                return 0;
            }
        });

        dominated_front.add(new ArrayList<>());
        dominated_front.get(0).add(domination_arr.get(0));
        int size = domination_arr.size();
        for(int j = 1;j < size ; j++){
            int front = 0;
            while(front < dominated_front.size() && this.someone_dominates(dominated_front.get(front),domination_arr.get(j)))
                front++;
            if(front == dominated_front.size())
                dominated_front.add(new ArrayList<>());
            dominated_front.get(front).add(domination_arr.get(j));
        }
        return dominated_front;
    }

    ArrayList<ArrayList<Integer> > non_dominating_sort_v3(){
        ArrayList<ArrayList<KdTree> > dominated_front = new ArrayList<>();
        ArrayList<ArrayList<Integer> > tt = new ArrayList<>();
        ArrayList<Integer> domination_arr = new ArrayList<>();
        for(int i = 0; i < this.curr_pop.size(); i++){
            domination_arr.add(i);
        }
        domination_arr.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                Double a=0.0,b=0.0;
                for(int i=0;i<obj_count;i++){
                    if(!curr_pop.get(integer).objective_values.get(i).equals(curr_pop.get(t1).objective_values.get(i))){
                        a = curr_pop.get(integer).objective_values.get(i);
                        b = curr_pop.get(t1).objective_values.get(i);
                        break;
                    }
                }
                if(a < b) return -1;
                else if(a > b)    return 1;
                return 0;
            }
        });

        tt.add(new ArrayList<>());
        tt.get(0).add(domination_arr.get(0));

        dominated_front.add(new ArrayList<>());
        for(int i = 0; i < this.obj_count; i++){
            dominated_front.get(0).add( new KdTree(domination_arr.get(0), i) );
        }
        int size = domination_arr.size();
        for(int j = 1;j <  size; j++){
            int front = 0;
            while(front < dominated_front.size() && this.isany_dominates(dominated_front.get(front),domination_arr.get(j)))
                front++;
            if(front == dominated_front.size()){
                tt.add(new ArrayList<>());
                dominated_front.add(new ArrayList<>());
                for(int i = 0; i < this.obj_count; i++){
                    dominated_front.get(front).add(new KdTree(domination_arr.get(j), i));
                }
            }else{
                for(int i = 0;i < this.obj_count; i++){
                    dominated_front.get(front).get(i).add(domination_arr.get(j));
                }
            }
            tt.get(front).add(domination_arr.get(j));
        }
        return tt;
    }
    public boolean isany_dominates(ArrayList<KdTree> arr, Integer x){
        for(int i = 0; i < this.obj_count; i++){
            if(arr.get(i).isAnyDominating(x))
                return true;
        }
        return false;
    }

    public static void main(String[] args){
        System.out.println("hello");
        Temp obj = new Temp();

        for(int i=0;i<obj.population_size;i++) {
            int index1 = obj.rand.nextInt(obj.population_size);
            int index2 = obj.rand.nextInt(obj.population_size);
            while (index2 == index1)
                index2 = obj.rand.nextInt(obj.population_size);
            Chromosome child = obj.curr_pop.get(index1).crossover(obj.curr_pop.get(index2));
            if (obj.curr_pop.contains(child)) {
//                System.out.println("duplidate child produced: \n"+child.data);
                continue;
            }
            child.mutate();
            child.calculate_objective_value();
            // the below condition improves output by reducing the range of solution
            if (!(child.dominates(obj.curr_pop.get(index1)) || child.dominates(obj.curr_pop.get(index2)))) {
//                System.out.println("Child does not dominates there parents");
                continue;
            }
            obj.curr_pop.add(child);
        }
        // sorting based on dominatinos and return all the list of dominated fronts
//        for(int i=0;i<obj.curr_pop.size();i++)
//            obj.curr_pop.get(i).display();

        KdTree.init();
        KdTree.curr_pop = obj.curr_pop;
        System.out.println("Kd tree pos : "+ KdTree.pos);

        long startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_fronts = obj.non_dominating_sort_v3();
        long endTime = System.nanoTime();
        System.out.println("That took v3 " + (endTime - startTime) + " Nanoseconds");
        long diff = endTime-startTime;


        //extra
        startTime = System.nanoTime();
        ArrayList<ArrayList<Integer> > dominated_frontv = obj.non_dominating_sort_v2();
        endTime = System.nanoTime();
        System.out.println("That took v2 " + (endTime - startTime) + " Nanoseconds");
        if(diff > endTime-startTime)
            System.out.println("winner is v2  : "+diff*1.00/(endTime-startTime)*1.00);
        else System.out.println("Winner is v3 :  "+(endTime-startTime)*1.00/diff);
        System.out.println();

        System.out.println("Comparing both method for same result");
        System.out.println(dominated_fronts.size() + " --- "+dominated_frontv.size());
        System.out.println("domination array v3");
        for(int i=0;i<dominated_fronts.size();i++){
            Collections.sort(dominated_fronts.get(i));
//            System.out.println(i+" -- "+dominated_fronts.get(i));
        }
        System.out.println("DOmination mine v2");
        for(int i=0;i<dominated_frontv.size();i++){
            Collections.sort(dominated_frontv.get(i));
//            System.out.println(i+" -- "+dominated_frontv.get(i));
        }

    }
}
