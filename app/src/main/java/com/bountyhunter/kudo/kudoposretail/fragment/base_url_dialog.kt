package com.bountyhunter.kudo.kudoposretail.fragment

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import kotlinx.android.synthetic.main.base_url_dialog_fragment.view.*

/**
 * Created by adrian on 11/30/17.
 */
class base_url_dialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val myView = inflater!!.inflate(R.layout.base_url_dialog_fragment,container,false)
        myView.btn_change_url_ok.setOnClickListener({
            var url = myView.tv_dialog_input.text.toString();
            RestAPI.changeBaseUrl(url)
            dismiss()
        })
        myView.btn_change_url_mock.setOnClickListener({
            RestAPI.changeMock()
            dismiss()
        })
        return myView
    }
}