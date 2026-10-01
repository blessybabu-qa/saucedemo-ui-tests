package pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class InventoryPage(private val page: Page) {

    val title: Locator = page.locator(".title")
    val cartBadge: Locator = page.locator(".shopping_cart_badge")
    private val sortDropdown = page.locator("[data-test='product-sort-container']")
    private val prices = page.locator(".inventory_item_price")

    fun addToCart(productId: String) {
        page.locator("[data-test='add-to-cart-$productId']").click()
    }

    fun sortBy(option: String) {
        sortDropdown.selectOption(option)
    }

    fun getPrices(): List<Double> =
        prices.allTextContents().map { it.removePrefix("$").toDouble() }

    fun openCart(): CartPage {
        page.locator(".shopping_cart_link").click()
        return CartPage(page)
    }
}