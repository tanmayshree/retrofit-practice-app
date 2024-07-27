package com.practice.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.retrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Url

class MainActivity : AppCompatActivity() {

    private var baseUrl = "https://jsonplaceholder.typicode.com/"
    lateinit var mainBinding : ActivityMainBinding

    var postsList = ArrayList<Posts>()
    lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        setContentView(view)

        showPosts()

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showPosts() {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI : RetrofitAPI = retrofit.create(RetrofitAPI::class.java)

        val call :  Call<List<Posts>> = retrofitAPI.getAllPosts()
        call.enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                if(response.isSuccessful) {

                    mainBinding.progressBar3.isVisible = false
                    mainBinding.recyclerView.isVisible = true
                    postsList = response.body() as ArrayList<Posts>

                    adapter = PostsAdapter(postsList)
                    mainBinding.recyclerView.adapter = adapter

                }

            }

            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })

    }
}