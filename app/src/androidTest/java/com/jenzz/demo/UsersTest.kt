package com.jenzz.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jenzz.demo.users.testUsers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UsersTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var localUsersDataSource: FakeLocalUsersDataSource

    @Before
    fun setUp() {
        hiltRule.inject()

        runBlocking {
            localUsersDataSource.add(testUsers)
        }

        composeTestRule.setContent {
            App()
        }
    }

    @Test
    fun displaysUsers() {
        testUsers.forEach { user ->
            composeTestRule.onNodeWithText(user.name.value)
                .assertIsDisplayed()
            composeTestRule.onNodeWithText(user.email.value)
                .assertIsDisplayed()
            composeTestRule.onNodeWithText(user.website.toString())
                .assertIsDisplayed()
        }
    }
}
