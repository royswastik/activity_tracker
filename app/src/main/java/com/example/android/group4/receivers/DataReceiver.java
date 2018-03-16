package com.example.android.group4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.group4.models.AccelerometerDatum;

/**
 * Created by jaydeep on 3/15/18.
 */

public class DataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        System.out.println("Called");
        AccelerometerDatum accelerometerDatum = (AccelerometerDatum) intent.getSerializableExtra("data");
    } ;

//    public abstract void received(AccelerometerDatum accelerometerDatum);
}
