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

import java.util.List;
import java.util.Vector;

/**
 * Created by Convergence on 18/2/14.
 */
public class CommittedFragment extends ListFragment {

    Dialog dialog;
    DatabaseHelper db;
    List<String> behavior;
    Context context;

    public CommittedFragment(DatabaseHelper db, List<String> behavior, Context context) {
        this.db = db;
        this.behavior = behavior;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("create view");
        ListAdapter adapter = new ListAdapter(inflater.getContext(), db.getAllCommitted());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("resumeing");
    }


    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
        AlertDialog.Builder builder;
        Context mContext = this.getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.grid_dialog,(ViewGroup) this.getActivity().findViewById(R.id.layout_root));

        final int list_position = position;

        StickyGridHeadersGridView gridview = (StickyGridHeadersGridView)layout.findViewById(R.id.gridview);


        gridview.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(inflater.getContext(),
                behavior,  R.layout.header,  R.layout.category));


        gridview.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                ListAdapter la = ((ListAdapter)CommittedFragment.this.getListAdapter());
                // Update DB
                db.updateBehavior(((Entry)la.getItem(list_position)).getId(),behavior.get(position));

                la.setItems(db.getAllCommitted());
                ((ListAdapter) CommittedFragment.this.getListAdapter()).notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();
    }

    public void displayCommitted() {
        List<Entry> commmitted = db.getAllUncommitted();
//        uncommittedItems.add(new Entry());
        if (commmitted==null)
            commmitted = new Vector<Entry>();

        ((ListAdapter)this.getListAdapter()).setItems(commmitted);
        ((ListAdapter) this.getListAdapter()).notifyDataSetChanged();
        System.out.println("lol");



    }
}

