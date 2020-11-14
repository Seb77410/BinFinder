package com.application.seb.binfinder.controllers.activities.cleanEventDetails

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.activities.addCleanEvent.AddCleanEventActivity
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.repositories.CommentRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.GlideApp
import com.application.seb.binfinder.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import java.util.*

private const val TAG = "CleanEventActivity"

class CleanEventDetailsActivity : AppCompatActivity() {

//--------------------------------------------------------------------------------------------------
// For Data
//--------------------------------------------------------------------------------------------------
    private lateinit var toolbar: Toolbar
    private lateinit var event: CleanEvent
    private lateinit var titleView: TextView
    private lateinit var participantsNumberView: TextView
    private lateinit var dateView: TextView
    private lateinit var addressView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var participateButton: AppCompatButton
    private lateinit var imageView: ImageView
    private lateinit var creatorNameView: TextView
    private lateinit var createDateView: TextView
    private lateinit var sendMessageButton: Button
    private lateinit var commentEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CleanEventCommentsAdapter
    private val cleanEventRepository = CleanEventRepository()
    private val commentRepository = CommentRepository()


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_event_details)

        toolbar = findViewById(R.id.clean_event_details_activity_toolbar)
        titleView = findViewById(R.id.clean_event_details_activity_title)
        participantsNumberView = findViewById(R.id.clean_event_details_activity_participants_number)
        dateView = findViewById(R.id.clean_event_details_activity_date)
        addressView = findViewById(R.id.clean_event_details_activity_address)
        descriptionView = findViewById(R.id.clean_event_details_activity_description)
        participateButton = findViewById(R.id.clean_event_details_activity_participate_button)
        imageView = findViewById(R.id.clean_event_details_activity_image)
        creatorNameView = findViewById(R.id.clean_event_details_activity_createBy)
        createDateView = findViewById(R.id.clean_event_details_activity_createDate)
        sendMessageButton = findViewById(R.id.clean_event_details_activity_send_comment_button)
        commentEditText = findViewById(R.id.clean_event_details_activity_comments_editText)
        recyclerView = findViewById(R.id.clean_event_details_activity_comments_recycler_view)

        getArgs()
        configureToolbar()
        showData()
        configureRecyclerView()
    }



//--------------------------------------------------------------------------------------------------
// Get data
//--------------------------------------------------------------------------------------------------

    private fun getArgs(){
        val bundle = intent.extras
        if (bundle != null) {
            event = Utils.convertStringToCleanEvent(bundle.getString(Constants.ARGS_EVENT)!!)
        }
    }

//--------------------------------------------------------------------------------------------------
// Toolbar
//--------------------------------------------------------------------------------------------------

    private fun configureToolbar() {
        // Set action bar
        setSupportActionBar(toolbar)
        //Set back stack
        val upArrow = ResourcesCompat.getDrawable(this.resources, R.drawable.ic_arrow_back_white_24dp, null)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(upArrow)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        configureMenu()
    }

    private fun configureMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.clean_events_details_menu_remove -> {
                    Log.d(TAG, "click on delete button")
                    showAlertDialog()
                    true
                }

                R.id.clean_events_details_menu_edit -> {
                    Log.d(TAG, "click on edit button")
                    val intent = Intent(this, AddCleanEventActivity::class.java)
                    intent.putExtra(Constants.ARGS_EVENT, Utils.convertCleanEventToString(event))
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        return if(event.createBy_userId == FirebaseAuth.getInstance().uid){
            menuInflater.inflate(R.menu.clean_event_details_menu, menu)
            true
        }
        else {
            false
        }
    }

    private fun showAlertDialog(){
        val dialogBuilder =  AlertDialog.Builder(this)
        dialogBuilder
                .setTitle(getString(R.string.alert_dialog_delete_event_title))
                .setMessage(getString(R.string.alert_dialog_delete_event_content))
                .setPositiveButton(getString(R.string.alert_dialog_yes_button)) { _: DialogInterface, _: Int ->
                    Log.d(TAG, "Alert dialog - click YES button")
                    cleanEventRepository.deleteCleanEvent(event.eventId!!)
                            .addOnSuccessListener {
                                cleanEventRepository.deletePhoto(event.eventId!!)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "clean event (id: ${event.eventId}) successful delete")
                                            if (event.comments != null && event.comments!!.size > 0) {
                                                for (commentId in event.comments!!) {
                                                    commentRepository.deleteComment(commentId)
                                                    Log.d(TAG, "clean event comment (id: $commentId) successful delete")
                                                }
                                            }
                                        }
                            }
                }
                .setNegativeButton(getString(R.string.alert_dialog_no_button)) { _: DialogInterface, _: Int ->
                    Log.d(TAG, "Alert dialog - click NO button")
                }

        val dialogCard: AlertDialog = dialogBuilder.create()
        dialogCard.window!!.setGravity(Gravity.TOP)
        dialogCard.show()
    }

