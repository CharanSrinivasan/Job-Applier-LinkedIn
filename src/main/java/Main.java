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
import java.time.LocalTime;
import java.time.ZoneId;

import org.apache.commons.io.FileUtils;

public class Main {

    public static void main(String[] args) throws Exception {

        // ✅ Wait until 7:05 PM IST
        waitUntil(19, 4);

        ChromeOptions options = new ChromeOptions();

        // Required for GitHub Actions
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Optional
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Chromium path (GitHub runner)
        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        driver.get("https://www.google.com");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Handle cookie popup if present
        try {
            WebElement acceptBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button//*[text()='Accept all']")
                )
            );
            acceptBtn.click();
        } catch (Exception ignored) {}

        // Wait for search box
        WebElement searchBox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("textarea[name='q'], input[name='q']")
            )
        );

        // Type search query
        typeLikeHuman(searchBox, "current time in India");

        // Press Enter
        searchBox.sendKeys(Keys.ENTER);

        // Wait for results page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));

        Thread.sleep(2000); // small buffer

        // Take screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("output.png"));

        driver.quit();
    }

    // ✅ Wait until specific IST time
    public static void waitUntil(int targetHour, int targetMinute) throws InterruptedException {

        ZoneId zone = ZoneId.of("Asia/Kolkata");

        while (true) {
            LocalTime now = LocalTime.now(zone);

            System.out.println("Current IST time: " + now);

            // Exit if current time is equal or past target
            if (now.getHour() > targetHour ||
               (now.getHour() == targetHour && now.getMinute() >= targetMinute)) {
                System.out.println("Target time reached!");
                break;
            }

            Thread.sleep(10000); // check every 10 sec
        }
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
