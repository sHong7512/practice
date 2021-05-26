package com.shong.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

class Ex_JsonParsing : AppCompatActivity() {
    val TAG = "ex_jsonParsing_TAG"
    val c_scope = CoroutineScope(Dispatchers.Default)
    lateinit var api_txt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ex__json_parsing)

        api_txt = findViewById(R.id.api_text)

        val url: URL? = try {
            URL("https://pastebin.com/raw/2bW31yqa")
        }catch (e: MalformedURLException){
            Log.d("Exception", e.toString())
            null
        }

        //let 은 null이 아닌 경우에만 사용하는 범위지정함수
        url?.let{ api_txt.text = it.toString() }

        var isRunning_co = true
        // io dispatcher for networking operation
        c_scope.launch(Dispatchers.IO) {
            url?.getString()?.apply{

                // default dispatcher for json parsing, cpu intensive work
                withContext(Dispatchers.Default){

                    //가공되지 않은 데이터 확인
                    Log.d(TAG,"${this@apply}")
                    //데이터 가공
                    val list = parseJson(this@apply) ?: return@withContext

                    // main dispatcher for interaction with ui objects
                    withContext(Dispatchers.Main){
                        api_txt.append("\n\n")

                        //List, Set, Map을 반복할땐 for보다 forEach문이 퍼포먼스 우세
                        //list.asSequence()로 크기가 큰 list 최적화 가능
                        list.forEach {
                            api_txt.append("\n${it.firstName} ${it.lastName} ${it.age}")
                        }
                    }
                }

            } ?: run {
                Log.d(TAG, "URL is null! launch Close!")
                return@launch
            }
            Log.d(TAG,"launch FINISH!")
            isRunning_co = false
        }

        Log.d(TAG, "Code END")

        c_scope.launch {
            while(isRunning_co){
                delay(100L)
            }
            prac_Parsing()
        }
    }


    fun URL.getString() : String? {
        val stream = openStream()

        return try{
            val r = BufferedReader(InputStreamReader(stream))
            val result = StringBuilder()
            var line: String?
            while (r.readLine().also { line = it } != null) {
                result.append(line).appendLine()
            }
            result.toString()
        }catch (e: IOException){
            e.toString()
        }
    }

    data class Student(
            val firstName : String,
            val lastName : String,
            val age : Int
    )

    // parse json data
    fun parseJson(data : String) : List<Student>?{
        val list = mutableListOf<Student>()

        try {
//            JSON의 array는 []로 구성
//            JSON의 object는 {}로 구성
//            만약 Array의 이름이 없다면 val arr = JSONArray(data)로 새롭게 생성해주면 된다.
            val array = JSONObject(data).getJSONArray("students")

            for(i in 0 until array.length()){
                val obj = JSONObject(array[i].toString())
                val firstName = obj.getString("firstname")
                val lastName = obj.getString("lastname")
                val age = obj.getInt("age")
                list.add(Student(firstName,lastName,age))
            }
        }catch (e: JSONException){
            Log.d("Exception", e.toString())
        }

        return list
    }




    fun prac_Parsing(){
        val url: URL? = try {
            URL("https://jsonplaceholder.typicode.com/users")
        }catch (e: MalformedURLException){
            Log.d("Exception", e.toString())
            null
        }

        url?.let { Log.d(TAG,"${it}") }

        CoroutineScope(Dispatchers.IO).launch {
            url?.getString()?.apply {
                withContext(Dispatchers.Default){
                    Log.d(TAG,"${this@apply}")
                    prac_Parsing_Json(this@apply)
                }
            } ?: return@launch

        }
    }

    fun prac_Parsing_Json(data : String){
        try{
            //JSON 배열의 이름이 없는 경우 JSONArray(data)를 통해서 new배열을 만들어 주면 된다.
            val arr = JSONArray(data)
            for(i in 0 until arr.length()){
//                Log.d(TAG, arr[i].toString())
                val obj = JSONObject(arr[i].toString())
                Log.d(TAG,"id : ${obj.getString("id")}")
                Log.d(TAG,"name : ${obj.getString("name")}")
                Log.d(TAG, "street : ${obj.getJSONObject("address").getString("street")}")
            }
        }catch (e : JSONException){
            Log.d(TAG, e.toString())
        }
    }

}