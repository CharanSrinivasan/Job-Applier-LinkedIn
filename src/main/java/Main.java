import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

import org.apache.commons.io.FileUtils;

public class Main {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();

        // Headless (GitHub Actions)
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Try to reduce bot detection
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36");

        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(options);

            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

            // ✅ Open ChatGPT
            driver.get("https://chatgpt.com");

            // Wait for page load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            System.out.println("Page loaded");

            // ✅ Try clicking "Stay logged out" / "Continue"
            try {
                WebElement continueBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[contains(., 'Stay logged out') or contains(., 'Continue')]")
                        )
                );
                continueBtn.click();
                System.out.println("Clicked continue button");
            } catch (Exception e) {
                System.out.println("No continue button found");
            }

            // ✅ Try to find input box
            WebElement inputBox = null;

            try {
                inputBox = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea"))
                );
                System.out.println("Input box found");
            } catch (Exception e) {
                System.out.println("Input box NOT found");
            }

            // ✅ If input exists → send message
            if (inputBox != null) {

                inputBox.sendKeys("Hii, Give me a joke");
                System.out.println("Typed message");

                try {
                    WebElement sendBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    By.id("composer-submit-button")
                            )
                    );

                    sendBtn.click();
                    System.out.println("Clicked send button");

                    // Wait for response attempt
                    wait.until(ExpectedConditions.or(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-message-author-role='assistant']")),
                            ExpectedConditions.presenceOfElementLocated(By.tagName("article"))
                    ));

                } catch (Exception e) {
                    System.out.println("Send button not clickable");
                }
            }

            // ✅ Always capture page source (VERY IMPORTANT)
            String pageSource = driver.getPageSource();
            Files.writeString(new File("page.html").toPath(), pageSource);

            System.out.println("Saved page source");

            // ✅ Always take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("output.png"));

            System.out.println("Screenshot captured");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
            System.exit(0);
        }
    }
}
