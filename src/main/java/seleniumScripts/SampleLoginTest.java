package seleniumScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SampleLoginTest {

    public static void main(String[] args) { // Main method for plain Java assertions

        WebDriver driver = new ChromeDriver();
        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("student");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Password123");

        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();

       // Verify URL (Plain Java assertion)
        String expectedUrl = "https://practicetestautomation.com/logged-in-successfully/";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains(expectedUrl)) {
            throw new AssertionError("URL does not contain expected string.");
        }

        // Verify success message (Plain Java assertion)
        WebElement successMessage = driver.findElement(By.xpath("//strong[contains(text(),'Congratulations')]"));
        if (!successMessage.isDisplayed()) {
            throw new AssertionError("Success message is not displayed.");
        }

        // Verify logout button (Plain Java assertion)
        WebElement logoutButton = driver.findElement(By.linkText("Log out"));
        if (!logoutButton.isDisplayed()) {
            throw new AssertionError("Logout button is not displayed.");
        }

        driver.quit();
    }
}
