package com.falcotech.mazz.promise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.falcotech.mazz.promiselibrary.PromiseUtils
import com.falcotech.mazz.promiselibrary.then
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()
    }

    private fun test(){
        btnTest.setOnClickListener {
            Log.d("DEBUG", "Clicky")
            val uiPromise = PromiseUtils.ofUi{
                debug()
                tvHelloWorld.text = "COCKS"
            }
            PromiseUtils.test(PromiseUtils.ofBg {
                "TITS"
            }.then {
                "DICKS-$it"
            }.then {
                Log.d("DEBUG", "final concat = $it")
                "WEINER"
            }.then {
                Log.d("DEBUG", "final text = $it")
                uiPromise
            })
        }
    }
    private fun debug(){
        Log.d("DEBUG", "myLooper = " + Looper.myLooper())
        Log.d("DEBUG", "mainLooper = " + Looper.getMainLooper())
    }
}
