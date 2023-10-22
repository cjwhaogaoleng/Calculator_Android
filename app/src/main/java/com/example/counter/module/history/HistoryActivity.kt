package com.example.counter.module.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.counter.R
import com.example.counter.bean.DataBean
import com.example.counter.databinding.ActivityHistoryBinding
import com.example.counter.module.advanced.AdvancedViewModel
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private lateinit var viewModel: AdvancedViewModel

    private lateinit var lv: ListView
     var history = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get<AdvancedViewModel>(
            AdvancedViewModel::class.java
        )
        getHistory()
        initView()

    }


    private fun initView() {
        lv = findViewById<ListView>(R.id.lv_history)
    }
    private fun subscribe() {


        if (history.isEmpty()){
            history.add("There is no historical record yet.")
        }
            else {
            history.remove(history.last())
            history.reverse()
            if (history.size > 20) {
                 history.subList(20,history.size).clear()
            }
        }

        var adapter = MyListViewAdapter(this, history)
        lv.adapter = adapter
    }

    private fun getHistory()  {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val body = FormBody.Builder()
            .build()

        val request =
            Request.Builder().url("http://8.130.110.171:80/read_all_history/").post(body)
                .build()

        val call = okHttpClient?.newCall(request)

        var bean: DataBean = DataBean()
        Thread {
            val response = call?.execute()
            bean =
                Gson().fromJson<DataBean>(response!!.body()!!.string(), DataBean::class.java)
            this?.runOnUiThread {
                history = bean.result.split(",") as ArrayList<String>
                subscribe()
            }

        }.start()


    }
}