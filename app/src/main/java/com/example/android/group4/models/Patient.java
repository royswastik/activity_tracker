package com.example.android.group4.models;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by sroy41 on 2/6/2018.
 */

public class Patient implements Serializable {


    String id;
    String name;
    String age;
    String sex;
    ArrayList<AccelerometerDatum> acc_val;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<AccelerometerDatum> getAcc_val() {
        return acc_val;
    }

    public void setAcc_val(ArrayList<AccelerometerDatum> acc_val) {
        this.acc_val = acc_val;
    }
}
