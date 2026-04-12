import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.Keys;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;

public class Main {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();

        // Headless (GitHub Actions)
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Reduce detection
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36");

        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // ✅ Open ChatGPT
        driver.get("https://chatgpt.com");

        // Wait for page load
        Thread.sleep(5000);

        // ✅ Try to handle "continue without login" (best effort)
        try {
            WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Stay logged out') or contains(., 'Continue')]")
                )
            );
            continueBtn.click();
            System.out.println("Clicked continue without login");
        } catch (Exception e) {
            System.out.println("No continue button found or already accessible");
        }

        Thread.sleep(5000);

        // ✅ Try to find chat input and type
        try {
            WebElement inputBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("textarea")
                )
            );

            inputBox.sendKeys("Hii, Give me a joke");
            inputBox.sendKeys(Keys.ENTER);

            System.out.println("Message sent");

            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("Could not interact with chat input (likely login required)");
        }

        // ✅ Take screenshot (always works)
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("output.png"));

        driver.quit();
    }
}
