package animal.behavior.ethogram;


import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class MainApplication extends Application
{
    public static List<String> behaviorCat;
//    public static List<Entry> uncommittedItems;
//    public static List<Entry> committedItems;
//    public static PlaceholderFragment uncommitFrag;
//    public static CommittedFragment commitFrag;

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.

        // yun long

        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
               File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
                File file = new File(sdcard,"ethogram.txt");

        //Read text from file
                StringBuilder text = new StringBuilder();
        behaviorCat = new Vector<String>();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        behaviorCat.add(line);
                        System.out.println(line);
                    }
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here

                }


        Collections.sort(behaviorCat);

//        committedItems.add("test");
//        committedItems.add("ahhh");
    }


    // yun long









}
