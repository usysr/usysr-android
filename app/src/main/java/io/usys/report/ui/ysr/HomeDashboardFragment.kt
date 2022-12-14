package io.usys.report.ui.ysr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.RealmObject
import io.usys.report.R
import io.usys.report.databinding.FragmentDashboardBinding
import io.usys.report.ui.onClickReturnViewRealmObject
import io.usys.report.ui.ysr.service.setupServiceList
import io.usys.report.ui.ysr.sport.setupSportList
import io.usys.report.utils.sessionSports


/**
 * Created by ChazzCoin : October 2022.
 */

class HomeDashboardFragment : YsrFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    var itemOnClickSportList: ((View, RealmObject) -> Unit)? = onClickReturnViewRealmObject()
    var itemOnClickServiceList: ((View, RealmObject) -> Unit)? = onClickReturnViewRealmObject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        rootView = binding.root
//        setupOnClickListeners()
        toFragment(R.id.navigation_tryout_frag)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupSportsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSportsList() {
        _binding?.dashboardRecyclerServiceList?.setupServiceList(requireContext(), itemOnClickServiceList)
        _binding?.recyclerSportList?.setupSportList(requireContext(), itemOnClickSportList)
    }

    override fun setupOnClickListeners() {
        itemOnClickSportList = { _, obj ->
            toFragment(R.id.navigation_org_list, bundleRealmObject(obj))
        }

        itemOnClickServiceList = { _, obj ->
            toFragment(R.id.navigation_service_details, bundleRealmObject(obj))
        }

    }

}

