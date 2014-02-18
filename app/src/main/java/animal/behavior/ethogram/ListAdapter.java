package animal.behavior.ethogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

/**
 * Created by Convergence on 18/2/14.
 */
public class ListAdapter extends BaseAdapter {

    List<Entry> listItems;
    Context context;

    public ListAdapter(Context context, List<Entry> items) {
            this.context = context;
            this.listItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
//                holder.txtDesc = (TextView) convertView.findViewById(android.R.id.text2);
            holder.txtTitle = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Entry rowItem = (Entry) getItem(position);

//            holder.txtDesc.setText(rowItem.toString());
        holder.txtTitle.setText(rowItem.toString());

        return convertView;



    }

    public void setItems(List<Entry> items){
        listItems = items;
    }

    public List<Entry> getItems(){
        return listItems;
    }


    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listItems.indexOf(getItem(position));
    }
}



