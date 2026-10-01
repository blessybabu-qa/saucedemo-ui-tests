# Sauce Demo UI Test Automation

[![UI Tests](https://github.com/blessybabu-qa/saucedemo-ui-tests/actions/workflows/tests.yml/badge.svg?branch=master)](https://github.com/blessybabu-qa/saucedemo-ui-tests/actions/workflows/tests.yml)

UI test automation framework for the [Sauce Demo](https://www.saucedemo.com) web shop,
written in **Kotlin** with **Playwright**, built with **Maven**, runnable in **Docker**
and executed in **GitHub Actions**.

## Tech stack

| Tool | Purpose |
|---|---|
| Kotlin + JUnit 5 | Test language and test runner |
| Playwright for Java | Browser automation (Chromium, Firefox, WebKit) |
| Maven | Build and dependency management |
| dotenv-kotlin | Configuration via `.env` / environment variables |
| Docker | Reproducible test execution on Linux |
| GitHub Actions | Continuous integration |

## Test coverage

| Area | Test | Type |
|---|---|---|
| Login | Standard user can log in | Positive |
| Login | Wrong password shows error | Negative |
| Login | Locked-out user cannot log in | Negative |
| Inventory | Adding a product updates the cart badge | Positive |
| Inventory | Sorting by price for `problem_user` | Defect detection (disabled, see below) |
| Checkout | User can complete a purchase (end-to-end) | Positive |
| Checkout | Missing first name shows validation error | Negative |
| Checkout | Removing a product empties the cart | Positive |

## Framework structure

```
src/test/kotlin/
├── config/Config.kt     # reads settings from .env or environment variables
├── base/BaseTest.kt     # shared setup/teardown for all tests
├── pages/               # Page Object Model
│   ├── LoginPage.kt
│   ├── InventoryPage.kt
│   ├── CartPage.kt
│   └── CheckoutPage.kt
└── tests/               # test classes, grouped by feature
    ├── LoginTest.kt
    ├── InventoryTest.kt
    └── CheckoutTest.kt
```

**Design decisions**
- **Page Object Model:** selectors live only in page classes, so tests read like user steps
  and a UI change is fixed in one place.
- **One browser per test class, a fresh browser context per test:** fast, but every test
  is isolated (no shared cookies or login state).
- **No secrets in code:** credentials come from a local `.env` file or CI secrets;
  safe defaults (headless, Chromium) apply when values are missing.
- **Known failures are quarantined, not deleted:** a test that fails for a known reason
  is disabled with an explanation, so the pipeline stays meaningful and the issue stays visible.

## Setup

Requirements: JDK 21, Maven (or IntelliJ IDEA's bundled Maven), optionally Docker.

Create a `.env` file in the project root (it is git-ignored):

```
BASE_URL=https://www.saucedemo.com
STANDARD_USER=standard_user
LOCKED_OUT_USER=locked_out_user
PROBLEM_USER=problem_user
PASSWORD=secret_sauce
BROWSER=chromium
HEADLESS=true
SLOW_MO=0
TRACE=false
TIMEOUT_MS=10000
```

The test credentials are publicly listed on the Sauce Demo login page.

| Setting | Effect |
|---|---|
| `BROWSER` | `chromium`, `firefox` or `webkit` |
| `HEADLESS=false` | Watch the browser while tests run |
| `SLOW_MO=500` | Pause 500 ms between actions (for watching/debugging) |
| `TRACE=true` | Save a Playwright trace per test to `target/traces/` (open at trace.playwright.dev) |

## Running the tests

**Maven**
```
mvn test
```

**Docker (Linux)**
```
docker build -t saucedemo-tests .
docker run --rm --env-file .env -e HEADLESS=true saucedemo-tests
```

**Another browser**
```
docker run --rm --env-file .env -e HEADLESS=true -e BROWSER=firefox saucedemo-tests
```

## Continuous integration

Every push and pull request runs the suite in GitHub Actions. Credentials are stored as
repository secrets. Test reports and traces are uploaded as build artifacts, even when
tests fail.

## Results

All 8 tests (7 passed, 1 skipped) verified on:
- Windows 11 (IntelliJ IDEA)
- Ubuntu Linux (Docker, `mcr.microsoft.com/playwright/java:v1.52.0-noble`)

## Intentional defect: sorting for `problem_user`

Sauce Demo provides `problem_user` as a deliberately faulty account for practicing
defect detection. One of its built-in issues: selecting **Price (low to high)** does
not reorder the products.

The test `InventoryTest > sorting by price low to high works for problem user`
detects this defect and fails as expected. Since the behavior is intentional in the
demo application, the test is disabled with an explanation, so the CI pipeline stays
green while documenting that the check exists and works.

Written as a defect report, it would look like this:

| Field | Value |
|---|---|
| Title | Sorting by price (low to high) does not reorder products for `problem_user` |
| Steps | 1. Log in as `problem_user` 2. Select "Price (low to high)" |
| Expected | Products sorted by price, cheapest first |
| Actual | Product order does not change |
| Scope | Not reproducible with `standard_user` |
| Status | Intentional behavior of the demo app (by design) |

## Observations while using IntelliJ IDEA

While building this project I also noted an issue in the IDE itself:

- **New Project wizard offers a JDK that no longer exists.** A registered JDK whose
  folder had been deleted was still selectable; the error only appeared later during
  Maven sync (`CreateProcess error=2`). *Expected:* invalid JDKs flagged in the wizard.
  *Observed in IntelliJ IDEA 2025.2.6.1 Community (build IC-252.28539.33), Windows 11;
  not yet verified on newer versions.*

