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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class MainApplication extends Application
{
    public static List<String> behaviorCat;
    public static List<String> uncommittedItems;
    public static List<String> chosenBehavior;
    public static PlaceholderFragment uncommittedFragment;
    public static PlaceholderFragment committedFragment;

//    DatabaseHelper db = new DatabaseHelper(this);
    DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());


    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.

        // yun long
        uncommittedItems = new Vector<String>();
        chosenBehavior = new Vector<String>();
        uncommittedFragment = new PlaceholderFragment();
        committedFragment = new PlaceholderFragment();
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
    }


    // yun long
    public class PlaceholderFragment extends ListFragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */

        Dialog dialog;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
//        public PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
////            Bundle args = new Bundle();
////            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
////            fragment.setArguments(args);
//            return fragment;
//        }
//
        public PlaceholderFragment() {
        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    uncommittedItems);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onListItemClick (ListView l, View v, int position, long id){
            if (MainActivity.start == false) {
                AlertDialog.Builder builder;
                Context mContext = this.getActivity();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.grid_dialog,(ViewGroup) this.getActivity().findViewById(R.id.layout_root));

                final int list_position = position;

                StickyGridHeadersGridView gridview = (StickyGridHeadersGridView)layout.findViewById(R.id.gridview);
    //                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
    //                    inflater.getContext(), android.R.layout.simple_list_item_1,
    //                    numbers_digits);

                gridview.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(inflater.getContext(),
                        ((MainApplication)this.getActivity().getApplication()).behaviorCat,  R.layout.header,  R.layout.category));

                gridview.setOnItemClickListener(new OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                        //Toast.makeText(v.getContext(), "Position is " + position, 3000).show();

                        //uncommittedItems.add("new item added from grid");
                        chosenBehavior.add(list_position,behaviorCat.get(position));

                        // Insert into DB
                        db.insertEntry(MainActivity.start_list.get(list_position), MainActivity.stop_list.get(list_position), behaviorCat.get(position));

                        // Remove entry in vectors
                        MainActivity.start_list.remove(list_position);
                        MainActivity.stop_list.remove(list_position);
                        chosenBehavior.remove(list_position);
                        uncommittedItems.remove(list_position);


                        ((ArrayAdapter<String>) PlaceholderFragment.this.getListAdapter()).notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                dialog = builder.create();
                dialog.show();

            }
            else {
                // If the "stop" button is not pressed, only allow to select for previous sessions
                if (position < MainActivity.start_list.size()-1) {
                    AlertDialog.Builder builder;
                    Context mContext = this.getActivity();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.grid_dialog,(ViewGroup) this.getActivity().findViewById(R.id.layout_root));

                    final int list_position = position;

                    StickyGridHeadersGridView gridview = (StickyGridHeadersGridView)layout.findViewById(R.id.gridview);

                    gridview.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(inflater.getContext(),
                            ((MainApplication)this.getActivity().getApplication()).behaviorCat,  R.layout.header,  R.layout.category));

                    gridview.setOnItemClickListener(new OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                            //Toast.makeText(v.getContext(), "Position is " + position, 3000).show();

                            //uncommittedItems.add("new item added from grid");
                            chosenBehavior.add(list_position,behaviorCat.get(position));

                            // Insert into DB
                            db.insertEntry(MainActivity.start_list.get(list_position), MainActivity.stop_list.get(list_position), behaviorCat.get(position));
                            // Remove entry in vectors
                            MainActivity.start_list.remove(list_position);
                            MainActivity.stop_list.remove(list_position);
                            chosenBehavior.remove(list_position);
                            uncommittedItems.remove(list_position);

                            ((ArrayAdapter<String>) PlaceholderFragment.this.getListAdapter()).notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    builder = new AlertDialog.Builder(mContext);
                    builder.setView(layout);
                    dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }
}