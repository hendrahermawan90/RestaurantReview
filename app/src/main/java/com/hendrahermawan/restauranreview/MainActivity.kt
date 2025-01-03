package com.hendrahermawan.restauranreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.hendrahermawan.restauranreview.data.response.CustomerReviewsItem
import com.hendrahermawan.restauranreview.data.response.PostReviewResponse
import com.hendrahermawan.restauranreview.data.response.Restaurant
import com.hendrahermawan.restauranreview.data.response.RestaurantResponse
import com.hendrahermawan.restauranreview.data.retrofit.ApiConfig
import com.hendrahermawan.restauranreview.databinding.ActivityMainBinding
import com.hendrahermawan.restauranreview.ui.ReviewAdapter
import okhttp3.Response
import java.util.concurrent.Callable
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var  binding: ActivityMainBinding

    companion object{
        private const val  TAG = "MainActivity"
        private const val  RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       supportActionBar?.hide()

        val layoutManager =LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        val itemDescription = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDescription)

        val btnMoveActivitywithData: Button = findViewById(R.id.btn_move_activity_data)
        btnMoveActivitywithData.setOnClickListener(this)



        findRestaurant()

        //kirim review
        binding.btnSend.setOnClickListener { view ->
            postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_move_activity_data -> {
                val moveIntentWithData = Intent(this, MoveActivityWithData::class.java)
                moveIntentWithData.putExtra(MoveActivityWithData.YOUR_NAME, "Hendra Hermawan")
                moveIntentWithData.putExtra(MoveActivityWithData.YOUR_NIM, 312310553)
                moveIntentWithData.putExtra(MoveActivityWithData.YOUR_CLASS, "TI.23.B2")
                moveIntentWithData.putExtra(MoveActivityWithData.YOUR_AGE, 34)
                moveIntentWithData.putExtra(
                    MoveActivityWithData.YOUR_EMAIL,
                    "hendra.031290@gmail.com"
                )

                startActivity(moveIntentWithData)
            }
        }
    }
    private fun findRestaurant() {
        // tampilkan loading
        showLoading(true)
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : retrofit2.Callback<RestaurantResponse>{
            override fun onResponse(
                call: retrofit2.Call<RestaurantResponse>,
                response: retrofit2.Response<RestaurantResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setRestauranData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<RestaurantResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }


        })

    }

    private fun setReviewData (consumerreview : List<CustomerReviewsItem>){
        val adapter = ReviewAdapter()
        adapter.submitList(consumerreview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun  showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setRestauranData (restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description

        Glide.with((this))
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.vPicture)
    }

    private fun postReview(review : String){
        showLoading(false)
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Hendra Hermawan", review)
        client.enqueue(object : Callback<PostReviewResponse>{
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: retrofit2.Response<PostReviewResponse>
            ) {
                showLoading(false)
                val responseBody =response.body()
                if (response.isSuccessful && responseBody != null){
                    setReviewData(responseBody.customerReviews)
                }else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}