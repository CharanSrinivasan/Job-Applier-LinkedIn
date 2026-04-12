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
import java.time.Duration;

import org.apache.commons.io.FileUtils;

public class Main {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();

        // ✅ Required for GitHub Actions
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Optional (basic anti-detection)
        options.addArguments("--disable-blink-features=AutomationControlled");

        // ✅ Chromium binary path (important for Ubuntu runner)
        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = new ChromeDriver(options);

        // Set consistent window size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        // Open Google
        driver.get("https://www.google.com");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ✅ Handle cookie popup (if present)
        try {
            WebElement acceptBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button//*[text()='Accept all']")
                )
            );
            acceptBtn.click();
        } catch (Exception ignored) {
            // Popup not present — continue
        }

        // ✅ Wait for search box properly
        WebElement searchBox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("textarea[name='q'], input[name='q']")
            )
        );

        // Type like human
        typeLikeHuman(searchBox, "Hello World");

        // Small wait so text appears clearly
        Thread.sleep(2000);

        // ✅ Take screenshot AFTER typing
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("output.png"));

        driver.quit();
    }

    public static void typeLikeHuman(WebElement element, String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(random(100, 300));
        }
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
