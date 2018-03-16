package com.example.android.group4.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.group4.MainActivity;
import com.example.android.group4.R;
import com.example.android.group4.db.DBHelper;
import com.example.android.group4.models.AccelerometerDatum;
import com.jjoe64.graphview.series.DataPoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jaydeep on 3/15/18.
 */

public class NetworkUtil {


    public static void downloadFile(IDownloaderListener mListener){
//        AsyncDownloadFile upTask = new AsyncDownloadFile(mListener);
//        upTask.execute();
    }

    public interface IDownloaderListener{
        void progressUpdate(String[] values);
        void downloadComplete(List<AccelerometerDatum> accelerometerData);
        void downloadFailed();
    }

    public static void uploadFile(IUploaderListener mListener){
        AsyncUploadFile upTask = new AsyncUploadFile(mListener);
        upTask.execute();
    }

    public interface IUploaderListener{
        void progressUpdate(String[] values);
        void uploadComplete(int resultCode);
        void uploadFailed(int resultCode);
    }

    private static class AsyncDownloadFile extends AsyncTask<Void, String, Void>  //returns exception strings to onProgressUpdate
    {
        boolean flag = false;   //flag to check for any exception
        ProgressDialog waitDialog;  //progress dialog to disable activity during download

        AsyncDownloadFile(IDownloaderListener mListener){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            waitDialog = ProgressDialog.show(MainActivity.this,"","Wait until download finishes",true);
        }

