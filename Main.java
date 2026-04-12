import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class Main {
    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();

        // ✅ REQUIRED for GitHub Actions
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Optional (anti-detection basic)
        options.addArguments("--disable-blink-features=AutomationControlled");

        // ✅ IMPORTANT: Set Chromium binary path
        options.setBinary("/usr/bin/chromium-browser");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.google.com");

        Thread.sleep(random(3000, 5000));

        WebElement searchBox = driver.findElement(By.name("q"));

        typeLikeHuman(searchBox, "Hello World");

        Thread.sleep(2000); // wait for text to appear

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
