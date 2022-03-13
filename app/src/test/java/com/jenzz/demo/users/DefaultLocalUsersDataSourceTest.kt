package com.jenzz.demo.users

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jenzz.demo.Database
import com.jenzz.demo.rules.CoroutineTestRule
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.*

class DefaultLocalUsersDataSourceTest {

    private val driver = JdbcSqliteDriver(
        JdbcSqliteDriver.IN_MEMORY,
        Properties().apply { put("foreign_keys", "true") }
    ).also { Database.Schema.create(it) }

    private val sut = DefaultLocalUsersDataSource(
        usersQueries = Database(driver).usersQueries,
        dispatchers = CoroutineTestRule().testCoroutineDispatchers,
    )

    @Test
    fun `emits users when new users added`() = runTest {
        sut.observeUsers().test {
            assertThat(awaitItem()).hasSize(0)

            sut.add(testUsers)
            assertThat(awaitItem()).isEqualTo(testUsers)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `upserts user when private key clashes`() = runTest {
        val user = testUsers.random()
        val updatedUser = user.copy(name = UserName("name updated"))

        sut.observeUsers().test {
            assertThat(awaitItem()).hasSize(0)

            sut.add(listOf(user))
            val users = awaitItem()
            assertThat(users).hasSize(1)
            assertThat(users.first()).isEqualTo(user)

            sut.add(listOf(updatedUser))
            val updatedUsers = awaitItem()
            assertThat(updatedUsers).hasSize(1)
            assertThat(updatedUsers.first()).isEqualTo(updatedUser)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }
}
