package com.dicoding.githubuser.networking

import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Test

class ApiConfigTest {

    @Test
    fun testGetApiService() {
        val apiService = ApiConfig.getApiService()
        assertThat(apiService, instanceOf(ApiService::class.java))
    }
}