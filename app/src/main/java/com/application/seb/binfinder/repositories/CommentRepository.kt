package com.application.seb.binfinder.repositories

import com.application.seb.binfinder.models.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

private const val COMMENT_COLLECTION_REFERENCE = "Comments"
private const val COMMENT_ID = "id"


class CommentRepository {


//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun commentCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(COMMENT_COLLECTION_REFERENCE)
    }

//--------------------------------------------------------------------------------------------------
// Create
//--------------------------------------------------------------------------------------------------

    fun createComment(userId: String, userName: String , content: String,date: String): Task<DocumentReference> {
        val comment = Comment(null, userId, userName, content, date)
        return commentCollection()!!.add(comment)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getCommentById(commentId: String): Task<DocumentSnapshot> {
        return commentCollection()!!.document(commentId).get()
    }

//--------------------------------------------------------------------------------------------------
// Update
//--------------------------------------------------------------------------------------------------
    fun updateCommentId(commentId: String): Task<Nothing> {
        val eventRef = commentCollection()!!.document(commentId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            transaction.update(eventRef, COMMENT_ID, commentId)
            // Success
            null
        }
    }

//--------------------------------------------------------------------------------------------------
// Delete
//--------------------------------------------------------------------------------------------------
    fun deleteComment(commentId: String): Task<Void> {
            return commentCollection()!!.document(commentId).delete()

    }


}