package excuseznuos.esp1617.dei.unipd.it.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final List<RowItem> rowItems;

    CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }
    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    private class ViewHolder {
        ImageView profile_pic;  //immagine del profilo
        TextView member_name;   //nome destinatario
        TextView status;        //stato
        TextView contactType;   //tipo di contatto
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            //gestione delle varie righe della listView
            holder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            holder.profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.contactType = (TextView) convertView.findViewById(R.id.contact_type);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        RowItem row_pos = rowItems.get(position);
        //inserisco nelle righe i dati relativi al destinatario
        holder.profile_pic.setImageResource(row_pos.getProfile_pic_id());
        holder.member_name.setText(row_pos.getMember_name());
        holder.status.setText(row_pos.getStatus());
        holder.contactType.setText(row_pos.getContactType());

        return convertView;
    }
}
