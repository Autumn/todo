package uguu.gao.wafu.todo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.CursorAdapter;
import android.widget.ListView;
import android.view.MenuItem;

import android.content.Intent;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private DatabaseHelper _db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _db = new DatabaseHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_action_bar, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                newTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newTask() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    public void fillTasks() {
        ListView list = (ListView) findViewById(R.id.tasks);
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor tasks;

        try {
            db.beginTransaction();
            tasks = db.rawQuery("SELECT _id, content, date_created FROM tasks ORDER BY _id DESC", null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (tasks.getCount() == 0) {
            String[] items = {"Looks like you have no tasks! Add one from the action bar."};
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            list.setAdapter(adapter);
        } else {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.main_list, tasks, new String[] {"content", "date_created"}, new int[] {R.id.taskText, R.id.dateAdded}, 2);
            list.setAdapter(adapter);
        }

        /*
        load a custom menu for each item when selected - edit, delete, mark as complete
        */
    }
}
