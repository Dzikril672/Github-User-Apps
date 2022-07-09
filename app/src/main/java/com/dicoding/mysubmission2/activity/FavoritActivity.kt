package com.dicoding.mysubmission2.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mysubmission2.R
import com.dicoding.mysubmission2.data.remote.response.ItemsSearch
import com.dicoding.mysubmission2.databinding.ActivityFavoritBinding
import com.dicoding.mysubmission2.ui.adapter.ListSearchAdapter
import com.dicoding.mysubmission2.ui.details.DetaiViewModelFactory
import com.dicoding.mysubmission2.ui.details.DetailActivity
import com.dicoding.mysubmission2.ui.details.DetailViewModel

class FavoritActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name3)

        val factory: DetaiViewModelFactory = DetaiViewModelFactory.getInstance(this)
        val viewModel: DetailViewModel by viewModels {
            factory
        }
        viewModel.deleteAll()
        viewModel.getFavoriteUsers().observe(this) { user ->
            binding.progressBar.visibility = View.GONE
            val userList = user.map {
                ItemsSearch(it.login, it.avatar)
            }
            val userAdapter = ListSearchAdapter(userList as ArrayList<ItemsSearch>)
            binding.rvUser.adapter = userAdapter
            userAdapter.setOnItemClickCallback(object : ListSearchAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsSearch) {
                    showSelectedUser(data)
                }
            })
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        binding.rvUser.setHasFixedSize(true)
    }

    private fun showSelectedUser(user: ItemsSearch) {
        val detailUserIntent = Intent(this, DetailActivity::class.java)
        detailUserIntent.putExtra(DetailActivity.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }
}