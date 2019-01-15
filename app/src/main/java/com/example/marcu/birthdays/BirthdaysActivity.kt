package com.example.marcu.birthdays

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*

import kotlinx.android.synthetic.main.activity_birthdays.*

class BirthdaysActivity : AppCompatActivity() {
    private val MENU_REMOVE = 1
    private val MENU_EDIT = 2

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val person = birthdays[item!!.groupId]
        val dbHandler = BirthdaysDBHandler(this)
        when (item.itemId){
            MENU_REMOVE -> dbHandler.deletePerson(person.firstName, person.secondName)
            //TODO Implementierung des "Bearbeiten" Button
        }

        viewAdapter.notifyDataSetChanged()
        return true
    }



    private lateinit var birthdayView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var birthdays: MutableList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthdays)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, NewPersonActivity::class.java)
            startActivity(intent)
        }

        val actionbar = supportActionBar
        actionbar!!.setTitle("Geburtstage")
        actionbar.setDisplayHomeAsUpEnabled(true)


        val dbHandler = BirthdaysDBHandler(this)

        val intent = Intent()
        val month = intent.getIntExtra("Month", 1)
        birthdays = dbHandler.findAllPeople(month)


        viewManager = LinearLayoutManager(this)
        viewAdapter = PersonAdapter(this, birthdays)

        birthdayView = findViewById<RecyclerView>(R.id.birthdayView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

            registerForContextMenu(this)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.notifyDataSetChanged()
    }

}
