package seleniumScripts;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class CreateLeadTest {
	
	public static void main(String[] args) {
		
		// Step 1) Launch the chrome browser
		ChromeDriver driver = new ChromeDriver();
		
		// Step 2) Load the URL 
		driver.get("http://leaftaps.com/opentaps");
		
		// Step 3) Maximize the chrome browser
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));		
		
		// Step 4) Find the username and type the value
		driver.findElement(By.id("username")).sendKeys("DemoSalesManager");
		
		// Step 5) Find the password and type the value 
		driver.findElement(By.id("password")).sendKeys("crmsfa");	
		
		// Step 6) Find the login button and click
		driver.findElement(By.className("decorativeSubmit")).click();
		
		// Step 7) Print the title 
		System.out.println(driver.getTitle());
		
		// Step 8) Click CRM/SFA link
		driver.findElement(By.linkText("CRM/SFA")).click();
		
		// Step 9) Click Create Lead Link
		driver.findElement(By.linkText("Create Lead")).click();
		
		// Step 10) Find the company name and type the value
		driver.findElement(By.id("createLeadForm_companyName")).sendKeys("TestLeaf");
		
		// Step 11) Find the first name and type your first name
		driver.findElement(By.id("createLeadForm_firstName")).sendKeys("Babu");

		// Step 12) Find the last name and type your last name
		driver.findElement(By.id("createLeadForm_lastName")).sendKeys("Manickam");
		
		// Step 13) Select the Source dropdown with one of the visible text
		WebElement source = driver.findElement(By.id("createLeadForm_dataSourceId"));
		Select dataSource = new Select(source);
		dataSource.selectByVisibleText("Employee");
		
		// Step 14) Select the marketing campaign with one of the value
		WebElement marketing = driver.findElement(By.id("createLeadForm_marketingCampaignId"));
		Select campaign = new Select(marketing);
		campaign.selectByValue("9001");

		// Step 15) Click Create Lead Button
		driver.findElement(By.name("submitButton")).click();
		
		// Step 16) Print the new title
		System.out.println(driver.getTitle());
		
		// Step 17) Close the browser
		driver.quit();
				
	}

}