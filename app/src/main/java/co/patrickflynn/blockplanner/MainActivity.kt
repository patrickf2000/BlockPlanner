package co.patrickflynn.blockplanner

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class MainActivity : AppCompatActivity(), NewBlock.NoticeDialogListener {
    val MAX_ROWS = 17;
    val START_HOUR = 6;
    var rows = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            var dialog = NewBlock()
            dialog.show(this.supportFragmentManager, "")
        }

        // Add table rows
        var table : TableLayout = findViewById(R.id.time_table);

        for (i in 0 .. MAX_ROWS) {
            var row : TableRow = TableRow(this);

            var t = i + START_HOUR;

            var time : TextView = TextView(this);
            time.setText(t.toString());
            time.setPadding(3, 0,5, 15);
            time.setTextSize(22.0F);
            row.addView(time);

            var block : TextView = TextView(this);
            rows.add(block);
            block.setPadding(3, 20,5, 20);
            row.addView(block);

            table.addView(row, i);
        }

        updateTableRows()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                val settings = getPreferences(Context.MODE_PRIVATE)

                for (i in 0 .. 17) {
                    val key = "row" + i.toString();
                    with (settings.edit()) {
                        putString(key + "_task", "");
                        putInt(key + "_color", Color.WHITE)
                        apply()
                    }
                }

                updateTableRows()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogPositiveClick(dialog: NewBlock) {
        val settings = getPreferences(Context.MODE_PRIVATE)

        var start = dialog.getStart() - START_HOUR
        var end = dialog.getEnd() - START_HOUR
        var task = dialog.getTask()
        val color = dialog.getColor()

        for (i in start .. (end - 1)) {
            var block = rows.get(i)
            block.setBackgroundColor(color)

            if (i == start) {
                block.setText(task)
            }

            val key = "row" + i.toString();
            with (settings.edit()) {
                if (i == start)
                    putString(key + "_task", task);

                putInt(key + "_color", color)
                apply()
            }
        }
    }

    override fun onDialogNegativeClick(dialog: NewBlock) {
    }

    fun updateTableRows() {
        val settings = getPreferences(Context.MODE_PRIVATE)

        for (i in 0 .. MAX_ROWS) {
            val key = "row" + i.toString()
            val task = settings.getString(key + "_task", "")
            val color = settings.getInt(key + "_color", Color.WHITE)

            var block = rows.get(i)
            block.setBackgroundColor(color)
            block.setText(task)
        }
    }
}