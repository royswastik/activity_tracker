package group4.swastikroy.com.heart_rate_monitor_demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerAction;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;

public class DBHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SVM";

    // Contacts table name
    private static final String TABLE_NAME = "accelerometer_data_table";

    //Trial Table
    private static final String TABLE_NAME_2 = "A_trial_table";
    SQLiteDatabase db2 = null;


    SQLiteDatabase db = null;

    // Contacts Table Columns names
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String ACTION = "action";
//    private static String DB_ABSOLUTE_PATH = "";
    Context context;


    public static String dbFileName = "Group4.db";  //databse name
    static String baseDbDir="/Android/Data/CSE535_ASSIGNMENT3";
    //Path where db to be created
    public static String dbFilePath = baseDbDir+"/"+dbFileName;

    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        if (this.db == null) {
//            this.db = db;
//        }
//    }

//    public String getAbsPathToDb() throws SQLException {
//        DB_ABSOLUTE_PATH = db.getPath();
//        return DB_ABSOLUTE_PATH;
//    }

    public void createTable() {
        checkDBFolder();    //This creates the database folder if not created

        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + X + " REAL,"
                + Y + " REAL," + Z + " REAL," + ACTION + " TEXT" + ")";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);
        db.execSQL(CREATE_TABLE);
        Log.d("Table created DB", CREATE_TABLE);
    }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);*/
//    }

    public void insertAccelerometerDataList(List<AccelerometerDataPoint> list, String action) {
        Log.d("INSERTING...", "DB");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);
        for (AccelerometerDataPoint point : list) {
            ContentValues values = new ContentValues();
            values.put(X, point.getX());
            values.put(Y, point.getY());
            values.put(Z, point.getZ());
            values.put(ACTION, action);
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public void cleardata() {
        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);
        String query = "DELETE FROM accelerometer_data_table;";
        db.execSQL(query);
    }



    public List<AccelerometerAction> getData(String action) {

        List<AccelerometerAction> actionList = new ArrayList<>();

        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);

        try {
            String query = "SELECT * FROM accelerometer_data_table WHERE action = '" + action + "' LIMIT 1000;";

            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                actionList.add(cursorToAA(cursor));
            }
            cursor.close();

            return actionList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("getData", "Error: .");
        }


        return actionList;
    }

    private AccelerometerAction cursorToAA(Cursor cursor) {
        AccelerometerAction aa = new AccelerometerAction();

        aa.setId(cursor.getInt(cursor.getColumnIndex("id")));
        aa.setX(cursor.getFloat(cursor.getColumnIndex("x")));
        aa.setY(cursor.getFloat(cursor.getColumnIndex("y")));
        aa.setZ(cursor.getFloat(cursor.getColumnIndex("z")));
        aa.setActionType(cursor.getString(cursor.getColumnIndex("action")));

        return aa;
    }

    public List<AccelerometerDataInstance> getInstanceData(String action) {

        List<AccelerometerAction> dataList = getData(action);

        List<AccelerometerDataInstance> adi = new ArrayList<>();

        List<Float> tempX = new ArrayList<>();
        List<Float> tempY = new ArrayList<>();
        List<Float> tempZ = new ArrayList<>();

        for(int i = 0;i <20 ; i++){
            adi.get(i).setActionType(dataList.get(i).getActionType());
            adi.get(i).setId(i);

                 for(int k = 0; k < 50;k++){
                    tempX.add(dataList.get(k).getX());
                    tempY.add(dataList.get(k).getY());
                    tempZ.add(dataList.get(k).getZ());
                 }
            adi.get(i).setX(tempX);
            adi.get(i).setY(tempY);
            adi.get(i).setZ(tempZ);

        }

       return adi;
    }


    public static int getCount(String action) {



        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);

        try {
            String query = "SELECT COUNT(*) FROM accelerometer_data_table WHERE action = '" + action + "' LIMIT 1000;";

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst(); //shouldnt it be last ?
            int result = cursor.getInt(0);
            cursor.close();

            return result/50;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("getData", "Error: .");
        }


        return 0;
    }

//    public JSONArray getDataAsJson(String action) throws JSONException {
//        List<AccelerometerAction> data = getData(action);
//        JSONArray mJSONArray = new JSONArray(Arrays.asList(data));
//        return mJSONArray;
//    }



    public JSONArray getDataForGraph(String action) {

        List<AccelerometerAction> dataList = getData(action);

        JSONArray activities = new JSONArray();

        for (int i = 0; i < 20; i++) {
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < 50; j++) {
                try {

                    if(((i * 50) + j) < dataList.size()){
                        jsonArray.put(dataList.get((i * 50) + j).getX());
                        jsonArray.put(dataList.get((i * 50) + j).getY());
                        jsonArray.put(dataList.get((i * 50) + j).getZ());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            activities.put(jsonArray);
        }

        return activities;

    }





    public static File checkDBFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() + baseDbDir);
        if(!folder.exists() || !folder.isDirectory()){
            boolean folderCreated = folder.mkdir();
            if(!folderCreated){ //Return if could not create folder
                Log.e("DB Folder", "Could not create DB Folder");
                throw new RuntimeException("Could not create DB Folder");
            }
        }
        return folder;
    }

//    public static String getDBFilePath(){
//        return android.os.Environment.getExternalStorageDirectory().toString() + dbFilePath;
//    }
//
//    public JSONObject cur2JsonObject(Cursor cursor, String keyColumn) throws JSONException {
//        JSONObject resultSet = new JSONObject();
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            int totalColumn = cursor.getColumnCount();
//            JSONObject rowObject = new JSONObject();
//            for (int i = 0; i < totalColumn; i++) {
//                if (cursor.getColumnName(i) != null) {
//                    try {
//                        rowObject.put(cursor.getColumnName(i),
//                                cursor.getString(i));
//                    } catch (Exception e) {
//                        Log.d("Cur2Json", e.getMessage());
//                    }
//                }
//            }
//            if(rowObject.has(keyColumn)){
//                resultSet.put(rowObject.getString(keyColumn), rowObject);
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return resultSet;
//    }
}
