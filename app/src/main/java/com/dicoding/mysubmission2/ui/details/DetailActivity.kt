package com.dicoding.mysubmission2.ui.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.mysubmission2.R
import com.dicoding.mysubmission2.ui.adapter.SectionPageAdapter
import com.dicoding.mysubmission2.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.mysubmission2.data.remote.Result

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name2)

        val factory: DetaiViewModelFactory = DetaiViewModelFactory.getInstance(this)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }

        username = intent.getStringExtra(EXTRA_USER) as String

        detailViewModel.getUser(username)
        val sectionsPagerAdapter = SectionPageAdapter(this)
        sectionsPagerAdapter.username = username

        binding.viewPager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f

        detailViewModel.getUser(username).observe(this){ result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        binding.progresBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progresBar.visibility = View.GONE
                        val user = result.data
                        binding.apply {
                            tvName.text = user.name
                            tvUsername.text = user.login
                            tvCompany.text = user.company
                            tvLocation.text = user.location
                            tvRepository.text = resources.getString(R.string.publicrepos, user.publicRepos)
                            if (user.isFavorite) {
                                ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.ic_baseline_favorite_red))
                            } else {
                                ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.ic_baseline_favorite_border_24))
                            }
                            ivFav.setOnClickListener {
                                if (user.isFavorite){
                                    detailViewModel.deleteFavorite(user)
                                } else {
                                    detailViewModel.saveFavorite(user)
                                }
                            }
                        }
                        Glide.with(this)
                            .load(user.avatar)
                            .circleCrop()
                            .into(binding.imgAvatar)
                        val countFollow = arrayOf(user.following, user.followers)
                        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                            tab.text = resources.getString(TAB_TITLES[position], countFollow[position])
                        }.attach()
                    }
                    is Result.Error -> {
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share, username))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}