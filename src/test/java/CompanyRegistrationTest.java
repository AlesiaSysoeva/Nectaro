import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;


public class CompanyRegistrationTest {
    private final By ACCEPT_COOKIES_BTN = By.id("cookiescript_accept");

    private final By COMPANY_NAME = By.xpath(".//label[contains(., 'Company name') or contains(., 'Company Name')]/parent::*//input");
    private final By FIRST_NAME = By.xpath(".//label[contains(., 'First name') or contains(., 'First Name')]/parent::*//input");
    private final By LAST_NAME = By.xpath(".//label[text()='Last name']/parent::*//input");
    private final By COUNTRY_OF_RESIDENCE = By.xpath(".//label[text()='Country of residence']/parent::*//input");
    private final By COUNTRY_AUSTRIA = By.xpath(".//div[@title = 'Austria']");
    private final By EMAIL = By.xpath(".//input[@type = 'email']");
    private final By PASSWORD = By.xpath(".//input[@type = 'password']");
    private final By MARKETING_CHECKBOX = By.xpath(".//span[@class = 'text-body-m text-lg-body']");
    private final By POLICY_CHECKBOX = By.xpath(".//span[@class = 'text-left text-body-m text-lg-body']");
    private final By CREATE_ACCOUNT_BTN = By.xpath(".//button[@type = 'submit']");

    private final String NAME_PREFIX = "Test-";


    @Test
    public void successfulCompanyRegistrationTest() {
        WebDriver browser = new ChromeDriver();

        try {
            browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            browser.manage().window().maximize();

            // Open Company Registration page
            browser.get("https://nectaro.eu/registration/company/?hl=en");

            // Accept cookies if shown
            try {
                WebDriverWait waitCookies = new WebDriverWait(browser, Duration.ofSeconds(10));
                waitCookies.until(ExpectedConditions.elementToBeClickable(ACCEPT_COOKIES_BTN));
                browser.findElement(ACCEPT_COOKIES_BTN).click();
            } catch (Exception ignored) {
            }

            WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(30));

            // Company name
            wait.until(ExpectedConditions.visibilityOfElementLocated(COMPANY_NAME));
            browser.findElement(COMPANY_NAME).click();
            browser.findElement(COMPANY_NAME).sendKeys(String.format("%s%s", NAME_PREFIX, "Company"));

            // First and Last name
            wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME));
            browser.findElement(FIRST_NAME).sendKeys(String.format("%s%s", NAME_PREFIX, "Auto"));
            browser.findElement(LAST_NAME).sendKeys("Test-Auto");

            // Country of residence
            wait.until(ExpectedConditions.elementToBeClickable(COUNTRY_OF_RESIDENCE));
            WebElement countryInput = browser.findElement(COUNTRY_OF_RESIDENCE);
            countryInput.click();
            // Type-ahead select can be used instead of clicking a dropdown item in some locales
            countryInput.sendKeys("Austria");
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_AUSTRIA)).click();
            } catch (Exception ignored) {
            }

            // Email
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String uniqueEmail = String.format("qa+%s@eco-fin.eu", timestamp);
            browser.findElement(EMAIL).sendKeys(uniqueEmail);

            // Password
            browser.findElement(PASSWORD).sendKeys("Testtest2$ghh");

            // Checkboxes may be below the fold
            Actions scroller = new Actions(browser);

            WebElement marketingCheckbox = browser.findElement(MARKETING_CHECKBOX);
            scroller.scrollToElement(marketingCheckbox).perform();
            try {
                marketingCheckbox.click();
            } catch (Exception e) {
                // Sometimes the label span is not directly clickable; click the nearest input instead
                marketingCheckbox.findElement(By.xpath("ancestor::label//input|preceding::input[1]"))
                        .click();
            }

            WebElement policyCheckbox = browser.findElement(POLICY_CHECKBOX);
            scroller.scrollToElement(policyCheckbox).perform();
            try {
                policyCheckbox.click();
            } catch (Exception e) {
                policyCheckbox.findElement(By.xpath("ancestor::label//input|preceding::input[1]"))
                        .click();
            }

            // Submit
            WebElement createAccountBtn = browser.findElement(CREATE_ACCOUNT_BTN);
            scroller.scrollToElement(createAccountBtn).perform();
            createAccountBtn.click();
        } finally {
            browser.quit();
        }
    }
}

