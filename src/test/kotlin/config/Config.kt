package config

import io.github.cdimascio.dotenv.dotenv

object Config {

    // Reads .env if it exists; environment variables (e.g. in CI) also work
    private val env = dotenv { ignoreIfMissing = true }

    private fun get(key: String, default: String? = null): String =
        env[key] ?: default
        ?: error("Missing config value '$key'. Add it to .env or set it as an environment variable.")

    val baseUrl: String get() = get("BASE_URL", "https://www.saucedemo.com")
    val standardUser: String get() = get("STANDARD_USER")
    val lockedOutUser: String get() = get("LOCKED_OUT_USER")
    val problemUser: String get() = get("PROBLEM_USER")
    val password: String get() = get("PASSWORD")

    val browser: String get() = get("BROWSER", "chromium").lowercase()
    val headless: Boolean get() = get("HEADLESS", "true").toBoolean()
    val timeoutMs: Double get() = get("TIMEOUT_MS", "10000").toDouble()
    val trace: Boolean get() = get("TRACE", "false").toBoolean()
    val slowMo: Double get() = get("SLOW_MO", "0").toDouble()
}