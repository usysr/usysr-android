package io.usys.report.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.usys.report.R
import io.usys.report.model.*
import io.usys.report.utils.*
import io.realm.RealmList
import io.realm.RealmObject
import io.usys.report.db.FireDB
import io.usys.report.db.FireTypes
import kotlinx.android.synthetic.main.fragment_org_list.view.*

/**
 * Created by ChazzCoin : October 2022.
 */

class OrganizationListFragment : YsrFragment() {

    private var hasBeenLoaded = false
    private var organizationList: RealmList<Organization> = RealmList() // -> ORIGINAL LIST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_org_list, container, false)

        setupOnClickListeners()

        if (!hasBeenLoaded) {
            getOrganizationsBySport((realmObjectArg as? Sport)?.name)
            hasBeenLoaded = true
        } else {
            rootView.recyclerOrgList.initRealmList(organizationList, requireContext(), FireTypes.ORGANIZATIONS, itemOnClick)
        }


        return rootView
    }

    //todo: navigate to org profile and coaching list...
    override fun setupOnClickListeners() {
        itemOnClick = { view, obj ->
            toFragment(R.id.navigation_coaches_list, bundleRealmObject(obj))
        }
    }

    private fun getOrganizationsBySport(sport:String?) {
        firebase { it ->
            it.child(FireDB.ORGANIZATIONS).orderByChild("sport").equalTo(sport)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        organizationList.clear()
                        for (ds in dataSnapshot.children) {
                            val org: Organization? = ds.getValue(Organization::class.java)
                            org?.let {
                                organizationList.add(it)
                            }
                        }
                        main {
                            rootView.recyclerOrgList.initRealmList(organizationList, requireContext(), FireTypes.ORGANIZATIONS, itemOnClick)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        log("Failed")
                    }
                })
        }
    }






}
