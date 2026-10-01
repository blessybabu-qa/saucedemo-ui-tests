package pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class CartPage(private val page: Page) {

    val items: Locator = page.locator(".cart_item")
    private val checkoutButton = page.locator("[data-test='checkout']")

    fun remove(productId: String) {
        page.locator("[data-test='remove-$productId']").click()
    }

    fun checkout(): CheckoutPage {
        checkoutButton.click()
        return CheckoutPage(page)
    }
}