        protected Void doInBackground(Void... params) {
            try {
                File downloadedFile1 = new File(android.os.Environment.getExternalStorageDirectory(), Constants.DownloadDir);   //create SD path based on device with /CSE535_ASSIGNMENT2_Extra/ folder
                if(!downloadedFile1.isDirectory())
                    downloadedFile1.mkdir();    //create directory if none exists


                URL urlPath = new URL(Constants.SERVER_DB_PATH);  //create URL object of server path
                HttpURLConnection urlConnect = (HttpURLConnection) urlPath.openConnection();    //create object to establish http connection
                int contentLength = urlConnect.getContentLength();  //get length of the file to be downloaded
                DataInputStream iStream = new DataInputStream(urlPath.openStream());    //new input stream to save buffer of downloaded file
                byte[] buffer = new byte[contentLength];    //buffer size is size of file
                //System.out.println(contentLength);

                int length;

                File downloadedFile = new File(downloadedFile1, DBHelper.dbFileName);  //create complete path for file
                FileOutputStream fStream = new FileOutputStream(downloadedFile);    //output stream to write from buffer and save as file
                DataOutputStream oStream = new DataOutputStream(fStream);

                //transfer from input stream to output stream using buffer
                while ((length = iStream.read(buffer)) != -1) {
                    oStream.write(buffer, 0, length);
                }

                //close streams
                iStream.close();
                oStream.flush();
                oStream.close();
            }

            catch (FileNotFoundException e)
            {
                flag = true;    //flag true if there is exception
                System.out.println(e.getMessage());
                publishProgress(e.getMessage());
            }
            catch (MalformedURLException e)
            {
                flag = true;
                publishProgress(e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e)
            {
                flag = true;
                publishProgress(e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... value) {
            super.onProgressUpdate(value);
            //show message if there exception flag is set
            if(flag)
                NotificationUtil.makeAToast("ERROR:  "+value[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            waitDialog.dismiss();   //remove dialog after download completes

            //show message if download is successful
            if(!flag)
                NotificationUtil.makeAToast("Download completed");
//
//            db1 = SQLiteDatabase.openOrCreateDatabase(android.os.Environment.getExternalStorageDirectory() + downloadDir + fileName, null);
//            db1.beginTransaction();
//
//            Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//            c.moveToLast();
//            tableName = c.getString(0);
//
//            String [] tabNameSplit = tableName.split("_");
//
//            nameString.setText(tabNameSplit[0]);
//            idValue.setText(tabNameSplit[1]);
//            ageValue.setText(tabNameSplit[2]);
//
//            RadioButton maleRadioButton, femaleRadioButton;
//            maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
//            femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);
//            if(tabNameSplit[3].equalsIgnoreCase("male"))
//            {
//                maleRadioButton.setChecked(true);
//                femaleRadioButton.setChecked(false);
//                patientSex = maleRadioButton.getText().toString();
//            }
//            else
//            {
//                maleRadioButton.setChecked(false);
//                femaleRadioButton.setChecked(true);
//                patientSex = femaleRadioButton.getText().toString();
//            }
//
//            patientName=nameString.getText().toString(); //Receives patient info
//            patientId=Integer.parseInt(idValue.getText().toString());
//            patientAge=Integer.parseInt(ageValue.getText().toString());
//
//
//
//
//
//            final String TABLE_NAME = tableName;
//            String selectQuery = "SELECT  * FROM " + TABLE_NAME;  //selects entries from table
//            Cursor cursor = db1.rawQuery(selectQuery, null);
//            db1.endTransaction();
//
//            if (cursor.moveToFirst())
//            {
//                do
//                {
//
//                    xValues.add(Float.parseFloat(cursor.getString(1)));
//                    yValues.add(Float.parseFloat(cursor.getString(2)));
//                    zValues.add(Float.parseFloat(cursor.getString(3)));
//
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//
//            final Timer timer = new Timer();
//            timer.scheduleAtFixedRate( new TimerTask() {
//                public void run() {                             //plots graph from db
//
//                    if(traverseEleCount<xValues.size())
//                    {
//
//                        series1.appendData(new DataPoint(graph2LastXValue, xValues.get(traverseEleCount)), true, 15);
//                        series2.appendData(new DataPoint(graph2LastXValue, yValues.get(traverseEleCount)), true, 15);
//                        series3.appendData(new DataPoint(graph2LastXValue, zValues.get(traverseEleCount)), true, 15);
//                        graph2LastXValue += 1d;
//
//                        traverseEleCount++;
//                    }
//                    else if(traverseEleCount != 0 && traverseEleCount>=xValues.size())
//                    {
//                        xValues.clear();
//                        yValues.clear();
//                        zValues.clear();
//                        traverseEleCount=0;
//
//                        clearEditTexts=true;
//
//                        timer.cancel();
//                        timer.purge();
//                    }
//                }
//            }, 0, 1000);

        }
    }


    //Upload
    private static class AsyncUploadFile extends AsyncTask<Void, String, Void>
    {
        int flag = 0;
        ProgressDialog waitDialog;
        //strings to create multipart format message
        String nl = "\r\n";
        String hyphen = "--";
        String boundary =  "*****";
        IUploaderListener mListener;

        AsyncUploadFile(IUploaderListener mListener){
            this.mListener = mListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            waitDialog = ProgressDialog.show(MainActivity.this,"","Wait till upload completes",true);
        }

        protected Void doInBackground(Void... params) {
            try {
                File fileToUpload = new File(DBHelper.getDBFilePath()); //get database file from storage
                URL upPath = new URL(Constants.PHP_PATH);
                HttpURLConnection urlConnect = (HttpURLConnection) upPath.openConnection(); //create http object
                urlConnect.setDoOutput(true);   //flag to set server to be written to using output stream
                urlConnect.setDoInput(true);
                urlConnect.setUseCaches(false);
                urlConnect.setRequestMethod("POST");    //send data using POST
                urlConnect.setRequestProperty("Connection", "Keep-Alive");  //keep connection without disconnecting
                urlConnect.setRequestProperty("ENCTYPE", "multipart/form-data");    //send multipart format data to server
                urlConnect.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                urlConnect.setRequestProperty("uploaded_file", String.valueOf(fileToUpload));   //To be sent to php code for setting file path
                DataOutputStream upStream = new DataOutputStream(urlConnect.getOutputStream());

                //open output stream in server
                upStream.writeBytes(hyphen + boundary + nl);    //start the message with boundary
                upStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + String.valueOf(fileToUpload) + "\""+nl);   //write the type of data with filename and path
                upStream.writeBytes(nl); //new line which is followed by the actual file contents

                FileInputStream fStream = new FileInputStream(fileToUpload);
                DataInputStream readStream = new DataInputStream(fStream);

                int length;
                byte[] buffer = new byte[4096];
                while ((length = readStream.read(buffer)) != -1) {
                    upStream.write(buffer, 0, length);
                }

                //after file is written, finish the message with boundary
                upStream.writeBytes(nl);
                upStream.writeBytes(hyphen + boundary + hyphen + nl);

                //System.out.println(urlConnect.getResponseCode());
                //To check success response from server
                if(urlConnect.getResponseCode() == 200)
                {
                    flag = 1;
                }

                //close streams
                readStream.close();
                fStream.close();
                upStream.close();

            }
            catch (MalformedURLException ex)
            {
                flag = 2;
                publishProgress(ex.getMessage());
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (FileNotFoundException nf)
            {
                flag = 2;
                publishProgress(nf.getMessage());
            } catch (IOException ioe)
            {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                flag = 2;
                publishProgress(ioe.getMessage());
            }


            return null;
        }

        protected void onProgressUpdate(String... value) {
            super.onProgressUpdate(value);
            //display message if there is any exception
            mListener.progressUpdate(value);
            if(flag == 2)
                NotificationUtil.makeAToast("ERROR: "+ value[0]);
        }


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            waitDialog.dismiss();
            //display if upload is success
            if(flag == 1){
                NotificationUtil.makeAToast("Upload completed");
                mListener.uploadComplete(200);
            }

        }
    }
}
