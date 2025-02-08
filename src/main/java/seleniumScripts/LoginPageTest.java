package seleniumScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageTest {private static final String USERNAME = "DemoSalesManager"; // In a real application, get this from a config file
    private static final String PASSWORD = "crmsfa"; // In a real application, get this from a config file
    private static final String LOGIN_URL = "http://leaftaps.com/opentaps/control/main";

    public static void main(String[] args) {
        
        WebDriver driver = new ChromeDriver();
        driver.get(LOGIN_URL);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            driver.findElement(By.id("username")).sendKeys(USERNAME);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            driver.findElement(By.id("password")).sendKeys(PASSWORD);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Login']")));
            driver.findElement(By.xpath("//input[@value='Login']")).click();

            try{
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Logout']")));
               if (driver.findElement(By.xpath("//input[@value='Logout']")).isDisplayed()) {
                   System.out.println("Test case pass");
               }
            }
            catch (NoSuchElementException E){
                System.out.println("Test case fail");
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
        } finally {
            driver.quit(); // Close the browser
        }
    }
}
