package uguu.gao.wafu.todo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by aki on 16/05/13.
 */

public class AddTaskActivity extends Activity {

    DatabaseHelper _db = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tasks);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                addTask();
                return true;
            case R.id.close_editor:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addTask() {
        EditText _content = (EditText) findViewById(R.id.add_task_content);
        String content = _content.getText().toString();
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DATE));
        if (content.equals("") == true) {
            Toast.makeText(this, "You haven't entered a task!", Toast.LENGTH_SHORT).show();

        } else {
            // add to database
            _db = new DatabaseHelper(this);

            SQLiteDatabase db = _db.getWritableDatabase();
            try {
                db.beginTransaction();
                ContentValues cv = new ContentValues();
                cv.put("CONTENT", content);
                cv.put("DATE_CREATED",year + "-" + month + "-" + day);
                db.insert("tasks","content", cv);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            Toast.makeText(this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
