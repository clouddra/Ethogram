package animal.behavior.ethogram;

import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapterWrapper;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

public class MainActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    Vector<String> start_time ;
    PlaceholderFragment first, second, third;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    static Vector<String> start_list = new Vector<String>();
    static Vector<String> stop_list = new Vector<String>();
    static Vector<String> elapsed_list = new Vector<String>();

    boolean start = false;
    boolean start_stop = false;

    long startTimeinUnix = 0;
    long stopTimeinUnix = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        start_time = new Vector<String>();
        for(int i = 0 ; i<5 ; i++){
            start_time.add("old item");
        }

         first = new PlaceholderFragment();
         second = new PlaceholderFragment();
         third = new PlaceholderFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Context context = getApplicationContext();

        int id = item.getItemId();
        if (id == R.id.action_start) {
            if (start == false) {
                start = true;

                startTimeinUnix = System.currentTimeMillis() / 1000L;



                Toast.makeText(context, "Started recording", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.action_stop) {
            if (start == true) {
                start = false;

                stopTimeinUnix = System.currentTimeMillis() / 1000L;

                start_list.add(unixConvertTo24Hours(startTimeinUnix));

                // elapsedTime in seconds
                int elapsedTime = (int) (stopTimeinUnix - startTimeinUnix);

                /*************************************************************
                ** write both startTimeinUnix & stopTimeinUnix to database here
                *************************************************************/

                Toast.makeText(context, "Elapsed Time: " + elapsedTime + " seconds" , Toast.LENGTH_LONG).show();
            }

            return true;
        }

        if (id == R.id.action_stop_start) {
            if (start == true) {
                start = false;

                stopTimeinUnix = System.currentTimeMillis() / 1000L;

                start_list.add(unixConvertTo24Hours(startTimeinUnix));
                /*************************************************************
                 ** write both startTimeinUnix & stopTimeinUnix to database here
                 *************************************************************/

                startTimeinUnix = System.currentTimeMillis() / 1000L;

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String unixConvertTo24Hours(long unixTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unixTime*1000L);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        String time = Integer.toString(hour) + ":" + Integer.toString(min) + "   " + Integer.toString(sec) + "s";

        return time;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {



        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position==0) return first;
            else if (position==1) return second;
            else return third;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_tab1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_tab2).toUpperCase(l);
            }
            return null;
        }
    }





    /**
     * A placeholder fragment containing a simple view.
     */
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
                    start_list);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onListItemClick (ListView l, View v, int position, long id){

            AlertDialog.Builder builder;
            Context mContext = this.getActivity();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.grid_dialog,(ViewGroup) this.getActivity().findViewById(R.id.layout_root));

            StickyGridHeadersGridView gridview = (StickyGridHeadersGridView)layout.findViewById(R.id.gridview);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                    inflater.getContext(), android.R.layout.simple_list_item_1,
//                    numbers_digits);

            gridview.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(inflater.getContext(),
                    ((MainApplication)this.getActivity().getApplication()).behaviorCat,  R.layout.header,  R.layout.category));

//            gridview.setOnItemClickListener(new OnItemClickListener()
//            {
//                public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
////                    Toast.makeText(v.getContext(), "Position is "+position, 3000).show();
//                    start_time.add("new item");
//                    ((ArrayAdapter<String>) PlaceholderFragment.this.getListAdapter()).notifyDataSetChanged();
//                    dialog.dismiss();
//                }
//            });

            builder = new AlertDialog.Builder(mContext);
            builder.setView(layout);
            dialog = builder.create();
            dialog.show();
        }
    }

}
