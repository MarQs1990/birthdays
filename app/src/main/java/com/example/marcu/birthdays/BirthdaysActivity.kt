package com.example.marcu.birthdays

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ExpandableListView

import kotlinx.android.synthetic.main.activity_birthdays.*
import android.os.Build

class BirthdaysActivity : AppCompatActivity() {

    private lateinit var birthdayView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var birthdays: MutableList<Person>
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var sidebarExpListView: ExpandableListView
    private lateinit var sidebarListAdapter: ExpandableListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthdays)

        createNotificationChannel()
        //TODO Beim Starten muss ein anderer Titel in der Toolbar stehen
        val intent = intent
        val month = intent.getIntExtra("Month", 14)

        NotificationScheduler.setReminder(this, AlarmReceiver::class.java, 7, 0)

        initiateToolbar()

        initiateFab()

        initiateSidebar()

        initiateExpListView()

        setToolBarTitle(NEXTTEN)

        setBirthdayView(month)
    }

    private fun initiateToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initiateFab(){
        fab.setOnClickListener {
            val intent = Intent(this, SavePersonActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initiateSidebar(){
        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun initiateExpListView(){
        sidebarExpListView = findViewById(R.id.navigation_menu)

        sidebarExpListView.setGroupIndicator(null)

        setItems()
        setListener()
    }

    private fun setItems(){

        val header = mutableListOf<String>()

        val childMonths = mutableListOf<String>()
        val childNextTen = mutableListOf<String>()
        val childAllBirthdays = mutableListOf<String>()

        val hashMap = mutableMapOf<String, MutableList<String>>()

        header.add(getString(R.string.next_ten_birthdays))
        header.add(getString(R.string.months))
        header.add(getString(R.string.all_birthdays))

        childMonths.add(getString(R.string.january))
        childMonths.add(getString(R.string.february))
        childMonths.add(getString(R.string.march))
        childMonths.add(getString(R.string.april))
        childMonths.add(getString(R.string.may))
        childMonths.add(getString(R.string.june))
        childMonths.add(getString(R.string.july))
        childMonths.add(getString(R.string.august))
        childMonths.add(getString(R.string.september))
        childMonths.add(getString(R.string.october))
        childMonths.add(getString(R.string.november))
        childMonths.add(getString(R.string.december))

        hashMap[header[0]] = childNextTen
        hashMap[header[1]] = childMonths
        hashMap[header[2]] = childAllBirthdays

        sidebarListAdapter = ExpandableListAdapter(this, header, hashMap)

        sidebarExpListView.setAdapter(sidebarListAdapter)
    }

    private fun setListener(){

        sidebarExpListView.setOnGroupClickListener { _, _, groupPosition, _ ->
            //val intent = Intent(this, BirthdaysActivity::class.java)
            if (groupPosition == GROUP_ALLMONTH){

                setBirthdayView(ALLBIRTHDAYS)
                setToolBarTitle(ALLBIRTHDAYS)
                closeDrawer()

                return@setOnGroupClickListener true
            } else if(groupPosition == GROUP_NEXTTEN){

                setBirthdayView(NEXTTEN)
                setToolBarTitle(NEXTTEN)
                closeDrawer()

                return@setOnGroupClickListener true
            }

            return@setOnGroupClickListener false
        }

        sidebarExpListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

            val month = childPosition + 1

            if (groupPosition == GROUP_MONTHS){
                setBirthdayView(month)
                setToolBarTitle(month)
                closeDrawer()
                return@setOnChildClickListener true
            }

            return@setOnChildClickListener false
        }
    }

    private fun setBirthdayView(month: Int){

        val dbHandler = BirthdaysDBHandler(this)

        birthdays = if (month < 14){
            dbHandler.findAllPeople(month)
        } else{
            dbHandler.findNextTenBirthdays()
        }

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
        val itemDecorator = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.abc_list_divider_material)!!)
        birthdayView.addItemDecoration(itemDecorator)

    }

    private fun setToolBarTitle(month: Int){

        when (month){
            1 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.january)
            2 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.february)
            3 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.march)
            4 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.april)
            5 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.may)
            6 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.june)
            7 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.july)
            8 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.august)
            9 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.september)
            10 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.october)
            11 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.november)
            12 -> toolbar.title = resources.getString(R.string.birthdays) + " " + resources.getString(R.string.december)
            13 -> toolbar.title = resources.getString(R.string.all_birthdays)
            14 -> toolbar.title = resources.getString(R.string.next_ten_birthdays)
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

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val person = birthdays[item!!.groupId]
        val dbHandler = BirthdaysDBHandler(this)
        val intent = Intent(this, SavePersonActivity::class.java)
        intent.putExtra("first name", person.firstName)
        intent.putExtra("second name", person.secondName)
        intent.putExtra("birthday", person.birthdayString)
        when (item.itemId){
            MENU_REMOVE -> dbHandler.deletePerson(person.firstName, person.secondName, person.birthdayString)
            MENU_EDIT -> startActivity(intent)
        }

        viewAdapter.notifyDataSetChanged()
        return true
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.notifyDataSetChanged()
    }

    private fun closeDrawer(){
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            closeDrawer()
        }else{
            super.onBackPressed()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "alarm_channel"
            val descriptionText = "Channel für Geburtstagserinnerung"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
