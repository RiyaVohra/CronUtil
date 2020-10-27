import javafx.util.Pair;
import java.util.HashSet;
import java.util.Set;

public class cronChecker {

    private static Set<Pair<String,Integer>> cronset = new HashSet<>();
    private static int[] firstchar=new  int[5];
    private static int[] lastchar = new int[5];
    private static int[] inc = new int[5];
    private static int[] maxrange = new int[]{60,24,31,12,7};
    private static int[] minrange = new int[]{0,0,1,1,0};

    private static boolean hourdiff(String fifthString, int hour) {
        int first;
        for(Pair<String,Integer> p : cronset) {
            String[] s = fifthString.split(" ");
            int newfirst = Integer.parseInt(s[0]);
            int pos = fifthString.indexOf(Integer.toString(hour), 2);
            String newthird = fifthString.substring(pos);

            if (p.getValue() == hour || p.getValue() == hour + 1 || p.getValue() == hour - 1) {
                String[] str = p.getKey().split(" ");
                first = Integer.parseInt(str[0]);
                if (p.getValue() == hour) {
                    pos = p.getKey().indexOf(Integer.toString(hour), 2);
                    String third = p.getKey().substring(pos + 3);
                    return !newthird.contains(third);
                } else {
                    int hourval = p.getValue();
                    pos = p.getKey().indexOf(Integer.toString(hourval), 2);
                    String third = p.getKey().substring(pos + 3);
                    boolean condition = (hourval > hour) ? (first + 60 - newfirst >= 60) : (newfirst + 60 - first >= 60);
                    return !newthird.contains(third) || condition;
                }
            }
        }return true;
    }

    private static void cronArrayGenerator(String[] cronArray) {
        int k = 0;
        for (String temp : cronArray) {
            if (temp.contains("/")) {
                String[] numbers = temp.split("/");
                if (numbers[0].equals("*")) {
                    firstchar[k] = minrange[k];
                    inc[k] = 1;
                    lastchar[k] = maxrange[k];
                } else {
                    firstchar[k] = Integer.parseInt(numbers[0]);
                    inc[k] = Integer.parseInt(numbers[1]);
                    lastchar[k] = maxrange[k];
                }
            } else if (temp.contains("-")) {
                String[] numbers;
                numbers = temp.split("-") ;
                firstchar[k] = Integer.parseInt(numbers[0]);
                inc[k] = 1;
                lastchar[k] = Integer.parseInt(numbers[1]);
            } else if(temp.contains(",")){
                String[] numbers;
                numbers = temp.split(",") ;
                firstchar[k] = Integer.parseInt(numbers[0]);
                inc[k] = Integer.parseInt(numbers[1])-Integer.parseInt(numbers[0]);
                lastchar[k] = Integer.parseInt(numbers[1]);

            } else if(temp.equals("*")) {
                firstchar[k] = minrange[k];
                inc[k] = 1;
                lastchar[k] = maxrange[k];
            } else{
                firstchar[k] = Integer.parseInt(temp);
                lastchar[k] = firstchar[k];
                inc[k] = 1;
            }
            k++;
        }
    }

    private static void cronsetGenerator(int k,String str,int hour){
        if(k==4){
            while(firstchar[k]<=lastchar[k]){
                String temp= Integer.toString(firstchar[k]);
                String fifthString = str + temp + " ";
                if (cronset.contains(new Pair<>(fifthString, hour)))
                    System.out.println("Same Cron already exist ,there should be at least 1 hour diff");
                else if (!hourdiff(fifthString, hour))
                    System.out.println("There should be at least 1 hour difference");
                else
                    cronset.add(new Pair<>(fifthString, hour));
                firstchar[k]+=inc[k];
            }return;
        }
        while(firstchar[k]<=lastchar[k]){
            if(k==1) hour=firstchar[k];
            String t=str;
            t += firstchar[k] + " ";
            cronsetGenerator(k+1,t,hour);
            firstchar[k]=firstchar[k]+inc[k];
        }
    }

    public static void main(String[] args) {
        String[] cronexpression =new String[]{"0 2 28,29 * *","4 22 5,7 * *","0 2 28 * *"};
        for (String s : cronexpression) {
            String[] cronArray = s.split("\\s+");
            cronArrayGenerator(cronArray);
            cronsetGenerator(0,"",-1);
       }
    }
}
