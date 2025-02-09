const { test, expect } = require('@playwright/test');

test('loginPageTest', async ({ page }) => {
    const USERNAME = "DemoSalesManager";
    const PASSWORD = "crmsfa";
    const LOGIN_URL = "http://leaftaps.com/opentaps/control/main";

    await page.goto(LOGIN_URL);

    // Wait for username field and fill it
    await page.waitForSelector('text="Username"');
    await page.fill('text="Username"', USERNAME);

    // Wait for password field and fill it
    await page.waitForSelector('text="Password"');
    await page.fill('text="Password"', PASSWORD);

    // Wait for login button and click it
    await page.waitForSelector('text="Login"');
    await page.click('text="Login"');

    try {
        // Wait for logout button to be visible
        await page.waitForSelector('text="Logout"');
        await expect(page.getByText('Logout')).toBeVisible();
        console.log("Test case pass");
    } catch (error) {
        console.log("Test case fail");
    }
});