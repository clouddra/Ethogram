package animal.behavior.ethogram;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Convergence on 18/2/14.
 */
public class UncommittedFragment extends ListFragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */

    Dialog dialog;

    private static final String ARG_SECTION_NUMBER = "section_number";


    DatabaseHelper db;
    List<String> behavior;

    public UncommittedFragment(){

    }

//    public UncommittedFragment(DatabaseHelper db, List<String> behavior, Context context) {
////        this.db = db;
//        this.behavior = behavior;
//        this.context = context;
//    }

//    public
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        db = ((MainActivity) this.getActivity()).db;
        behavior = ((MainApplication) this.getActivity().getApplication()).behaviorCat;
        update();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        System.out.println("create view");
        ListAdapter adapter = new ListAdapter(inflater.getContext(), new ArrayList<Entry>());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("resumeing");
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, final long listId){
        AlertDialog.Builder builder;
        Context mContext = this.getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(this.getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.grid_dialog,(ViewGroup) this.getActivity().findViewById(R.id.layout_root));

        final int list_position = position;

        StickyGridHeadersGridView gridview = (StickyGridHeadersGridView)layout.findViewById(R.id.gridview);
        //                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //                    inflater.getContext(), android.R.layout.simple_list_item_1,
        //                    numbers_digits);

        gridview.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(inflater.getContext(),
                behavior,  R.layout.header,  R.layout.category));

        gridview.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                ListAdapter la = ((ListAdapter)UncommittedFragment.this.getListAdapter());
                // Update DB
                db.updateBehavior(listId,behavior.get(position));

                la.setItems(db.getAllUncommitted());
                ((ListAdapter) UncommittedFragment.this.getListAdapter()).notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }

    public void update() {
        List<Entry> uncommittedItems = db.getAllUncommitted();
//        uncommittedItems.add(new Entry());
        if (uncommittedItems==null)
            uncommittedItems = new Vector<Entry>();

        ((ListAdapter)this.getListAdapter()).setItems(uncommittedItems);
        ((ListAdapter) this.getListAdapter()).notifyDataSetChanged();
        System.out.println("lol");



    }


    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
    }
}
