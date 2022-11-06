package io.usys.report.model

import android.app.Activity
import android.app.Dialog
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.google.firebase.database.DataSnapshot
import io.realm.RealmList
import io.usys.report.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.usys.report.firebase.*
import io.usys.report.model.Review.Companion.RATING_SCORE
import io.usys.report.utils.*

/**
 * Created by ChazzCoin : October 2022.
 */
open class Review : RealmObject() {

    companion object {
        const val RATING_SCORE = "score"
    }

    @PrimaryKey
    var id: String = newUUID()
    var dateCreated: String? = getTimeStamp()
    var creatorId: String? = null
    var receiverId: String? = null
    var sportName: String? = null
    var score: String = "0" // score 1 or 10
    var type: String? = null
    var details: String? = null //
    var questions: RealmList<Question>? = null
    var comment: String? = ""
}

// Verified
inline fun getOrgRatingAsync(objId: String, crossinline block: DataSnapshot?.() -> Unit) {
    firebaseDatabase {
        it.child(FireTypes.ORGANIZATIONS).child(objId).child(RATING_SCORE)
            .fairAddListenerForSingleValueEvent { ds ->
                block(ds)
            }
    }
}


fun updateOrgRatingScore(orgId:String, newRatingScore:String) {
    //busa = "54d9d63d-52bb-4503-95ca-8bda462e0f9a"
    updateSingleValueDBAsync(FireTypes.ORGANIZATIONS, orgId, RATING_SCORE, newRatingScore)
}

open class Question: RealmObject() {
    @PrimaryKey
    var id: String = newUUID()
    var question: String? = null
    var choices: RealmList<String>? = null
    var answer: String? = null
}

fun Review.addUpdateInFirebase(): Boolean {
    return addUpdateDBAsync(FireDB.REVIEWS, this.id, this)
}

private fun createReview() {
    val rev = Review()
    rev.apply {
        this.id = newUUID()
        this.score = "4"
        this.details = "us soccer"
        this.questions = RealmList(
            Question().apply { this.question = "Are you satisfied?" },
            Question().apply { this.question = "Does this coach work well with kids?" },
            Question().apply { this.question = "Does this coach work well with parents?" },
            Question().apply { this.question = "Is this coach Chace Zanaty?" })
    }
    addUpdateDBAsync(FireDB.REVIEWS, rev.id, rev)
}

fun createReviewDialog(activity: Activity, receiverId: String) : Dialog {

    val TYPE = FireTypes.ORGANIZATIONS
    val SPORT = "soccer"

    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.dialog_review_layout)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val commentEditTxt = dialog.bind<EditText>(R.id.reviewEditComment)
    val reviewRatingBar = dialog.bind<RatingBar>(R.id.reviewRatingBar)

    // On Clicks
    val submit = dialog.findViewById(R.id.reviewBtnSubmit) as Button
    val cancel = dialog.findViewById(R.id.reviewBtnCancel) as Button

    // Inner Function for updating overall score and returning it to add Review Obj.
    fun getAndUpdateNewRatingScore() {
        var overallAvgScore = "0.0"
        val ratingScore: Float = reviewRatingBar.rating
        getOrgRatingAsync(receiverId) {
            val result = this?.getValue(String::class.java)
            overallAvgScore = calculateAverageRatingScore(result, ratingScore)
            updateOrgRatingScore(receiverId, overallAvgScore)
            safeUserId { itUserId ->
                Review().apply {
                    this.creatorId = itUserId
                    this.receiverId = receiverId
                    this.comment = commentEditTxt.text.toString()
                    this.score = overallAvgScore
                    this.sportName = SPORT
                    this.type = TYPE
                }.addUpdateReviewDBAsync()
            }
        }
    }

    submit.setOnClickListener {
        getAndUpdateNewRatingScore()
        dialog.dismiss()
    }
    cancel.setOnClickListener {
        dialog.dismiss()
    }
    return dialog
}

fun calculateAverageRatingScore(overallScore:String?, singleScore:Float) : String {
    // Math for Rating
    val orgScore: Float = overallScore?.toFloat() ?: 0.0F
    val sumScore = orgScore + singleScore
    return (sumScore / 2).toString()
}