//--------------------------------------------------------------------------------------------------
// Configure view
//--------------------------------------------------------------------------------------------------

    private fun showData(){
        setTitle()
        setParticipantsNumber()
        setDate()
        setAddress()
        setEventCreatorName()
        setCreateDateView()
        setDescription()
        setParticipateButton()
        setImage()
        setSendMessageButton()
    }

    private fun setTitle() { titleView.text = event.title }

    private fun setParticipantsNumber() { participantsNumberView.text = event.participants!!.size.toString() }

    private fun setDate(){ dateView.text = Utils.convertDataDateToFormatString(event.eventDate)}

    private fun setAddress(){ addressView.text = event.address}

    private fun setEventCreatorName(){ creatorNameView.text = event.createBy_userName }

    private fun setCreateDateView(){ createDateView.text = Utils.convertDataDateToFormatString(event.createDate.toInt()) }

    private fun setDescription(){ descriptionView.text = event.description}

    private fun setImage(){
        cleanEventRepository.getPhoto(event.eventId!!).addOnSuccessListener { uri ->
            GlideApp.with(applicationContext)
                    .load(uri)
                    .centerCrop()
                    .into(imageView)
        }

    }

    private fun setParticipateButton(){
        var buttonIsClicked: Boolean

        if (event.participants!!.contains(FirebaseAuth.getInstance().uid!!)){
            buttonIsClicked = true
            participateButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccentTransparent))
            Log.e(TAG,"Participate button is clicked = $buttonIsClicked")
        }else{
            buttonIsClicked = false
            participateButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.transparent))
            Log.e(TAG,"Participate button is clicked = $buttonIsClicked")
        }

        participateButton.setOnClickListener{
            Log.e(TAG,"Participate button just clicked")
            when (buttonIsClicked){
                true -> {
                    cleanEventRepository.removeParticipantToCleanEvent(event.eventId!!, FirebaseAuth.getInstance().uid!!)
                            .addOnSuccessListener {
                                participateButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.transparent))
                                event.participants!!.remove(FirebaseAuth.getInstance().uid!!)
                                setParticipantsNumber()
                                Log.e(TAG,"if button was yet clicked")
                                buttonIsClicked = false
                            }
                }
                false -> {
                    cleanEventRepository.addParticipantToCleanEvent(event.eventId!!, FirebaseAuth.getInstance().uid!!)
                            .addOnSuccessListener {
                                participateButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccentTransparent))
                                event.participants!!.add(FirebaseAuth.getInstance().uid!!)
                                setParticipantsNumber()
                                Log.e(TAG, "if button was not clicked")
                                buttonIsClicked = true
                            }
                }
            }
        }
    }

    private fun setSendMessageButton(){

        var comment:String
        sendMessageButton.setOnClickListener {
            comment = commentEditText.text.toString()
            if(comment != ""){
                // Create document in FireStore data base
                commentRepository.createComment(FirebaseAuth.getInstance().uid!!, FirebaseAuth.getInstance().currentUser!!.displayName!!, comment, Utils.convertCalendarToFormatString(Calendar.getInstance())!!)
                        .addOnSuccessListener {doc ->
                                // Update comment id
                                commentRepository.updateCommentId(doc.id)
                                        .addOnSuccessListener {
                                            // Update event comments list
                                            cleanEventRepository.updateCommentsList(event.eventId!!, doc.id)
                                            event.comments!!.add(0,doc.id)
                                            adapter.notifyDataSetChanged()
                                            Log.e(TAG, "Comment save, comments size = ${event.comments?.size}")
                                        }
                            }
            }
        }
    }

//--------------------------------------------------------------------------------------------------
// Configure RecyclerView
//--------------------------------------------------------------------------------------------------
    private fun configureRecyclerView() {
        adapter = CleanEventCommentsAdapter(event.comments)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }


}