package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by eduardalbu on 21.02.2018.
 */

class PagerAdapter(val context: Context, val manager: FragmentManager): FragmentPagerAdapter(manager) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mTitles = mutableListOf<String>()
    private var mShowTitle: Boolean = true

    fun setShowTitle(showTitle: Boolean) {
        mShowTitle = showTitle
    }

    override fun getItemPosition(`object`: Any?): Int = super.getItemPosition(`object`)

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = if (mShowTitle) mTitles.get(position) else null


    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mTitles.add(title)
        notifyDataSetChanged()
    }

    fun clearLists() {
        mFragmentList.clear()
        mTitles.clear()
    }
}