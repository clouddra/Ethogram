package animal.behavior.ethogram;


import android.app.Application;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class MainApplication extends Application
{
    public static List<String> behaviorCat;
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.


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




    }


}