package animal.behavior.ethogram;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pohchiat on 2/17/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // constants for sqlite
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "EthoEntries";
    private static final String TABLE_ENTRIES = "Entries";

    // database column names
    private static final int NUM_OF_COLS = 4;   // exclusive of id
    private static final String ID = "id";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String TIME_TAKEN = "time_taken";
    private static final String BEHAVIOR = "behavior";
    private Context context;

    // sqlite code
    private static final String ETHO_ENTRIES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ENTRIES + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    START_TIME + " INTEGER, " +
                    END_TIME + " INTEGER, " +
                    TIME_TAKEN + " INTEGER, " +
                    BEHAVIOR + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ETHO_ENTRIES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public long insertEntry(long startTime, long endTime, String behavior){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.START_TIME, startTime);
        values.put(this.END_TIME, endTime);
        values.put(this.TIME_TAKEN, endTime-startTime);
        if(behavior == null)
            values.putNull(this.BEHAVIOR);
        else
            values.put(this.BEHAVIOR, behavior);

        Log.i("db", "inserting entry " + startTime + " " + endTime + " into db...");

        long id = db.insert(TABLE_ENTRIES, null, values);
        db.close();
        return id;
    }

    public void updateBehavior(long id, String behavior){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.BEHAVIOR, behavior);

        db.update(this.TABLE_ENTRIES, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Entry> getAllCommitted(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + BEHAVIOR + " NOT NULL", null);
        List<Entry> list = new ArrayList<Entry>();
        Log.i("db", "retriving all committed entries...");

        if(c != null){
            while(c.moveToNext()){
                Entry entry = new Entry();
                entry.setId(c.getLong(c.getColumnIndex(ID)));
                entry.setStartTime(c.getLong(c.getColumnIndex(START_TIME)));
                entry.setEndTime(c.getLong(c.getColumnIndex(END_TIME)));
                entry.setTimeTaken(c.getLong(c.getColumnIndex(TIME_TAKEN)));
                entry.setBehavior(c.getString(c.getColumnIndex(BEHAVIOR)));

                list.add(entry);
                Log.i("db", entry.toString());
            }
        }

        db.close();
        return list;
    }

    public List<Entry> getAllUncommitted(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + BEHAVIOR + " IS NULL", null);
        List<Entry> list = new ArrayList<Entry>();
        Log.i("db", "retriving all uncommitted entries...");

        if(c != null){
            while(c.moveToNext()){
                Entry entry = new Entry();
                entry.setId(c.getLong(c.getColumnIndex(ID)));
                entry.setStartTime(c.getLong(c.getColumnIndex(START_TIME)));
                entry.setEndTime(c.getLong(c.getColumnIndex(END_TIME)));
                entry.setTimeTaken(c.getLong(c.getColumnIndex(TIME_TAKEN)));
                entry.setBehavior(c.getString(c.getColumnIndex(BEHAVIOR)));

                list.add(entry);
                Log.i("db", entry.toString());
            }
        }

        db.close();
        return list;
    }

    public void exportToFile(){
        String csvHeader = "";
        String csvValues = getAllRowsInCSV();
        csvHeader = "\"" + START_TIME + "\"," +  "\"" + END_TIME + "\"," + "\"" + TIME_TAKEN + "\"," + "\"" + BEHAVIOR + "\"\n";

        // write to file
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ethogramResults");
        myDir.mkdirs();

        String filename = "csv-"+ System.nanoTime() +".csv";
        File file = new File (myDir, filename);

        try{
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fileWriter);

            out.write(csvHeader);
            out.write(csvValues);
            Log.i("db", "wrote to file: " + filename);

            out.close();
            Toast.makeText(context, "Export Success", Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, "Export Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public String getAllRowsInCSV(){
        String csvValues = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_ENTRIES, null);

        if(c != null){
            while(c.moveToNext()){
                csvValues += String.valueOf(c.getLong(c.getColumnIndex(START_TIME))) + ",";
                csvValues += String.valueOf(c.getLong(c.getColumnIndex(END_TIME))) + ",";
                csvValues += String.valueOf(c.getLong(c.getColumnIndex(TIME_TAKEN))) + ",";
                csvValues += "\"" + String.valueOf(c.getString(c.getColumnIndex(BEHAVIOR))) + "\"";
                csvValues += "\n";
            }
            c.close();
        }

        Log.i("db", csvValues);
        return csvValues;
    }
}
