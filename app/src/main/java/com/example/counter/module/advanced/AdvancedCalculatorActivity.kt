package com.example.counter.module.advanced

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.counter.R
import com.example.counter.databinding.ActivityAdvancedCaculatorBinding
import com.example.counter.module.main.MainActivity
import com.example.counter.util.StatusBar

class AdvancedCalculatorActivity : AppCompatActivity() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding:ActivityAdvancedCaculatorBinding

    }

    private val viewModel : AdvancedViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(AdvancedViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statusBar = StatusBar(this)
        //设置颜色为半透明
        statusBar.setColor(R.color.translucent)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_advanced_caculator)


        binding.lifecycleOwner = this
        binding.click = viewModel
        viewModel.context = this

        initView()
    }

    private fun initView() {
        binding.toolbarAdvanced.title=""  //标题栏文字
//        binding.toolbarAdvanced.setTitleTextColor(Color.RED) //标题栏文字颜色
//        binding.toolbar.setLogo(R.mipmap.ic_launcher) //标题栏左侧logo
//        binding.toolbar.subtitle="副标题"  //标题栏副标题文字
//        binding.toolbarAdvanced.setSubtitleTextColor(Color.GREEN)  //标题栏副标题文字颜色
        binding.toolbarAdvanced.setBackgroundResource(R.color.transparent)  //标题栏背景
        setSupportActionBar(binding.toolbarAdvanced)  //Toolbar替换系统自带的ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        binding.toolbar.setNavigationIcon(R.mipmap.ic_launcher) //工具栏左侧导航图标,通常用作返回按钮
        //左侧导航(返回键)点击事件，需要放在setSupportActionBar(mTooBar)之后执行,不然不起作用
        binding.toolbarAdvanced.setNavigationOnClickListener {
            this.finish()
        }
    }
}