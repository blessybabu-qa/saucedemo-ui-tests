package tests

import base.BaseTest
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import config.Config
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pages.LoginPage
import org.junit.jupiter.api.Disabled

class InventoryTest : BaseTest() {

    @Test
    fun `adding a product updates the cart badge`() {
        val inventory = LoginPage(page).open().login(Config.standardUser, Config.password)
        inventory.addToCart("sauce-labs-backpack")
        assertThat(inventory.cartBadge).hasText("1")
    }

    @Disabled("FIXME BUG-001: sorting by price does not work for problem_user")
    @Test
    fun `sorting by price low to high works for problem user`() {
        val inventory = LoginPage(page).open().login(Config.problemUser, Config.password)
        inventory.sortBy("lohi")
        val prices = inventory.getPrices()
        assertEquals(prices.sorted(), prices, "Products should be sorted by price")
    }
}