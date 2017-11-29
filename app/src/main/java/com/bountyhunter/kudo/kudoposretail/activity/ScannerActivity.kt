package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bountyhunter.kudo.kudoposretail.CatalogLocalDataSource
import com.bountyhunter.kudo.kudoposretail.CatalogRemoteDataSource
import com.bountyhunter.kudo.kudoposretail.CatalogRepository
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import io.realm.Realm
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class ScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler {

    lateinit var mScannerView :ZBarScannerView

    private val catalogRepo by lazy {
        CatalogRepository(CatalogLocalDataSource(this), CatalogRemoteDataSource())
    }

    private val compositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZBarScannerView(this)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
        if(compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear()
        }
    }

    override fun handleResult(rawResult: Result?) {
        Log.v("SCANNER", rawResult?.contents) // Prints scan results
        Log.v("SCANNER", rawResult?.barcodeFormat?.name) // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this)

        var disposable = catalogRepo.getProduct(rawResult?.contents!!.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { data ->
                            Realm.getDefaultInstance().use {
                                it.executeTransaction {
                                    val item = it.createObject(CartItem::class.java)
                                    item.fromProduct(data)
                                }
                            }
                        },
                        {

                        },
                        {
                            finish()
                        })

//        Realm.getDefaultInstance().use {
//            it.executeTransaction {
//                val item = it.createObject(CartItem::class.java)
//                item.mItemId = 1001
//                item.mItemName = "Test Product"
//                item.mItemPrice = 100000.0
//                item.mItemQuantity = 1
//                item.mItemImage = "https://img2.exportersindia.com/product_images/bc-full/dir_22/653236/detol-hand-wash-1833868.jpeg"
//                item.mItemStock = 10
//            }
//        }

        compositeSubscription.add(disposable)

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ScannerActivity::class.java)
    }
}
