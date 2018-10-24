package excuseznuos.esp1617.dei.unipd.it.chat;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] member_names;
    private TypedArray profile_pics;
    private String[] statues;
    private String[] contactType;
    private List<RowItem> rowItems;
    private ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("OnCreateMain","OnCreateMain");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //prendo l'array di risorse dalla cartella strings
        rowItems = new ArrayList<RowItem>();
        member_names = getResources().getStringArray(R.array.Member_names);
        profile_pics = getResources().obtainTypedArray(R.array.profile_pics);
        statues = getResources().getStringArray(R.array.statues);
        contactType = getResources().getStringArray(R.array.contactType);

        //per ogni utente inserisco i dati che si riferiscono alla posizione i-esima dell'array
        for (int i = 0; i < member_names.length; i++) {
            RowItem item = new RowItem(member_names[i],
                    profile_pics.getResourceId(i, -1) ,statues[i], contactType[i]);
            rowItems.add(item);
        }
        mylistview = (ListView) findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);
        profile_pics.recycle();
        mylistview.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("OnResumeMain","OnResumeMain");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String member_name = rowItems.get(position).getMember_name();
        int pic_name = rowItems.get(position).getProfile_pic_id();
        //mando un intent a WriteMessage
        Intent message = new Intent(getApplicationContext(), WriteMessage.class);
        message.putExtra("Destinatario",member_name);
        message.putExtra("id_pic",pic_name);
        startActivity(message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPauseMain","OnPauseMain");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("OnDestroyMain","OnDestroyMain");
    }
}