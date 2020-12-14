package com.application.seb.binfinder.views

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.activities.CleanEventDetailsActivity
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.GlideApp
import com.application.seb.binfinder.utils.Utils

class CleanEventsListAdapter(private val cleanEventsList: List<CleanEvent>, private val context: Context) :  RecyclerView.Adapter<CleanEventsListAdapter.CleanEventsListViewHolder>(){


    inner class CleanEventsListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var dateView: TextView = itemView.findViewById(R.id.clean_event_frag_item_date)
        var addressView: TextView = itemView.findViewById(R.id.clean_event_frag_item_address)
        var participantsNumber: TextView = itemView.findViewById(R.id.clean_event_frag_item_participants_number)
        var eventTitle: TextView = itemView.findViewById(R.id.clean_event_frag_item_title)
        var eventImage: ImageView = itemView.findViewById(R.id.clean_event_frag_item_image)
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanEventsListViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_clean_event_list_item, parent, false)

        return CleanEventsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CleanEventsListViewHolder, position: Int) {

        holder.dateView.text = Utils.convertDataDateToFormatString( cleanEventsList[position].eventDate)
        holder.addressView.text = cleanEventsList[position].address
        holder.participantsNumber.text = cleanEventsList[position].participants!!.size.toString()
        holder.eventTitle.text = cleanEventsList[position].title

        val cleanEventRepository= CleanEventRepository()
        cleanEventRepository.getPhoto(cleanEventsList[position].eventId!!).addOnSuccessListener { uri ->
            GlideApp.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(holder.eventImage)
        }


        holder.itemView.setOnClickListener{
            val intent = Intent(context, CleanEventDetailsActivity::class.java)
            intent.putExtra(Constants.ARGS_EVENT, Utils.convertCleanEventToString(cleanEventsList[position]))
            context.startActivity(intent)
        }


    }

    override fun getItemCount() = cleanEventsList.size


}



