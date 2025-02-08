// File: loginPageTest.spec.js

const { test, expect } = require('@playwright/test');

test('loginPageTest', async ({ page }) => {
    const USERNAME = "DemoSalesManager";
    const PASSWORD = "crmsfa";
    const LOGIN_URL = "http://leaftaps.com/opentaps/control/main";

    await page.goto(LOGIN_URL);

    // Fill username
    await page.getByLabel('Username').fill(USERNAME);

    // Fill password
    await page.getByLabel('Password').fill(PASSWORD);

    // Click login button
    await page.getByText('Login').click();

    // Wait for page to navigate after login
    await page.waitForURL(LOGIN_URL);

    try {
        // Check if logout button is visible
        const logoutButton = page.getByText('Logout');
        await expect(logoutButton).toBeVisible();
        console.log("Test case pass");
    } catch (error) {
        console.log("Test case fail");
        throw error;
    }
});