package annalinewyork.c4q.nyc.todolist;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity implements View.OnClickListener, View.OnKeyListener {

    EditText toDoEditTxtItem;
    Button toDoBtnAdd;
    ListView toDoListItems;

    List<String> toDoItemsList;
    ArrayAdapter<String> toDoAdapter;

    NotificationManager toDoNotificationManager;
    static final int uniqueID = 123123;

    public static final String MY_APP_PREFS = "MyAppPrefs";
    public static final String TO_DO_ITEMS_SET = "toDoList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoEditTxtItem = (EditText) findViewById(R.id.toDoEditTxtItem);
        toDoBtnAdd = (Button) findViewById(R.id.toDoBtnAdd);
        toDoListItems = (ListView) findViewById(R.id.toDoListItems);

        toDoBtnAdd.setOnClickListener(this);
        toDoEditTxtItem.setOnClickListener(this);

        reloadToDoSet();
        toDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoItemsList);
        toDoListItems.setAdapter(toDoAdapter);

        toDoNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        toDoNotificationManager.cancel(uniqueID);
    }

    private void reloadToDoSet() {
        SharedPreferences thePrefs = getSharedPreferences(MY_APP_PREFS, 0);
        Set<String> toDoItemsSet = new HashSet<String>();
        toDoItemsSet = thePrefs.getStringSet(TO_DO_ITEMS_SET, toDoItemsSet);
        toDoItemsList = new ArrayList<String>(toDoItemsSet);
    }

    private void saveToDoList() {
        SharedPreferences thePrefs = getSharedPreferences(MY_APP_PREFS, 0);
        SharedPreferences.Editor editor = thePrefs.edit();
        Set<String> toDoSet = new HashSet<String>(toDoItemsList);
        editor.putStringSet(TO_DO_ITEMS_SET, toDoSet);
        editor.commit();

    }

    private void addItem(String item) {
        if (item.length() > 0) {
            this.toDoItemsList.add(item);
            this.toDoAdapter.notifyDataSetChanged();
            this.toDoEditTxtItem.setText("");
            saveToDoList();
        }
    }

    @Override
    public void onClick(View view) {
     if (view == this.toDoBtnAdd){
         this.addItem(this.toDoEditTxtItem.getText().toString());
     }

        Intent intent = new Intent(this, StatusBarNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        String body = "You added a new to-do";
        String title = "TO DO";
        Notification notification = new Notification(R.drawable.noitification,body,System.currentTimeMillis());
        notification.setLatestEventInfo(this, title, body, pendingIntent);
        notification.defaults = Notification.DEFAULT_ALL;
        toDoNotificationManager.notify(uniqueID, notification);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
       if (event.getAction()== KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
           this.addItem(this.toDoEditTxtItem.getText().toString());
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

