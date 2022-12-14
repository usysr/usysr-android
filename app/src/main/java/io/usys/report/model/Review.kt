package io.usys.report.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.usys.report.firebase.*
import io.usys.report.utils.*
import java.io.Serializable

/**
 * Created by ChazzCoin : October 2022.
 */
open class Review : RealmObject(), Serializable {

    companion object {
        const val RATING_SCORE = "ratingScore"
        const val RATING_COUNT = "ratingCount"
        const val ANSWER_COUNT = "reviewAnswerCount"
    }

    @PrimaryKey
    var id: String = newUUID()
    var dateCreated: String? = getTimeStamp()
    var creatorId: String? = null
    var receiverId: String? = null
    var sportName: String? = null
    var score: String = "0" // score 1 or 5
    var type: String? = null
    var details: String? = null //
    var questions: RealmList<Question>? = null
    var comment: String? = ""
}

open class ReviewTemplate : RealmObject(), Serializable {

    companion object {
        const val UNASSIGNED = "unassigned"
        const val RATING_SCORE = "ratingScore"
        const val RATING_COUNT = "ratingCount"
    }

    @PrimaryKey
    var id: String = "master"
    var type: String = UNASSIGNED
    var dateCreated: String? = getTimeStamp()
    var sportName: String = ""
    var topScore: String = "5" // score 1 or 5
    var lowScore: String = "0"
    var details: String = "" //
    var questions: RealmList<Question>? = null
    var commentsEnabled: Boolean = false
}

fun getQuestionScore(letter:String?) : String {
    return when (letter) {
        Question.A -> "5"
        Question.B -> "4"
        Question.C -> "3"
        Question.D -> "2"
        else -> "0"
    }
}

fun createReviewTemplate() {
    val revTemplate = ReviewTemplate()
    revTemplate.apply {
        this.sportName = "soccer"
        this.type = FireTypes.COACHES
        this.details = "us soccer based review system for coaches"
        this.questions = RealmList(
            createBaseQuestion(),
            createBaseQuestion("Does this coach work well with kids?"),
            createBaseQuestion("Does this coach work well with parents?"),
            createBaseQuestion("Is this coach Chace Zanaty?"))
    }
    fireAddUpdateCoachReviewDBAsync(revTemplate)
}

open class Question: RealmObject(), Serializable {
    companion object {
        const val A = "a"
        const val B = "b"
        const val C = "c"
        const val D = "d"
    }
    @PrimaryKey
    var id: String = newUUID()
    var dateCreated: String = getTimeStamp()
    var question: String = ""
    var choiceA: String = ""
    var choiceB: String = ""
    var choiceC: String = ""
    var choiceD: String = ""
    var finalScore: String = "0"
    var answer: String? = null
}

fun createBaseQuestion(question:String?=null) : Question {
    return Question().apply {
        this.question = if (!question.isNullOrEmpty()) question.toString() else "This is a review question about the coach?"
        this.choiceA = "Weighted option 5"
        this.choiceB = "Weighted option 4"
        this.choiceC = "Weighted option 3"
        this.choiceD = "Weighted option 2"
    }
}