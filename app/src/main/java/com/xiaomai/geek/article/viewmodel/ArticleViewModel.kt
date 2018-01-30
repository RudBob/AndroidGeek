package com.xiaomai.geek.article.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.application.InitializeService
import com.xiaomai.geek.article.db.Article
import com.xiaomai.geek.article.model.ArticleRemoteDataSource
import com.xiaomai.geek.article.model.ArticleRepository
import com.xiaomai.geek.article.model.ArticleResponse
import com.xiaomai.geek.base.BaseObserver
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseViewModel(context) {

    private var articleRepository: ArticleRepository? = null

    private var articles: MutableLiveData<List<ArticleResponse>> = MutableLiveData()

    fun getArticles() = articles

    fun loadArticles() {
        pageStatus.value = PageStatus.LOADING
        if (articleRepository == null) {
            articleRepository = ArticleRepository(ArticleRemoteDataSource())
        }
        articleRepository?.let {
            it.getArticles()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseObserver<List<ArticleResponse>>(this@ArticleViewModel) {
                        override fun onNext(t: List<ArticleResponse>) {
                            t.forEach {
                                val article = Article(null, it.name, it.url)
                                InitializeService.getArticleDaoSession()?.articleDao?.insert(article)
                            }

                            articles.value = t
                            pageStatus.value = if (t.isEmpty()) PageStatus.EMPTY else PageStatus.NORMAL
                        }
                    })
        }
    }
}