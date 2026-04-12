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

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36");

        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            driver.get("https://chatgpt.com");

            // ✅ Wait for page load (body present)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            // ✅ Try clicking "Continue / Stay logged out"
            try {
                WebElement continueBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[contains(., 'Stay logged out') or contains(., 'Continue')]")
                        )
                );
                continueBtn.click();
                System.out.println("Clicked continue button");
            } catch (Exception e) {
                System.out.println("Continue button not present");
            }

            // ✅ Wait for textarea OR fallback (login page)
            WebElement inputBox = null;

            try {
                inputBox = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("textarea"))
                );
                System.out.println("Input box found");
            } catch (Exception e) {
                System.out.println("Input box not found (likely login page)");
            }

            // ✅ If input exists → type & send
            if (inputBox != null) {

                inputBox.sendKeys("Hii, Give me a joke");

                // Wait for send button to be clickable
                WebElement sendBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.id("composer-submit-button")
                        )
                );

                sendBtn.click();
                System.out.println("Message sent");

                // ✅ Wait for response to appear (very important)
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-message-author-role='assistant']")),
                        ExpectedConditions.presenceOfElementLocated(By.tagName("article"))
                ));
            }

            // ✅ Wait until something stable before screenshot
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            // Screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("output.png"));

        } finally {
            driver.quit();
        }
    }
}
