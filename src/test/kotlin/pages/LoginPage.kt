package pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class LoginPage(private val page: Page) {

    private val usernameField = page.locator("#user-name")
    private val passwordField = page.locator("#password")
    private val loginButton = page.locator("#login-button")
    val errorMessage: Locator = page.locator("[data-test='error']")

    fun open(): LoginPage {
        page.navigate("/")   // BASE_URL from .env is added automatically
        return this
    }

    fun login(username: String, password: String): InventoryPage {
        usernameField.fill(username)
        passwordField.fill(password)
        loginButton.click()
        return InventoryPage(page)
    }
}