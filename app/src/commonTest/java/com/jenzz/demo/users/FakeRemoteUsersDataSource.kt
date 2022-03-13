package com.jenzz.demo.users

import com.jenzz.demo.common.ApiResponse

class FakeRemoteUsersDataSource(
    private val apiResponse: ApiResponse<List<User>>,
) : RemoteUsersDataSource {

    override suspend fun fetchUsers(): ApiResponse<List<User>> = apiResponse
}
