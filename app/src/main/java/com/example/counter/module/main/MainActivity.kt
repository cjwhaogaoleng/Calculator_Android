package com.example.counter.module.main


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.counter.R
import com.example.counter.databinding.ActivityMainBinding
import com.example.counter.module.advanced.AdvancedCalculatorActivity
import com.example.counter.module.history.HistoryActivity
import com.example.counter.util.StatusBar

class MainActivity : AppCompatActivity() {


    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMainBinding
    }

    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java) }



    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statusBar = StatusBar(this)
        //设置颜色为半透明
        statusBar.setColor(R.color.translucent);
        //设置颜色为透明
//        statusBar.setColor(R.color.transparent);
        //隐藏状态栏
//        statusBar.hide();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.myViewModel = viewModel
        viewModel.context = this

        initView()

        //toolbar隐藏
//        this.supportActionBar?.hide()



    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {
        binding.toolbar.title="Calculator"  //标题栏文字
        binding.toolbar.setTitleTextColor(Color.WHITE) //标题栏文字颜色
        binding.toolbar.titleMarginBottom = 80
//        binding.toolbar.setLogo(R.mipmap.ic_launcher) //标题栏左侧logo
//        binding.toolbar.subtitle="副标题"  //标题栏副标题文字
//        binding.toolbar.setSubtitleTextColor(Color.GREEN)  //标题栏副标题文字颜色
        binding.toolbar.setBackgroundResource(R.color.transparent)  //标题栏背景
        setSupportActionBar(binding.toolbar)  //Toolbar替换系统自带的ActionBar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.toolbar.setNavigationIcon(R.mipmap.ic_launcher) //工具栏左侧导航图标,通常用作返回按钮

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_advanced -> startActivity(
                Intent(
                    this,
                    AdvancedCalculatorActivity::class.java
                )
            )
            R.id.menu_history -> startActivity(Intent(this, HistoryActivity::class.java))

        }
        return true
    }
}
