package com.bountyhunter.kudo.kudoposretail.rxjava

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject
import rx.Observable
import rx.functions.Func1
import io.realm.RealmResults
import rx.Observable.create


/**
 * Created by norman on 11/28/17.
 */
class RealmObservable {

    companion object {

        fun <T : RealmObject> prepare(context: Context, function: Func1<Realm, T>): Observable<T> {
            return create(object : OnSubscribeRealm<T>(context) {
                override operator fun get(realm: Realm): T {
                    return function.call(realm)
                }
            })
        }

        fun <T : RealmObject> results(context: Context, function: Func1<Realm, RealmResults<T>>): Observable<RealmResults<T>> {
            return create(object : OnSubscribeRealm<RealmResults<T>>(context) {
                override fun get(realm: Realm): RealmResults<T> {
                    return function.call(realm)
                }
            })
        }
    }

}