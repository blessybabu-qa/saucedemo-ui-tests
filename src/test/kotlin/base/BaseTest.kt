package base

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import config.Config
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import com.microsoft.playwright.Tracing
import org.junit.jupiter.api.TestInfo
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

    private lateinit var playwright: Playwright
    private lateinit var browser: Browser
    private lateinit var context: BrowserContext
    protected lateinit var page: Page

    // Once per test class: start Playwright and the browser (expensive)
    @BeforeAll
    fun launchBrowser() {
        playwright = Playwright.create()
        val browserType = when (Config.browser) {
            "firefox" -> playwright.firefox()
            "webkit" -> playwright.webkit()
            else -> playwright.chromium()
        }
        browser = browserType.launch(
            BrowserType.LaunchOptions()
                .setHeadless(Config.headless)
                .setSlowMo(Config.slowMo))
    }

    // Before every test: a fresh, isolated browser context (clean cookies, no leftover login)
    @BeforeEach
    fun createPage() {
        context = browser.newContext(Browser.NewContextOptions().setBaseURL(Config.baseUrl))
        context.setDefaultTimeout(Config.timeoutMs)
        if (Config.trace) {
            context.tracing().start(
                Tracing.StartOptions().setScreenshots(true).setSnapshots(true)
            )
        }
        page = context.newPage()
    }


    @AfterEach
    fun closePage(testInfo: TestInfo) {
        if (Config.trace) {
            val fileName = testInfo.displayName.replace(Regex("[^a-zA-Z0-9]+"), "_")
            context.tracing().stop(
                Tracing.StopOptions().setPath(Paths.get("target/traces/$fileName.zip"))
            )
        }
        context.close()
    }
}