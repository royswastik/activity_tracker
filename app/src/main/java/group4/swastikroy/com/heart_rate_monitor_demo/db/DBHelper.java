package group4.swastikroy.com.heart_rate_monitor_demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerAction;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;


public class DBHelper extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SVM";

    // Contacts table name
    private static final String TABLE_NAME = "accelerometer_data_table";

    SQLiteDatabase db = null;

    // Contacts Table Columns names
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String ACTION = "action";
    private static String DB_ABSOLUTE_PATH = "";
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (this.db == null) {
            this.db = db;
        }
    }

    public String getAbsPathToDb() throws SQLException {
        DB_ABSOLUTE_PATH = db.getPath();
        return DB_ABSOLUTE_PATH;
    }

    public void createTable() {
        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + X + " REAL,"
                + Y + " REAL," + Z + " REAL," + ACTION + " TEXT" + ")";
        getWritableDatabase().execSQL(CREATE_TABLE);
        Log.d("Table created DB", CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);*/
    }

    public void insertAccelerometerDataList(List<AccelerometerDataPoint> list, String action) {
        Log.d("INSERTING...", "DB");
        SQLiteDatabase db = this.getWritableDatabase();
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
        db = this.getWritableDatabase();
        String query = "DELETE FROM accelerometer_data_table;";
        db.execSQL(query);
    }

    public List<AccelerometerAction> getData(String activity) {

        List<AccelerometerAction> actionList = new ArrayList<>();

        db = this.getWritableDatabase();

        try {
            String query = "SELECT * FROM accelerometer_data_table WHERE action = '" + activity + "' LIMIT 1000;";

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

    public JSONArray getDataForGraph(String activity) {

        List<AccelerometerAction> dataList = getData(activity);

        JSONArray activities = new JSONArray();

        for (int i = 0; i < 20; i++) {
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < 10; j++) {
                try {
                    jsonArray.put(dataList.get((i * 50) + j).getX());
                    jsonArray.put(dataList.get((i * 50) + j).getY());
                    jsonArray.put(dataList.get((i * 50) + j).getZ());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            activities.put(jsonArray);
        }

        return activities;

    }
}
