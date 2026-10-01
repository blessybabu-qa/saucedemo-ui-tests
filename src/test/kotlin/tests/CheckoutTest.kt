package tests

import base.BaseTest
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import config.Config
import org.junit.jupiter.api.Test
import pages.LoginPage

class CheckoutTest : BaseTest() {

    private val product = "sauce-labs-backpack"

    @Test
    fun `user can complete a purchase`() {
        val inventory = LoginPage(page).open().login(Config.standardUser, Config.password)
        inventory.addToCart(product)

        val checkout = inventory.openCart().checkout()
        checkout.fillInformation("Anna", "Tester", "80331")
        checkout.finish()

        assertThat(checkout.completeHeader).hasText("Thank you for your order!")
    }

    @Test
    fun `checkout without first name shows error`() {
        val inventory = LoginPage(page).open().login(Config.standardUser, Config.password)
        inventory.addToCart(product)

        val checkout = inventory.openCart().checkout()
        checkout.fillInformation("", "Tester", "80331")

        assertThat(checkout.errorMessage).containsText("First Name is required")
    }

    @Test
    fun `removing a product empties the cart`() {
        val inventory = LoginPage(page).open().login(Config.standardUser, Config.password)
        inventory.addToCart(product)

        val cart = inventory.openCart()
        cart.remove(product)

        assertThat(cart.items).hasCount(0)
    }
}