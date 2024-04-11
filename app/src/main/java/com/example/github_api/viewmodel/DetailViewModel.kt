package com.example.github_api.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_api.data.response.DetailUserResponse
import com.example.github_api.data.response.ListFollowResponse
import com.example.github_api.data.response.ListFollowResponseItem
import com.example.github_api.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel () : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ListFollowResponseItem>>()
    val listFollowers: LiveData<List<ListFollowResponseItem>> = _listFollowers

    private val _listFollowings = MutableLiveData<List<ListFollowResponseItem>>()
    val listFollowings: LiveData<List<ListFollowResponseItem>> = _listFollowings

    private val _listDetailFollowers = MutableLiveData<List<DetailUserResponse>>(emptyList())
    val listDetailFollowers: LiveData<List<DetailUserResponse>> = _listDetailFollowers

    private val _listDetailFollowings = MutableLiveData<List<DetailUserResponse>>(emptyList())
    val listDetailFollowings: LiveData<List<DetailUserResponse>> = _listDetailFollowings



    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
    }

    init {
        getListFollowers("Fadzaa")
        getListFollowings("Fadzaa")
    }

    private fun getListFollowers(username: String) {
        _isLoading.value = true
        val client =  ApiConfig.getApiService().getUserFollowers(username)

        client.enqueue(
            object : Callback<ListFollowResponse> {
                override fun onResponse(call: Call<ListFollowResponse>, response: Response<ListFollowResponse>, ) {
                    if (response.isSuccessful) {
                        val listFollowers = response.body()?.listFollowResponse
                        if (listFollowers != null) {
                            for (followers in listFollowers) {
                                getDetailFollow(followers.login, _listDetailFollowers)
                            }
                        }
                        _isLoading.value = false
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ListFollowResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }


            }
        )

    }

    private fun getListFollowings(username: String) {
        _isLoading.value = true
        val client =  ApiConfig.getApiService().getUserFollowings(username)

        client.enqueue(
            object : Callback<ListFollowResponse> {
                override fun onResponse(
                    call: Call<ListFollowResponse>,
                    response: Response<ListFollowResponse>, ) {
                    if (response.isSuccessful) {
                        val listFollowings = response.body()?.listFollowResponse
                        if (listFollowings != null) {
                            for (following in listFollowings) {
                                getDetailFollow(following.login, _listDetailFollowings)
                            }
                        }
                        _isLoading.value = false
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ListFollowResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            }
        )
    }

    private fun getDetailFollow(username: String, listDetailFollow: MutableLiveData<List<DetailUserResponse>>) {

        val client = ApiConfig.getApiService().getUserDetail(username)

        client.enqueue(
            object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>,
                ) {
                    if (response.isSuccessful) {
                        val userDetail = response.body()
                        Log.d(TAG, "onResponse: $userDetail")
                        if (userDetail != null) {
                            listDetailFollow.value = listDetailFollow.value?.plus(userDetail)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            }
        )
    }


}