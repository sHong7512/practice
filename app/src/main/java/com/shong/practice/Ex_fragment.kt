package com.shong.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class Ex_fragment : AppCompatActivity() {
    val TAG = "Ex_fragment_TAG"

    val fragmentA = Fragment_A()
    val fragmentB = Fragment_B()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ex_fragment)


        val f_btn1 = findViewById<Button>(R.id.f_button1)
        f_btn1.setOnClickListener { onClick(f_btn1)}
        val f_btn2 = findViewById<Button>(R.id.f_button2)
        f_btn2.setOnClickListener { onClick(f_btn2) }
    }


    fun onClick(v : View){
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        when(v.id){
            R.id.f_button1 -> { transaction.replace(R.id.frameLayout, fragmentA) }
            R.id.f_button2 -> { transaction.replace(R.id.frameLayout, fragmentB) }
            else -> { Log.d(TAG,"onCLick ERROR") }
        }
//        transaction.addToBackStack(null)
        transaction.commit()
    }

}