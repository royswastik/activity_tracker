package swastikroy.com.mobile_computing_assignment1.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import swastikroy.com.mobile_computing_assignment1.models.HealthDatum;

/**
 * Created by Swastik on 1/22/2018.
 */
public class RandomHealthDataGenerator {

    public static Random random = new Random();

    public static int getRandomHealthData(int min, int max){
        int i1 = random.nextInt(max - min) + min;
        return i1;
    }

    public static Float[] generate_array_of_float(int count, float max, float min){
        Float[] arr = new Float[count];
        for(int i = 0; i < count ; i++){
            arr[i] = random.nextFloat()*(max-min)+min;
        }
        return arr;
    }

    public static List<HealthDatum> get_dummy_health_data(int count, float max, float min){
        Float[] random_float_arr = generate_array_of_float(count, max, min);
        List<HealthDatum> healthDataList = new ArrayList<>();
        for(int i = 0; i < random_float_arr.length; i++){
            healthDataList.add(new HealthDatum(i, random_float_arr[i].doubleValue()));
        }
        return healthDataList;
    }

}
