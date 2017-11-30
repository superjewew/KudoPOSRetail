package com.bountyhunter.kudo.kudoposretail.rxjava

import android.content.Context
import io.realm.Realm
import io.realm.exceptions.RealmException
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions


/**
 * Created by norman on 11/28/17.
 */
abstract class OnSubscribeRealm<T>(context: Context) : Observable.OnSubscribe<T> {
    private var context: Context? = context

    override fun call(subscriber: Subscriber<in T>) {
        val realm = Realm.getDefaultInstance()
        subscriber.add(Subscriptions.create {
            try {
                realm.close()
            } catch (ex: RealmException) {
                subscriber.onError(ex)
            }
        })

        val `object`: T?
        realm.beginTransaction()
        try {
            `object` = get(realm)
            realm.commitTransaction()
        } catch (e: RuntimeException) {
            realm.cancelTransaction()
            subscriber.onError(RealmException("Error during transaction.", e))
            return
        } catch (e: Error) {
            realm.cancelTransaction()
            subscriber.onError(e)
            return
        }

        if (`object` != null) {
            subscriber.onNext(`object`)
        }
        subscriber.onCompleted()
    }

    abstract operator fun get(realm: Realm): T
}