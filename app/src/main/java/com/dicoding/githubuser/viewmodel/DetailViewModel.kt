package com.dicoding.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.model.UserDetailResponse
import com.dicoding.githubuser.networking.ApiConfig
import com.dicoding.githubuser.other.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private var _listFollowers = MutableLiveData<List<UserDetailResponse>>()
    val listFollowers: LiveData<List<UserDetailResponse>> get() = _listFollowers

    private var _listFollowing = MutableLiveData<List<UserDetailResponse>>()
    val listFollowing: LiveData<List<UserDetailResponse>> get() = _listFollowing

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val isSnackbarShown: Event<Boolean> = Event(false)

    var errorMessage: String = ""
        private set

    fun getUserFollowers(username: String) {
        _isError.value = false

        fun setFollowers(newValue: ArrayList<UserDetailResponse>) {
            _listFollowers.postValue(newValue)
        }

        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    getUserDetails(responseBody, ::setFollowers)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isError.value = false

        fun setFollowing(newValue: ArrayList<UserDetailResponse>) {
            _listFollowing.postValue(newValue)
        }

        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    getUserDetails(responseBody, ::setFollowing)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun getUserDetails(listItems: List<ItemsItem>, setValue: (newValue: ArrayList<UserDetailResponse>) -> Unit) {
        _isLoading.value = true
        _isError.value = false

        val listUserDetails = ArrayList<UserDetailResponse>()

        if (listItems.count() == 0) {
            setValue(listUserDetails)
            _isLoading.value = false
            return
        }

        listItems.forEach {
            val client = ApiConfig.getApiService().getUserDetail(it.login!!)
            client.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        listUserDetails.add(responseBody)
                        if (listUserDetails.count() >= listItems.count()) {
                            setValue(listUserDetails)
                            _isLoading.value = false
                        }
                    } else {
                        setValue(listUserDetails)
                        onError(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    if (listUserDetails.count() > 0)
                        setValue(listUserDetails)

                    onError(t.message)
                    t.printStackTrace()
                }
            })
        }
    }

    private fun onError(inputMessage: String?) {
        var message = inputMessage
        message = if (message.isNullOrBlank() or message.isNullOrEmpty()) ApiConfig.UNKNOWN_ERROR
        else message
        
        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}