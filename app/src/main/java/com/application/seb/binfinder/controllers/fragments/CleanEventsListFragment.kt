package com.application.seb.binfinder.controllers.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.seb.binfinder.R
import com.application.seb.binfinder.views.CleanEventsListAdapter
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import java.util.*


private const val ARG_PARAM1 = "isForSoonCleanEvents"
private const val ARG_PARAM2 = "isForUserCleanEvents"
private const val TAG = "CleanEventListFragment"

class CleanEventListFragment : Fragment() {

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
    private var isForSoonCleansEvents: Boolean = false
    private var isForUserCleansEvents: Boolean = true
    private val cleanEventRepository = CleanEventRepository()
    private lateinit var createTitleContainer: ConstraintLayout
    private lateinit var userTitleContainer: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView

//--------------------------------------------------------------------------------------------------
// Constructor
//--------------------------------------------------------------------------------------------------
    companion object {

        @JvmStatic
        fun newInstance(isForSoonCleansEvents: Boolean, isForUserCleansEvents: Boolean) =
                CleanEventListFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_PARAM1, isForSoonCleansEvents)
                        putBoolean(ARG_PARAM2, isForUserCleansEvents)
                    }
                }
    }

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isForSoonCleansEvents = it.getBoolean(ARG_PARAM1)
            isForUserCleansEvents = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_clean_event_list, container, false)

        createTitleContainer = rootView.findViewById(R.id.clean_event_list_fragment_create_list_title_container)
        userTitleContainer = rootView.findViewById(R.id.clean_event_list_fragment_participate_list_title_container)
        recyclerView = rootView.findViewById(R.id.clean_event_lis_fragment_recyclerView)
        recyclerView2 = rootView.findViewById(R.id.clean_event_lis_fragment_recyclerView2)


        if(isForSoonCleansEvents && !isForUserCleansEvents){
            Log.e(TAG, "is for Soon clean Events")
            createTitleContainer.visibility = View.GONE
            userTitleContainer.visibility = View.GONE
            recyclerView2.visibility = View.GONE
            getEventsByDateAndSTartRecyclerView()
        }
        else if(!isForSoonCleansEvents && isForUserCleansEvents){
            Log.e(TAG, "is for User clean Events")

            getUserCreatedEventsAndSTartRecyclerView()
            getParticipateCleanEventsAngConfigureRecyclerView()
        }

        return rootView
    }


//--------------------------------------------------------------------------------------------------
// Configure RecyclerView
//--------------------------------------------------------------------------------------------------
    private fun configureRecyclerView(results: MutableList<CleanEvent>, recyclerView: RecyclerView) {
        val adapter = CleanEventsListAdapter(results, context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context,2)
    }

//--------------------------------------------------------------------------------------------------
// Get data and start RecyclerView
//--------------------------------------------------------------------------------------------------

    private fun getEventsByDateAndSTartRecyclerView() {
        Log.e(TAG, "get Events by Date")

        cleanEventRepository.getCleanEventByDate(Utils.convertCalendarToFormatString(Calendar.getInstance())!!)
                .addOnSuccessListener {snapshot ->
                    val results : MutableList<CleanEvent> = mutableListOf()
                    for(cleanEvent in snapshot.documents) {results.add(cleanEvent.toObject(CleanEvent::class.java)!!)}
                    Log.e(TAG, "soon result size = ${results.size}")
                    configureRecyclerView(results, recyclerView)
                }
    }


    private fun getUserCreatedEventsAndSTartRecyclerView() {
        Log.e(TAG, "get Events by Created id")
        cleanEventRepository.getCleanEventByCreateUserId(FirebaseAuth.getInstance().uid!!)
                .addOnSuccessListener {snapshot ->

                    val results : MutableList<CleanEvent> = mutableListOf()
                    for(cleanEvent in snapshot.documents) {results.add(cleanEvent.toObject(CleanEvent::class.java)!!)}
                    Log.e(TAG, "create result size = ${results.size}")
                    if (results.size >= 1){
                        createTitleContainer.visibility = View.VISIBLE
                        configureRecyclerView(results, recyclerView)
                    }else{
                        createTitleContainer.visibility = View.GONE
                    }
                }
    }

    private fun getParticipateCleanEventsAngConfigureRecyclerView() {
        Log.e(TAG, "get Events by participate Id")
        cleanEventRepository.getParticipateCleanEvent(FirebaseAuth.getInstance().uid!!)
                .addOnSuccessListener {snapshot ->
                    val results : MutableList<CleanEvent> = mutableListOf()
                    for(cleanEvent in snapshot.documents) {results.add(cleanEvent.toObject(CleanEvent::class.java)!!)}
                    Log.e(TAG, "participate result size = ${results.size}")
                    if(results.size >= 1){
                        userTitleContainer.visibility = View.VISIBLE
                        recyclerView2.visibility = View.VISIBLE
                        configureRecyclerView(results, recyclerView2)
                    }else{
                        userTitleContainer.visibility = View.GONE
                        recyclerView2.visibility = View.GONE
                    }
                }
    }
}