import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.google.com");

        Thread.sleep(random(2000, 4000));

        WebElement searchBox = driver.findElement(By.name("q"));

        typeLikeHuman(searchBox, "Hello World");

        Thread.sleep(5000);

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
