package com.example.github_api.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_api.model.remote.response.DetailUserResponse
import com.example.github_api.model.remote.ApiService
import com.example.github_api.model.repository.UserRepository

class DetailViewModel(username: String, apiService: ApiService, application: Application) : ViewModel() {
    private val detailRepository = UserRepository(apiService, application)

    private val _listDetailFollowers = MutableLiveData<List<DetailUserResponse>>(emptyList())
    val listDetailFollowers: LiveData<List<DetailUserResponse>> = _listDetailFollowers

    private val _listDetailFollowings = MutableLiveData<List<DetailUserResponse>>(emptyList())
    val listDetailFollowings: LiveData<List<DetailUserResponse>> = _listDetailFollowings

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    init {
        getListFollowers(username)
        getListFollowings(username)
        getUser(username)
    }

    private fun getListFollowers(username: String) {
        _isLoadingFollowers.value = true
        detailRepository.getListFollowers(username).observeForever {
            _listDetailFollowers.value = it
            _isLoadingFollowers.value = false
        }
    }

    private fun getListFollowings(username: String) {
        _isLoadingFollowing.value = true
        detailRepository.getListFollowings(username).observeForever {
            _listDetailFollowings.value = it
            _isLoadingFollowing.value = false
        }
    }

    private fun getUser(username: String) {
        detailRepository.getDetailUser(username).observeForever {
            _user.value = it
        }
    }

}