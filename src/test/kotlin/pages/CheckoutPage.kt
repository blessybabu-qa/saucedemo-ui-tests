package pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class CheckoutPage(private val page: Page) {

    private val firstNameField = page.locator("[data-test='firstName']")
    private val lastNameField = page.locator("[data-test='lastName']")
    private val postalCodeField = page.locator("[data-test='postalCode']")
    private val continueButton = page.locator("[data-test='continue']")
    private val finishButton = page.locator("[data-test='finish']")

    val errorMessage: Locator = page.locator("[data-test='error']")
    val completeHeader: Locator = page.locator(".complete-header")

    fun fillInformation(firstName: String, lastName: String, postalCode: String): CheckoutPage {
        firstNameField.fill(firstName)
        lastNameField.fill(lastName)
        postalCodeField.fill(postalCode)
        continueButton.click()
        return this
    }

    fun finish() {
        finishButton.click()
    }
}