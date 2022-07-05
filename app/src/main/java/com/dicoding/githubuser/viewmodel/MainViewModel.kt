package com.dicoding.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.model.UserDetailResponse
import com.dicoding.githubuser.model.UserSearchResponse
import com.dicoding.githubuser.networking.ApiConfig
import com.dicoding.githubuser.other.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _searchedUserDetails = MutableLiveData<List<UserDetailResponse>>()
    val searchedUserDetails: LiveData<List<UserDetailResponse>> get() = _searchedUserDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var isSnackbarShown: Event<Boolean> = Event(false)

    var errorMessage: String = ""
        private set

    fun getSearchedUsers(username: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getSearchedUser(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    getSearchedUserDetails(responseBody.items as List<ItemsItem>)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun getSearchedUserDetails(listItems: List<ItemsItem>) {
        _isLoading.value = true
        _isError.value = false

        val listUserDetails = ArrayList<UserDetailResponse>()

        if (listItems.count() == 0) {
            errorMessage = NO_RESULT
            _searchedUserDetails.postValue(listUserDetails)
            _isLoading.value = false
            _isError.value = true
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
                            _isLoading.value = false
                            _searchedUserDetails.postValue(listUserDetails)
                        }
                    } else {
                        onError(response.code().toString())
                        _searchedUserDetails.postValue(listUserDetails)
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    _searchedUserDetails.postValue(listUserDetails)
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

    companion object {
        const val NO_RESULT = "No Result"
    }
}