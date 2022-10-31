package io.usys.report.utils

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import io.usys.report.model.*
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.usys.report.db.addUpdateDB
import io.usys.report.db.forceGetNameOfRealmObject

/**
 * Created by ChazzCoin : December 2019.
 */

/** -> TRIED AND TRUE! <- */
fun <T> RealmList<T>?.toMutableList() : MutableList<T> {
    val listOfT = mutableListOf<T>()
    this?.let {
        for (item in it) {
            listOfT.add(item)
        }
    }
    return listOfT
}

fun <K, V> HashMap<K, V>?.toRealmList() : RealmList<Any> {
    val listOfT = RealmList<Any>()
    this?.let {
        for ((_,value) in it) {
            listOfT.add(value as? Any)
        }
    }
    return listOfT
}


//fun HashMap<*,*>.toJsonRealmList(): RealmList<Any> {
//    var resultList: RealmList<Any> = RealmList()
//    for ((_,v) in this) {
//        val test = (v as? HashMap<*,*>)?.toJSON()
//        resultList.add(test)
//    }
//    return resultList
//}

inline fun <reified T> DataSnapshot.toRealmList(): RealmList<T> {
    val realmList: RealmList<T> = RealmList()
    for (ds in this.children) {
        val org: T? = ds.getValue(T::class.java)
        org?.let {
            realmList.add(org)
        }
    }
    return realmList
}

fun realm() : Realm {
    return Realm.getDefaultInstance()
}

//LAMBA FUNCTION -> Shortcut for realm().executeTransaction{ }
inline fun executeRealm(crossinline block: (Realm) -> Unit) {
    realm().executeTransaction { block(it) }
}

// in progress
inline fun <T : RealmModel> T.applyAndSave(block: (T) -> Unit) {
    this.apply {
        block(this)
    }
    this.cast<T>()?.let { itObj ->
        this.getRealmId<T>()?.let { itId ->
            addUpdateDB(itObj.forceGetNameOfRealmObject(), itId, itObj)
        }
    }
}

fun <T> RealmModel.getRealmId() : String? {
    val id = this.getAttribute<T>("id")
    if (id.isNullOrEmpty()) { return null }
    return id.toString()
}

// Untested
fun <T> DataSnapshot.toClass(clazz: Class<T>): T? {
    return this.getValue(clazz)
}

inline fun userOrLogout(activity: Activity? = null, block: (User) -> Unit) {
    val user = Session.getCurrentUser()
    user?.let {
        block(it)
    } ?: run {
        activity?.let {
            Session.restartApplication(it)
        }
    }
    //todo: get firebase user, if valid, set and continue
}

fun userOrLogout(activity: Activity? = null) {
    if (Session.user.isNullOrEmpty()) {
        activity?.let { Session.restartApplication(it) }
    }
    //todo: get firebase user, if valid, set and continue
}

fun sessionAndUser(block: (Session, User) -> Unit) {
    session { itSession ->
        userOrLogout { itUser ->
            block(itSession, itUser)
        }
    }
}

inline fun session(block: (Session) -> Unit) {
    Session.session?.let { block(it) }
}

inline fun sessionOrganizationList(block: (RealmList<Organization>) -> Unit) {
    Session.session?.organizations?.let { block(it) }
}


