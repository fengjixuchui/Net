/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.drake.brv.BindingAdapter
import com.drake.brv.PageRefreshLayout
import com.drake.net.BuildConfig
import com.drake.net.NetConfig
import com.drake.net.R
import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.drake.net.toast
import com.yanzhenjie.kalle.exception.*
import io.reactivex.observers.DefaultObserver

/**
 * 自动结束下拉刷新和上拉加载状态
 * 自动展示缺省页
 * 自动分页加载
 */
abstract class PageObserver<M>(val pageRefreshLayout: PageRefreshLayout) : DefaultObserver<M>() {

    companion object {
        internal var onPageError: Throwable.(pageRefreshLayout: PageRefreshLayout) -> Unit = {

            val message = when (this) {
                is NetworkError -> NetConfig.app.getString(R.string.network_error)
                is URLError -> NetConfig.app.getString(R.string.url_error)
                is HostError -> NetConfig.app.getString(R.string.host_error)
                is ConnectTimeoutError -> NetConfig.app.getString(R.string.connect_timeout_error)
                is ReadException -> NetConfig.app.getString(R.string.read_exception)
                is WriteException -> NetConfig.app.getString(R.string.write_exception)
                is ConnectException -> NetConfig.app.getString(R.string.connect_exception)
                is ReadTimeoutError -> NetConfig.app.getString(R.string.read_timeout_error)
                is DownloadError -> NetConfig.app.getString(R.string.download_error)
                is NoCacheError -> NetConfig.app.getString(R.string.no_cache_error)
                is ParseError -> NetConfig.app.getString(R.string.parse_error)
                is RequestParamsException -> NetConfig.app.getString(R.string.request_error)
                is ServerResponseException -> NetConfig.app.getString(R.string.server_error)
                is ResponseException -> msg
                else -> {
                    NetConfig.app.getString(R.string.other_error)
                }
            }

            if (BuildConfig.DEBUG) {
                printStackTrace()
            }

            when (this) {
                is ParseError, is ResponseException -> {
                    NetConfig.app.toast(message)
                }
            }
        }
    }

    init {
        pageRefreshLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        pageRefreshLayout.showError()
        onPageError.invoke(e, pageRefreshLayout)
    }

    override fun onComplete() {
    }

    /**
     * 自动判断是添加数据还是覆盖数据
     *
     * @param data 要添加的数据集
     */
    fun addData(data: List<Any>, hasMore: BindingAdapter.() -> Boolean) {
        pageRefreshLayout.addData(data, hasMore)
    }


    fun showEmpty() {
        pageRefreshLayout.showEmpty()
    }
}