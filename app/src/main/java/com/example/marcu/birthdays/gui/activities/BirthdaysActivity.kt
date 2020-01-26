package com.example.marcu.birthdays.gui.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.ExpandableListView
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.R.drawable.abc_list_divider_material
import com.example.marcu.birthdays.birthdays.Birthday
import com.example.marcu.birthdays.birthdays.BirthdaysDBHandler
import com.example.marcu.birthdays.birthdays.MonthsBirthdayView
import com.example.marcu.birthdays.core.*
import com.example.marcu.birthdays.gui.sidebar.ExpandableListAdapter
import com.example.marcu.birthdays.gui.birthdayview.OnBirthdayClickListener
import com.example.marcu.birthdays.gui.birthdayview.BirthdayAdapter
import com.example.marcu.birthdays.gui.birthdayview.SwipeToDeleteBirthdayCallback
import com.example.marcu.birthdays.core.notification.NotificationScheduler
import kotlinx.android.synthetic.main.activity_birthdays.*

class BirthdaysActivity : AppCompatActivity(),
    OnBirthdayClickListener {

    private lateinit var birthdayView: RecyclerView
    private lateinit var birthdayList: MutableList<Birthday>
    private lateinit var birthdayViewAdapter: BirthdayAdapter
    private lateinit var birthdayViewManager: RecyclerView.LayoutManager
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var sidebarExpListView: ExpandableListView
    private lateinit var sidebarListAdapter: ExpandableListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthdays)

        createNotificationChannel()

        val intent = intent
        val month = intent.getIntExtra("Month", 14)

        NotificationScheduler.setAlarm(this, BirthdaysActivity::class.java, 7, 0)

        initiateToolbar()

        initiateSidebar()

        initiateExpListView()

        setToolBarTitle(MonthsBirthdayView.NEXTTEN.value)

        setBirthdayView(month)
    }

    private fun initiateToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.next_ten_birthdays)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))
        toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initiateSidebar() {
        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun initiateExpListView() {
        sidebarExpListView = findViewById(R.id.navigation_menu)

        sidebarExpListView.setGroupIndicator(null)

        setItems()
        setListener()
    }

    private fun setItems() {
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

        sidebarListAdapter =
            ExpandableListAdapter(
                this, header, hashMap
            )

        sidebarExpListView.setAdapter(sidebarListAdapter)
    }

    private fun setListener() {
        sidebarExpListView.setOnGroupClickListener { _, _, groupPosition, _ ->
            if (groupPosition == GROUP_ALLMONTH) {

                setBirthdayView(MonthsBirthdayView.ALL.value)
                setToolBarTitle(MonthsBirthdayView.ALL.value)
                closeDrawer()

                return@setOnGroupClickListener true
            } else if (groupPosition == GROUP_NEXTTEN) {

                setBirthdayView(MonthsBirthdayView.NEXTTEN.value)
                setToolBarTitle(MonthsBirthdayView.NEXTTEN.value)
                closeDrawer()

                return@setOnGroupClickListener true
            }

            return@setOnGroupClickListener false
        }

        sidebarExpListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

            val month = childPosition + 1

            if (groupPosition == GROUP_MONTHS) {
                setBirthdayView(month)
                setToolBarTitle(month)
                closeDrawer()
                return@setOnChildClickListener true
            }

            return@setOnChildClickListener false
        }

        fab.setOnClickListener {
            showSaveBirthdayDialog()
        }
    }

    private fun showSaveBirthdayDialog() {
        SaveEditBirthdayDialog(this, birthdayViewAdapter).setupDialog()
    }

    private fun showEditBirthdayDialog(birthday: Birthday) {
        SaveEditBirthdayDialog(this, birthdayViewAdapter, birthday).setupDialog()
    }

    @SuppressLint("PrivateResource")
    private fun setBirthdayView(monthValue: Int) {
        val dbHandler = BirthdaysDBHandler(this)
        birthdayList = when {
            monthValue < 13 -> dbHandler.getAllBirthdaysByMonth(monthValue)
            monthValue == 13 -> dbHandler.getAllBirthdays()
            else -> dbHandler.findNextTenBirthdays()
        }

        birthdayViewManager = LinearLayoutManager(this)
        birthdayViewAdapter = BirthdayAdapter(this, birthdayList,this)

        birthdayView = findViewById<RecyclerView>(R.id.birthdayView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = birthdayViewManager

            adapter = birthdayViewAdapter

            registerForContextMenu(this)
        }

        val itemDecorator = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                applicationContext, abc_list_divider_material
            )!!
        )

        birthdayView.addItemDecoration(itemDecorator)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteBirthdayCallback(birthdayViewAdapter, this))
        itemTouchHelper.attachToRecyclerView(birthdayView)
    }

    private fun setToolBarTitle(month: Int) {

        when (month) {
            MonthsBirthdayView.JANUARY.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.january
                )
            MonthsBirthdayView.FEBRUARY.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.february
                )
            MonthsBirthdayView.MARCH.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.march
                )
            MonthsBirthdayView.APRIL.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.april
                )
            MonthsBirthdayView.MAY.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.may
                )
            MonthsBirthdayView.JUNE.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.june
                )
            MonthsBirthdayView.JULY.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.july
                )
            MonthsBirthdayView.AUGUST.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.august
                )
            MonthsBirthdayView.SEPTEMBER.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.september
                )
            MonthsBirthdayView.OCTOBER.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.october
                )
            MonthsBirthdayView.NOVEMBER.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.november
                )
            MonthsBirthdayView.DECEMBER.value -> toolbar.title =
                resources.getString(R.string.birthdays) + " " + resources.getString(
                    R.string.december
                )
            MonthsBirthdayView.ALL.value -> toolbar.title =
                resources.getString(R.string.all_birthdays)
            MonthsBirthdayView.NEXTTEN.value -> toolbar.title =
                resources.getString(R.string.next_ten_birthdays)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, as long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                goToSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val birthday = birthdayList[item!!.groupId]

        when (item.itemId) {
            MENU_REMOVE -> birthdayViewAdapter.removeBirthday(birthday)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        birthdayViewAdapter.notifyDataSetChanged()
    }

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
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

    private fun goToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onBirthdayClicked(birthday: Birthday) {
        showEditBirthdayDialog(birthday)
    }
}
