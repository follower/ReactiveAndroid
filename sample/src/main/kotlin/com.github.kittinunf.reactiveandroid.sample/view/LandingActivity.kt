package com.github.kittinunf.reactiveandroid.sample.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.reactiveandroid.Property
import com.github.kittinunf.reactiveandroid.reactive.addTo
import com.github.kittinunf.reactiveandroid.sample.R
import com.github.kittinunf.reactiveandroid.scheduler.AndroidThreadScheduler
import com.github.kittinunf.reactiveandroid.widget.AdapterViewProxyAdapter
import com.github.kittinunf.reactiveandroid.widget.rx_itemsWith
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_landing.toolbar
import kotlinx.android.synthetic.main.activity_landing.toolbarSpinner
import kotlinx.android.synthetic.main.spinner_item.view.spinnerTextView
import kotlinx.android.synthetic.main.spinner_item_dropdown.view.spinnerTextViewDropdown
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class LandingActivity : AppCompatActivity() {

    val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        setUpToolbar()
        setUpButtons()
        setUpTextView()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)

        val property = Property(listOf("Item 1", "Item 2", "Item 3"))
        toolbarSpinner.rx_itemsWith(property.observable, ItemAdapter()).addTo(subscriptions)
        title = ""
    }

    private fun setUpButtons() {
//        toSignInPageButton.rx_click().map { SignInActivity::class }.subscribe(this::start).addTo(subscriptions)
//        toSignUpPageButton.rx_click().map { SignUpActivity::class }.subscribe(this::start).addTo(subscriptions)
//        toListPageButton.rx_click().map { RecyclerViewActivity::class }.subscribe(this::start).addTo(subscriptions)
//        toSectionListPageButton.rx_click().map { SectionRecyclerViewActivity::class }.subscribe(this::start).addTo(subscriptions)
//        toFragmentPagerButton.rx_click().map { FragmentPagerActivity::class }.subscribe { start(it) }.addTo(subscriptions)
//        toNestedListButton.rx_click().map { NestedRecyclerViewActivity::class }.subscribe { start(it) }.addTo(subscriptions)
    }

    private fun setUpTextView() {
        Observable.interval(5, TimeUnit.SECONDS, Schedulers.computation())
                .map(Long::toString)
                .observeOn(AndroidThreadScheduler.main)
//                .bindTo(resultTextView.rx_text)
//                .addTo(subscriptions)
    }

    fun start(clazz: KClass<*>) {
        startActivity(Intent(this, clazz.java))
    }

    inner class ItemAdapter : AdapterViewProxyAdapter<String>() {
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = convertView ?: LayoutInflater.from(this@LandingActivity).inflate(R.layout.spinner_item, parent, false)
            view.spinnerTextView.text = this[position]
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = convertView ?: LayoutInflater.from(this@LandingActivity).inflate(R.layout.spinner_item_dropdown, parent, false)
            view.spinnerTextViewDropdown.text = this[position]
            return view
        }
    }

}
