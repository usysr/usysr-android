package io.usys.report.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.usys.report.firebase.FireTypes
import io.usys.report.firebase.fireAddUpdateDBAsync
import io.usys.report.model.AuthTypes.Companion.UNASSIGNED
import io.usys.report.utils.*
import java.io.Serializable

/**
 * Created by ChazzCoin : October 2022.
 */

open class Organization : RealmObject(), Serializable {

    companion object {
        const val ORDER_BY_SPORTS = "sport"
    }


    @PrimaryKey
    var id: String = newUUID()
    var dateCreated: String = getTimeStamp()
    var name: String = "" //Name Given by Manager
    var addressOne: String = "" // 2323 20th Ave South
    var addressTwo: String = "" // 2323 20th Ave South
    var city: String = "" // Birmingham
    var state: String = "" // AL
    var zip: String = "" // 35223
    var sport: String = UNASSIGNED
    var type: String = UNASSIGNED
    var subType: String = UNASSIGNED
    var ratingScore: String = "0.0"
    var ratingCount: String = "0"
    var details: String = ""
    var officeHours: String = ""
    var websiteUrl: String = ""
    var imgOrgIconUri: String = ""
    var managerId: String = UNASSIGNED
    var managerName: String = UNASSIGNED
    var estMemberCount: String = ""
    var estStaffCount: String = ""
    var staffIds: RealmList<String>? = null
    var reviewIds: RealmList<String>? = null
    var leagueIds: RealmList<String>? = null
    var regionIds: RealmList<String>? = null
    var locationIds: RealmList<String>? = null
    var imgUris: RealmList<String>? = null

    fun getCityStateZip(): String {
        return "$city, $state $zip"
    }

    fun addUpdateOrgToFirebase(): Boolean {
        return fireAddUpdateDBAsync(FireTypes.ORGANIZATIONS, this.id, this)
    }

}

fun createOrg() {
    val org = Organization()
    org.apply {
        this.sport = "soccer"
        this.city = "birmingham"
        this.name = "USYSR Club"
    }
    fireAddUpdateDBAsync(FireTypes.ORGANIZATIONS, org.id, org)
}
