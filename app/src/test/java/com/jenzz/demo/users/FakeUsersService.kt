package com.jenzz.demo.users

class FakeUsersService(
    private val fetchUsersDelegate: () -> List<UserDto>,
) : UsersService {

    override suspend fun fetchUsers(): List<UserDto> = fetchUsersDelegate()
}
