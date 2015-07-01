package annalinewyork.c4q.nyc.todolist;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener, View.OnKeyListener {

    EditText txtItem;
    Button btnAdd;
    ListView listItems;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aa;

    NotificationManager nm;
    static final int uniqueID = 123123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtItem = (EditText) findViewById(R.id.txtItem);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        listItems = (ListView) findViewById(R.id.listItems);

        btnAdd.setOnClickListener(this);
        txtItem.setOnClickListener(this);

        todoItems = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        listItems.setAdapter(aa);

        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(uniqueID);
    }

    private void addItem(String item) {
        if (item.length() > 0) {
            this.todoItems.add(item);
            this.aa.notifyDataSetChanged();
            this.txtItem.setText("");
        }
    }

    @Override
    public void onClick(View v) {
     if (v == this.btnAdd){
         this.addItem(this.txtItem.getText().toString());
     }

        Intent intent = new Intent(this, StatusBarNotification.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        String body = "You add a new to-do";
        String title = "TO DO";
        Notification n = new Notification(R.drawable.noitification,body,System.currentTimeMillis());
        n.setLatestEventInfo(this,title,body,pi);
        n.defaults = Notification.DEFAULT_ALL;
        nm.notify(uniqueID,n);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
       if (event.getAction()== KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
           this.addItem(this.txtItem.getText().toString());
       }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
