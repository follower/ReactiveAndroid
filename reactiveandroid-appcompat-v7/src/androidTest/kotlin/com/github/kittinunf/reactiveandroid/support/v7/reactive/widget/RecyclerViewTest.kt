package com.github.kittinunf.reactiveandroid.support.v7.reactive.widget

import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.reactiveandroid.support.v7.reactive.activity.RecyclerViewTestActivity
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(RecyclerViewTestActivity::class.java)

    val instrumentation = InstrumentationRegistry.getInstrumentation()

    lateinit var view: RecyclerView

    @Before
    fun before() {
        view = activityRule.activity.recyclerView
    }

    @Test
    @Ignore
    fun scrollStateChanged() {
        val test = view.rx.scrollStateChanged().test()

        activityRule.activity.setItemsAdapter()

        instrumentation.runOnMainSync {
            view.smoothScrollToPosition(10)
        }

        test.awaitCount(2, {}, 2000)

        val values = test.values()
        val last = values.last()
        assertThat(last.recyclerView, equalTo(view))
    }

    @Test
    fun scrolled() {
        val test = view.rx.scrolled().test()

        activityRule.activity.setItemsAdapter()

        instrumentation.runOnMainSync {
            view.scrollBy(0, 100)
        }

        test.awaitCount(2)

        val values = test.values()
        val last = values.last()
        assertThat(last.dx, equalTo(0))
        assertThat(last.dy, equalTo(100))
    }

    @Test
    @Ignore
    fun recycler() {
        val test = view.rx.recycler().test()

        activityRule.activity.setItemsAdapter()

        instrumentation.runOnMainSync {
            view.smoothScrollToPosition(50)
        }

        test.awaitCount(1, {}, 2000)

        val values = test.values()
        val last = values.last()

        assertThat(last.itemView, notNullValue())
    }

    @Test
    fun childViewAttachedDetached() {
        val attach = view.rx.childViewAttachedToWindow().test()
        val detach = view.rx.childViewDetachedFromWindow().test()

        val child = activityRule.activity.child
        activityRule.activity.setItem1Adapter(child)

        attach.awaitCount(1)
        attach.assertValueCount(1)
        attach.assertValue(ChildAttachStateChange.ChildViewAttachedToWindow(child))

        activityRule.activity.unsetAdapter()
        detach.awaitCount(1)
        detach.assertValueCount(1)
        detach.assertValue(ChildAttachStateChange.ChildViewDetachedFromWindow(child))

        attach.dispose()
        detach.dispose()

        activityRule.activity.setItem1Adapter(child)
        attach.assertValueCount(1)
        detach.assertValueCount(1)
    }

    @Test
    fun touchEvent() {
    }

    @Test
    fun interceptTouchEvent() {
    }

    @Test
    @Ignore
    fun changed() {
        val change = view.rx.changed().test()

        val newItem = (1..5).toList()

        activityRule.activity.setItemsAdapter()
        activityRule.activity.items = newItem

        instrumentation.runOnMainSync {
            view.adapter.notifyDataSetChanged()
        }

        change.awaitCount(1, {}, 2000)
    }

    @Test
    fun itemChanged() {
    }

    @Test
    fun itemInserted() {
    }

    @Test
    fun itemMoved() {
    }

    @Test
    fun itemRemoved() {
    }

    @Test
    @UiThreadTest
    fun adapter() {
        val expected = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            }

            override fun getItemCount(): Int = 1

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
                    object : RecyclerView.ViewHolder(View(instrumentation.context)) {}
        }

        Single.just(expected).subscribe(view.rx.adapter)

        assertThat(view.adapter, equalTo(expected as RecyclerView.Adapter<*>))
    }

    @Test
    @UiThreadTest
    fun itemAnimator() {
        val expected = DefaultItemAnimator()

        Single.just(expected).subscribe(view.rx.itemAnimator)

        assertThat(view.itemAnimator, equalTo(expected as RecyclerView.ItemAnimator))
    }

    @Test
    @UiThreadTest
    fun layoutManager() {
        val expected = LinearLayoutManager(instrumentation.context)

        Single.just(expected).subscribe(view.rx.layoutManager)

        assertThat(view.layoutManager, equalTo(expected as RecyclerView.LayoutManager))
    }
}