package com.application.seb.binfinder.controllers.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.activities.addCleanEvent.AddCleanEventActivity
import com.application.seb.binfinder.utils.PageAdapter
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "showUserCleanEvents"
private const val TABS_NUMBER = 2
private const val TABS_USER_CLEAN_EVENTS_INDEX = 1



class CleanEventFragment : Fragment() {

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
    private var showUserCleanEvents: Boolean = false
    private lateinit var pager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var addButton: ImageButton

//--------------------------------------------------------------------------------------------------
// Constructor
//--------------------------------------------------------------------------------------------------
    companion object {
        @JvmStatic
        fun newInstance(showUserCleanEvents: Boolean) =
                CleanEventFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_PARAM1, showUserCleanEvents)
                    }
                }
    }

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showUserCleanEvents = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_clean_event, container, false)

        pager = rootView.findViewById(R.id.clean_event_frag_viewpager)
        tabs = rootView.findViewById(R.id.clean_event_frag_table_layout)
        addButton = rootView.findViewById(R.id.clean_event_frag_fab_add)

        configureViewPagerAndTabs()
        configureAddButton()
        setDefaultView()
        return rootView

    }


//--------------------------------------------------------------------------------------------------
// Show View Pager
//--------------------------------------------------------------------------------------------------
    /**
     * This method configure the ViewPager and his TabLayout
     */
    private fun configureViewPagerAndTabs() {
        // Set total page number to 2
        pager.offscreenPageLimit = TABS_NUMBER
        //Set Adapter PageAdapter and glue it together
        pager.adapter = PageAdapter(fragmentManager, context!!)
        //Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager)
        // Design purpose. Tabs have the same width
        tabs.tabMode = TabLayout.MODE_FIXED
    }


//--------------------------------------------------------------------------------------------------
// Add button
//--------------------------------------------------------------------------------------------------
    private fun configureAddButton(){
        addButton.setOnClickListener {
            startActivity(Intent(context, AddCleanEventActivity::class.java))
        }
}

//--------------------------------------------------------------------------------------------------
// Default view
//--------------------------------------------------------------------------------------------------

    private fun setDefaultView(){

        if(showUserCleanEvents){
            val tab : TabLayout.Tab? = tabs.getTabAt(TABS_USER_CLEAN_EVENTS_INDEX)
            tab!!.select()
        }

    }
}