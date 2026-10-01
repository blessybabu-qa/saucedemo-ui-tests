package tests

import base.BaseTest
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import config.Config
import org.junit.jupiter.api.Test
import pages.LoginPage

class LoginTest : BaseTest() {

    @Test
    fun `standard user can log in and sees products`() {
        val inventory = LoginPage(page).open().login(Config.standardUser, Config.password)
        assertThat(inventory.title).hasText("Products")
    }

    @Test
    fun `wrong password shows error message`() {
        val loginPage = LoginPage(page).open()
        loginPage.login(Config.standardUser, "wrong_password")
        assertThat(loginPage.errorMessage).containsText("do not match")
    }

    @Test
    fun `locked out user cannot log in`() {
        val loginPage = LoginPage(page).open()
        loginPage.login(Config.lockedOutUser, Config.password)
        assertThat(loginPage.errorMessage).containsText("locked out")
    }
}