package com.example.github_api.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.github_api.R
import com.example.github_api.data.response.DetailUserResponse
import com.example.github_api.databinding.ActivityMainBinding
import com.example.github_api.ui.adapter.ListUserAdapter
import com.example.github_api.viewmodel.MainViewModel
import com.example.github_api.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var listUserAdapter: ListUserAdapter

    private val mainViewModel by viewModels<MainViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.user.observe(this) {
            setCurrentUser(it)
        }

        mainViewModel.isLoading.observe(this) {
            setHeadingLoading(it)
        }

        searchViewModel.listDetailUsers.observe(this) {
            setRvUser(it)
        }

        searchViewModel.isLoading.observe(this) {
            setLoading(it)
        }

        searchUser()

        navigateToDetailUser()

    }

    private fun setCurrentUser(detailUserResponse: DetailUserResponse) {
        val followers = detailUserResponse.followers
        val following = detailUserResponse.following
        val repository = detailUserResponse.publicRepos

        binding.tvUserFullname.text = detailUserResponse.name
        binding.tvFollowers.text = getString(R.string.followers, followers)
        binding.tvFollowings.text = getString(R.string.followings, following)
        binding.tvPublicRepo.text = getString(R.string.public_repo, repository)
        Glide.with(this)
            .load(detailUserResponse.avatarUrl)
            .into(binding.civCurrentUser)
    }

    private fun setRvUser(listUser: List<DetailUserResponse>) {
        with(binding) {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            listUserAdapter = ListUserAdapter(listUser)
            rvUsers.adapter = listUserAdapter
        }
    }

    private fun setHeadingLoading(isLoading: Boolean) {
        with(binding) {
            progressBarHeading.visibility = if (isLoading) View.VISIBLE else View.GONE
            tvGreeting.visibility = if (isLoading) View.GONE else View.VISIBLE
            tvUserFullname.visibility = if (isLoading) View.GONE else View.VISIBLE
            civCurrentUser.visibility = if (isLoading) View.GONE else View.VISIBLE
            ivArrow.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun searchUser() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{ _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchBar.text = searchView.text
                        searchView.hide()
                        searchViewModel.searchUsers(searchView.text.toString())
                        true
                    } else {
                        false
                    }
                }
        }
    }

    private fun navigateToDetailUser() {
        binding.ivArrow.setOnClickListener {
            val intent = Intent(this, FavouriteUserActivity::class.java)
//            intent.putExtra(DetailUserActivity.EXTRA_USER, mainViewModel.user.value)
            startActivity(intent)
        }
    }
}