const { test, expect } = require('@playwright/test');

test('Create Lead Test', async ({ page }) => {
  // Step 1) Launch the chrome browser and load the URL 
  await page.goto("http://leaftaps.com/opentaps");

  // Step 2) Find the username and type the value
  await page.fill('id=username', 'DemoSalesManager');
  
  // Step 3) Find the password and type the value 
  await page.fill('id=password', 'crmsfa');
  
  // Step 4) Find the login button and click
  await page.click('text=Login');

  // Step 5) Click CRM/SFA link
  await page.click('text=CRM/SFA');

  // Step 6) Click Create Lead Link
  await page.click('text=Create Lead');

  // Step 7) Find the company name and type the value
  await page.fill('id=createLeadForm_companyName', 'TestLeaf');

  // Step 8) Find the first name and type your first name
  await page.fill('id=createLeadForm_firstName', 'Babu');

  // Step 9) Find the last name and type your last name
  await page.fill('id=createLeadForm_lastName', 'Manickam');

  // Step 10) Select the Source dropdown with one of the visible text
  await page.selectOption('id=createLeadForm_dataSourceId', 'Employee');

  // Step 11) Select the marketing campaign with one of the value
  await page.selectOption('id=createLeadForm_marketingCampaignId', '9001');

  // Step 12) Click Create Lead Button
  await page.click('name=submitButton');

  // Step 13) Verify the new title
  await expect(page).toHaveTitle(/My Leads/);
});