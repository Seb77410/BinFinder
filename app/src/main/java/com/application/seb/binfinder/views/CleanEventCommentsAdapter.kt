package com.application.seb.binfinder.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.Comment
import com.application.seb.binfinder.repositories.CommentRepository
import com.application.seb.binfinder.utils.Utils


class CleanEventCommentsAdapter (private val commentsList: MutableList<String>?) :  RecyclerView.Adapter<CleanEventCommentsAdapter.CleanEventCommentsViewHolder>(){

    inner class CleanEventCommentsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var userNameView: TextView = itemView.findViewById(R.id.comment_item_userName)
        var dateView: TextView = itemView.findViewById(R.id.comment_item_date)
        var commentView: TextView = itemView.findViewById(R.id.comment_item_comment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanEventCommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_clean_event_details_comments_item, parent, false)
        return CleanEventCommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CleanEventCommentsViewHolder, position: Int) {

        val repository = CommentRepository()
        repository.getCommentById(commentsList!![position])
                .addOnSuccessListener { doc ->
                    val mComment = doc.toObject(Comment::class.java)

                    holder.userNameView.text = mComment!!.userName
                    holder.dateView.text = Utils.convertDataDateToFormatString( mComment.date.toInt())
                    holder.commentView.text = mComment.content
                }

    }

    override fun getItemCount(): Int {
        return commentsList?.size ?: 0
    }
}