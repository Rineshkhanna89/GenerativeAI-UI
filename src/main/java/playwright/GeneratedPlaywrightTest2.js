const { test, expect } = require('@playwright/test');

const USERNAME = "DemoSalesManager";
const PASSWORD = "crmsfa";
const LOGIN_URL = "http://leaftaps.com/opentaps/control/main";

test.describe('Login Page Test', () => {
  test('should login successfully', async ({ page }) => {
    await page.goto(LOGIN_URL);
    
    // Wait for and fill username
    await page.waitForSelector('#username');
    await page.fill('#username', USERNAME);

    // Wait for and fill password
    await page.waitForSelector('#password');
    await page.fill('#password', PASSWORD);

    // Wait for and click login button
    await page.waitForSelector('input[value="Login"]');
    await page.click('input[value="Login"]');

    try {
      // Wait for logout button to be visible
      await page.waitForSelector('input[value="Logout"]');
      console.log("Test case pass");
    } catch (error) {
      console.log("Test case fail");
    }
  });
});