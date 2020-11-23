package com.application.seb.binfinder.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.fragments.cleanEventList.CleanEventListFragment

private const val PAGE_NUMBER = 2

class PageAdapter(fragmentManager: FragmentManager?, var context: Context) : FragmentStatePagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int { return PAGE_NUMBER }

    override fun getItem(position: Int): Fragment {
        var fragment = CleanEventListFragment()
        when (position) {
             0 -> fragment = CleanEventListFragment.newInstance(isForSoonCleansEvents = true, isForUserCleansEvents = false)
             1 -> fragment = CleanEventListFragment.newInstance(isForSoonCleansEvents = false, isForUserCleansEvents = true)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val tab = arrayOf(context.getString(R.string.page_title_soon), context.getString(R.string.page_title_my_clean_events))
        return tab[position]
    }
}
