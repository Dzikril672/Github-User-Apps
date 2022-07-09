package com.dicoding.mysubmission2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mysubmission2.data.remote.retrofit.ApiConfig
import com.dicoding.mysubmission2.util.Event
import com.dicoding.mysubmission2.data.remote.response.ItemsSearch
import com.dicoding.mysubmission2.data.remote.response.ResponseSearch
import retrofit2.*

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<ItemsSearch>>()
    val userList: LiveData<List<ItemsSearch>> = _userList

    private val _userCount = MutableLiveData<Int?>()
    val userCount: LiveData<Int?> = _userCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun findUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items as List<ItemsSearch>
                    _userCount.value = response.body()?.totalCount
                    _toastText.value = Event("Success")
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }
}
