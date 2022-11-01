package io.usys.report.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.usys.report.utils.applyAndSave
import io.usys.report.utils.newUUID
import java.io.Serializable

/**
 * Created by ChazzCoin : October 2022.
 */
open class Coach : RealmObject(), Serializable {

    companion object {
        const val ORDER_BY_ORGANIZATION = "organizationId"
    }

    @PrimaryKey
    var id: String? = "" //UUID
    var name: String? = "" //Name Given by Manager
    var dateCreated: String? = "" // timestamp
    var ownerId: String? = "unassigned"
    var ownerName: String? = "unassigned"
    var organizationId: String? = ""
    var organizationIds: RealmList<String>? = RealmList()
    var addressOne: String? = "" // 2323 20th Ave South
    var addressTwo: String? = "" // 2323 20th Ave South
    var city: String? = "" // Birmingham
    var state: String? = "" // AL
    var zip: String? = "" // 35223
    var sport: String? = "unassigned"
    var type: String? = "unassigned"
    var subType: String? = "unassigned"
    var details: String? = ""
    var estPeople: String? = ""

    var hasReview: Boolean = false
    var reviews: RealmList<String>? = RealmList()
    var reviewOverallScore: Int = 9999
    var reviewDetails: String = ""

    fun getCityStateZip(): String {
        return "$city, $state $zip"
    }

}


fun createCoach() {
    Coach().applyAndSave() {
        it.id = newUUID()
        it.name = "Lucas Romeo"
        it.sport = "soccer"
        it.organizationId = "d72c7cd5-1789-437c-b620-bb1383d629e0"
    }
}


