import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistrationTest {
    private final By ACCEPT_COOKIES_BTN = By.id("cookiescript_accept");
    private final By REGISTRATION_BTN = By.xpath(".//a[@class = 'ms-8 hidden-sm-and-down v-btn v-btn--has-bg v-btn--rounded v-btn--router theme--light v-size--default text-body-m text-lg-body v-btn--primary']");
    private final By FIRST_NAME = By.xpath(".//label[text()='First name']/parent::*//input");
    private final By LAST_NAME = By.xpath(".//label[text()='Last name']/parent::*//input");
    private final By COUNTRY_OF_RESIDENCE = By.xpath(".//label[text()='Country of residence']/parent::*//input");
    private final By COUNTRY_AUSTRIA = By.xpath(".//div[@title = 'Austria']");
    private final By EMAIL = By.xpath(".//input[@type = 'email']");
    String PREFIX = "Test-";
    private final By PASSWORD = By.xpath(".//input[@type = 'password']");
    private final By MARKETING_CHECKBOX = By.xpath(".//span[@class = 'text-body-m text-lg-body']");
    private final By POLICY_CHECKBOX = By.xpath(".//span[@class = 'text-left text-body-m text-lg-body']");
    private final By CREATE_ACCOUNT_BTN = By.xpath(".//button[@type = 'submit']");



    @Test
    public void successfulRegistrationTest() throws InterruptedException {
        //Open Browser window
        WebDriver browser = new ChromeDriver();

        //Open Home Page
        browser.get("http://nectaro.eu");
        browser.manage().window().maximize();

        //Accept cookies
        WebDriverWait waitCookies = new WebDriverWait(browser, Duration.ofSeconds(10));
        browser.findElement(ACCEPT_COOKIES_BTN).click();

        //Press Registration btn
        browser.findElement(REGISTRATION_BTN).click();


        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(FIRST_NAME));
        browser.findElement(FIRST_NAME).click();
//        Thread.sleep(200);
//        browser.findElement(FIRST_NAME).click();

        //Enter First Name
//        browser.findElement(FIRST_NAME).sendKeys("Test-Auto");
        browser.findElement(FIRST_NAME).sendKeys(String.format("%s%s", PREFIX, "Auto"));

//        Actions actions = new Actions(browser);
//        actions.scrollToElement(registrationLink);
//        actions.perform();

        //Enter Last Name
        browser.findElement(LAST_NAME).sendKeys("Test-Auto");

        //Choose Country of residence
        wait.until(ExpectedConditions.elementToBeClickable(COUNTRY_OF_RESIDENCE));
        browser.findElement(COUNTRY_OF_RESIDENCE).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_AUSTRIA)).click();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_AUSTRIA)).click();

        //Enter email
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueEmail = String.format("qa+%s@eco-fin.eu", timestamp);
        browser.findElement(EMAIL).sendKeys(uniqueEmail);

        //Enter password
        browser.findElement(PASSWORD).sendKeys("Testtest2$ghh");

        //Select marketing check-box
        WebElement marketingCheckbox = browser.findElement(MARKETING_CHECKBOX);
        Actions checkbox = new Actions(browser);
        checkbox.scrollToElement(marketingCheckbox);
        checkbox.perform();
        marketingCheckbox.click();

        //Select policy check-box
        WebElement policyCheckbox = browser.findElement(POLICY_CHECKBOX);
        checkbox.scrollToElement(policyCheckbox);
        checkbox.perform();
        policyCheckbox.click();

        //Press Create account btn
       WebElement createAccountBtn = browser.findElement(CREATE_ACCOUNT_BTN);
       checkbox.scrollToElement(createAccountBtn);
       checkbox.perform();
       createAccountBtn.click();
    }
}



