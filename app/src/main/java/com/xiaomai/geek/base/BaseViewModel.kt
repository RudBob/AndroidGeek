package com.xiaomai.geek.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.common.PageStatus
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by wangce on 2018/1/29.
 */
open class BaseViewModel(context: Application) : AndroidViewModel(context) {
    val tag: String = javaClass.simpleName

    var pageStatus: MutableLiveData<PageStatus> = MutableLiveData()

    var snackMessage: MutableLiveData<String> = MutableLiveData()

    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun showSnackBar(message: String) {
        snackMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.apply {
            clear()
            compositeDisposable = null
        }
    }
}