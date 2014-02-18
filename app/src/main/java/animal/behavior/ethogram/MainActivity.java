package animal.behavior.ethogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public static boolean start = false;
    public static boolean start_stop = false;

    long startTimeinUnix = 0;
    long stopTimeinUnix = 0;
    CommittedFragment commitFrag;
    UncommittedFragment uncommitFrag;

    DatabaseHelper db = new DatabaseHelper(this);

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


        commitFrag = new CommittedFragment(db, ((MainApplication) this.getApplication()).behaviorCat, this);
        uncommitFrag = new UncommittedFragment(db, ((MainApplication) this.getApplication()).behaviorCat, this);
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

                invalidateOptionsMenu();

                Toast.makeText(context, "Started recording", Toast.LENGTH_SHORT).show();
            } else if (start == true) {
                start = false;

                stopTimeinUnix = System.currentTimeMillis() / 1000L;

                // elapsedTime in seconds
                int elapsedTime = (int) (stopTimeinUnix - startTimeinUnix);

                db.insertEntry(startTimeinUnix, stopTimeinUnix, null);

                invalidateOptionsMenu();

//                ((MainApplication) this.getApplicationContext()).update();
                uncommitFrag.update();

                Toast.makeText(context, "Elapsed Time: " + elapsedTime + " seconds", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        if (id == R.id.action_stop_start) {
            if (start == true) {

                stopTimeinUnix = System.currentTimeMillis() / 1000L;

                // elapsedTime in seconds
                int elapsedTime = (int) (stopTimeinUnix - startTimeinUnix);

                db.insertEntry(startTimeinUnix, stopTimeinUnix, null);

                invalidateOptionsMenu();

//                commitFrag.update();
                uncommitFrag.update();

                Toast.makeText(context, "Elapsed Time: " + elapsedTime + " seconds", Toast.LENGTH_SHORT).show();

                startTimeinUnix = System.currentTimeMillis() / 1000L;

            }

            return true;
        }

        if (id == R.id.action_export) {

            db.exportToFile();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (start == true)
            menu.getItem(0).setTitle("Stop");
        else
            menu.getItem(0).setTitle("Start");

        return true;
    }


    private String unixConvertTo24Hours(long unixTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unixTime * 1000L);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        String time = sdf.format(c.getTime());

        return time;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        Log.i("main", String.valueOf(tab.getPosition()));
        if(tab.getPosition() == 0) { // 0 is uncommitFrag
            if(uncommitFrag != null){
                uncommitFrag.update();
                ((ListAdapter) uncommitFrag.getListAdapter()).notifyDataSetChanged();
            }
        }
        else {
            if(commitFrag != null){
                commitFrag.update();
                ((ListAdapter) commitFrag.getListAdapter()).notifyDataSetChanged();
            }
        }
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
            // yun long
            if (position == 0) {
                return uncommitFrag;
            } else if (position == 1) {
                return commitFrag;
            } else return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
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


}
