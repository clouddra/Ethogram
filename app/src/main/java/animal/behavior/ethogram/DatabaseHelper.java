package animal.behavior.ethogram;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
    private static final int NUM_OF_COLS = 5;   // exclusive of id
    private static final String ID = "id";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String TIME_TAKEN = "time_taken";
    private static final String BEHAVIOR = "behavior";
    private static final String NOTES = "notes";

    // sqlite code
    private static final String ETHO_ENTRIES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ENTRIES + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    START_TIME + " INTEGER, " +
                    END_TIME + " INTEGER, " +
                    TIME_TAKEN + " INTEGER, " +
                    BEHAVIOR + " TEXT, " +
                    NOTES + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ETHO_ENTRIES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public long insertEntry(long startTime, long endTime, String behavior, String note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.START_TIME, startTime);
        values.put(this.END_TIME, endTime);
        values.put(this.TIME_TAKEN, endTime-startTime);
        if(behavior == null)
            values.put(this.BEHAVIOR, "");
        else
            values.put(this.BEHAVIOR, behavior);
        if(note == null)
            values.put(this.NOTES, "");
        else
            values.put(this.NOTES, note);

        Log.i("db", "inserting entry " + startTime + " " + endTime + " into db...");

        long id = db.insert(TABLE_ENTRIES, null, values);
        db.close();
        return id;
    }

    public void updateBehavior(long id, String behavior){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.BEHAVIOR, behavior);

        db.update(this.TABLE_ENTRIES, values, "id = ?", new String [] {String.valueOf(id)});
        db.close();
    }

    public void updateNote(long id, String note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(this.NOTES, note);

        db.update(this.TABLE_ENTRIES, values, "id = ?", new String [] {String.valueOf(id)});
        db.close();
    }

    public String getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + ID + " = " + String.valueOf(id), null);
        if(c != null){
            c.moveToFirst();
            String note = c.getString(c.getColumnIndex(NOTES));
            c.close();
            db.close();
            return note;
        }
        else{
            c.close();
            db.close();
            return null;
        }
    }

    public Entry getEntry(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c= db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + ID + " = " + String.valueOf(id), null);

        if(c != null){
            c.moveToFirst();
            Entry entry = new Entry();
            entry.setId(c.getLong(c.getColumnIndex(ID)));
            entry.setStartTime(c.getLong(c.getColumnIndex(START_TIME)));
            entry.setEndTime(c.getLong(c.getColumnIndex(END_TIME)));
            entry.setTimeTaken(c.getLong(c.getColumnIndex(TIME_TAKEN)));
            entry.setBehavior(c.getString(c.getColumnIndex(BEHAVIOR)));
            entry.setNote(c.getString(c.getColumnIndex(NOTES)));

            c.close();
            db.close();
            return entry;
        }
        else{
            c.close();
            db.close();
            return null;
        }
    }

    public List<Entry> getAllCommitted(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE "  + BEHAVIOR + " != ''", null);
        List<Entry> list = new ArrayList<Entry>();
        Log.i("db", "retrieving all committed entries...");

        if(c != null){
            while(c.moveToNext()){
                Entry entry = new Entry();
                entry.setId(c.getLong(c.getColumnIndex(ID)));
                entry.setStartTime(c.getLong(c.getColumnIndex(START_TIME)));
                entry.setEndTime(c.getLong(c.getColumnIndex(END_TIME)));
                entry.setTimeTaken(c.getLong(c.getColumnIndex(TIME_TAKEN)));
                entry.setBehavior(c.getString(c.getColumnIndex(BEHAVIOR)));
                entry.setNote(c.getString(c.getColumnIndex(NOTES)));

                list.add(entry);
                Log.i("db", entry.toString());
            }
        }

        db.close();
        return list;
    }

    public List<Entry> getAllUncommitted(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + BEHAVIOR + " IS NULL OR " + BEHAVIOR + " == ''", null);
        List<Entry> list = new ArrayList<Entry>();
        Log.i("db", "retrieving all uncommitted entries...");

        if(c != null){
            while(c.moveToNext()){
                Entry entry = new Entry();
                entry.setId(c.getLong(c.getColumnIndex(ID)));
                entry.setStartTime(c.getLong(c.getColumnIndex(START_TIME)));
                entry.setEndTime(c.getLong(c.getColumnIndex(END_TIME)));
                entry.setTimeTaken(c.getLong(c.getColumnIndex(TIME_TAKEN)));
                entry.setBehavior(c.getString(c.getColumnIndex(BEHAVIOR)));
                entry.setNote(c.getString(c.getColumnIndex(NOTES)));


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
        csvHeader = "\"" + START_TIME + "\"," +  "\"" + END_TIME + "\"," + "\"" + TIME_TAKEN + "\"," + "\"" + BEHAVIOR + "\"" + NOTES + "\"\n";

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
        }
        catch(Exception ex){
            ex.printStackTrace();
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
                csvValues += "\"" + String.valueOf(c.getString(c.getColumnIndex(NOTES))) + "\"";
                csvValues += "\n";
            }
            c.close();
        }

        Log.i("db", csvValues);
        return csvValues;
    }
}
