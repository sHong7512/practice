package com.shong.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val TAG = "${this::class.java.simpleName}_shong"
    val c_scope : CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun <T : View?> findViewById(id: Int): T {
        Log.d(TAG,"findViewById : ${id}")
        return super.findViewById(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txt1 : TextView = findViewById(R.id.textView1)
        txt1.text = "hello uangel~!"

        val btn1 : Button = findViewById(R.id.button1)
        btn1.setOnClickListener {onClick(btn1)}
        val btn2 : Button = findViewById(R.id.button2)
        btn2.setOnClickListener {onClick(btn2)}
        val btn3 : Button = findViewById(R.id.button3)
        btn3.setOnClickListener {onClick(btn3)}
        val btn4 : Button = findViewById(R.id.button4)
        btn4.setOnClickListener {onClick(btn4)}
        val btn5 : Button = findViewById(R.id.button5)
        btn5.setOnClickListener {onClick(btn5)}


        Log.d(TAG, "${ex_Lambda(1,2)}")

        with(c_scope){
            launch {
                ex("ex_1", 2000L)
                Log.d(TAG, "c_scope1 middle!")
                ex("ex_2", 500L)
            }
            launch {
                ex("ex_3",1000L)
            }
        }

        Log.d(TAG,"CODE END!")
    }


    suspend fun ex(name : String, delayTime : Long) = coroutineScope {
        launch {
            Log.d(TAG,"${name} In !")
            delay(delayTime)
            Log.d(TAG,"${name} Out !")
        }
    }


    fun ex_Lambda(a : Int, b : Int) : Int{
        val sum = { x : Int, y : Int -> Log.d(TAG, "ex_Lambda execute")
            x + y
        }
        return sum(a,b)
    }


    fun onClick(v: View) {
        when (v.id) {
            R.id.button1 -> {
                val intent = Intent(this,Ex_JsonParsing::class.java)
                startActivity(intent)
            }
            R.id.button2 -> {
                val intent = Intent(this,Ex_foregroundService::class.java)
                startForegroundService(intent)
            }
            R.id.button3 -> {
                val intent = Intent(this,Ex_foregroundService::class.java)
                stopService(intent)
            }
            R.id.button4 ->{
                val intent = Intent(this,Ex_fragment::class.java)
                startActivity(intent)
            }
            R.id.button5 ->{
                val inflater = LayoutInflater.from(this)
                inflater.inflate(R.layout.item_dot,findViewById<LinearLayout>(R.id.inflateLinearLayout),true)
            }
            else -> {
                // else condition
            }
        }
    }

}