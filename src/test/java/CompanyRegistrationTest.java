import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;


public class CompanyRegistrationTest {
    private final By ACCEPT_COOKIES_BTN = By.id("cookiescript_accept");

    private final By COMPANY_NAME = By.xpath(".//label[normalize-space()='Company name']/parent::*//input");
    private final By FIRST_NAME = By.xpath(".//label[normalize-space()='Representative first name']/parent::*//input");
    private final By FIRST_NAME_ATTRS = By.xpath("//input[@name='firstName' or @name='first_name' or @autocomplete='given-name' or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'representative first name') or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'representative first name') or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'firstname')]");
    private final By LAST_NAME = By.xpath(".//label[normalize-space()='Representative last name']/parent::*//input");
    private final By LAST_NAME_ATTRS = By.xpath("//input[@name='lastName' or @name='last_name' or @autocomplete='family-name' or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'representative last name') or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'representative last name') or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lastname')]");
    private final By COUNTRY_OF_RESIDENCE = By.xpath(".//label[normalize-space()='Country of registration']/parent::*//input");
    private final By COUNTRY_AUSTRIA = By.xpath(".//div[@title = 'Austria']");
    private final By EMAIL = By.xpath(".//input[@type = 'email']");
    private final By PASSWORD = By.xpath(".//input[@type = 'password']");
    private final By MARKETING_CHECKBOX = By.xpath(".//label[.//span[contains(., 'Agree to receive marketing communications')]]");
    private final By POLICY_CHECKBOX = By.xpath(".//label[.//span[contains(., 'By proceeding with registration I agree')]]");
    private final By CREATE_ACCOUNT_BTN = By.xpath(".//button[@type = 'submit']");

    private final String NAME_PREFIX = "Test-";

    private WebElement waitForAnyVisible(WebDriver browser, WebDriverWait wait, By... candidates) {
        for (By locator : candidates) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception ignored) {
            }
        }
        throw new NoSuchElementException("None of the candidate locators were visible");
    }

    private void clickCheckboxSafely(WebDriver browser, WebElement input) {
        ((JavascriptExecutor) browser).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", input);
        try {
            ((JavascriptExecutor) browser).executeScript("arguments[0].click();", input);
        } catch (Exception ignored) {
        }
        if (!input.isSelected()) {
            try {
                input.sendKeys(Keys.SPACE);
            } catch (Exception ignored) {
            }
        }
        if (!input.isSelected()) {
            ((JavascriptExecutor) browser).executeScript(
                    "if(!arguments[0].checked){arguments[0].checked=true; arguments[0].dispatchEvent(new Event('change',{bubbles:true})); arguments[0].dispatchEvent(new Event('input',{bubbles:true}));}",
                    input);
        }
    }


    @Test
    public void successfulCompanyRegistrationTest() {
        WebDriver browser = new ChromeDriver();

        try {
            browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            browser.manage().window().maximize();

            // Open Company Registration page
            browser.get("https://nectaro.eu/registration/company/?hl=en");

            // Accept cookies if shown (handle possible iframe)
            try {
                WebDriverWait waitCookies = new WebDriverWait(browser, Duration.ofSeconds(10));
                // Some deployments show the cookie banner inside an iframe
                int frames = browser.findElements(By.cssSelector("iframe")).size();
                for (int i = 0; i < frames; i++) {
                    browser.switchTo().frame(i);
                    if (!browser.findElements(ACCEPT_COOKIES_BTN).isEmpty()) {
                        browser.findElement(ACCEPT_COOKIES_BTN).click();
                        browser.switchTo().defaultContent();
                        break;
                    }
                    browser.switchTo().defaultContent();
                }
                if (browser.findElements(ACCEPT_COOKIES_BTN).size() > 0) {
                    waitCookies.until(ExpectedConditions.elementToBeClickable(ACCEPT_COOKIES_BTN));
                    browser.findElement(ACCEPT_COOKIES_BTN).click();
                }
            } catch (Exception ignored) {
            }

            WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(30));

            // Ensure page fully loaded
            ((JavascriptExecutor) browser).executeScript("return document.readyState").equals("complete");

            // Company name
            WebElement companyNameInput = waitForAnyVisible(browser, wait, COMPANY_NAME,
                    By.xpath("//input[@name='companyName' or @name='company_name' or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'company name') or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'company name') or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'company')]")
            );
            companyNameInput.click();
            companyNameInput.sendKeys(String.format("%s%s", NAME_PREFIX, "Company"));

            // First and Last name
            WebElement firstNameInput = waitForAnyVisible(browser, wait, FIRST_NAME, FIRST_NAME_ATTRS);
            firstNameInput.sendKeys(String.format("%s%s", NAME_PREFIX, "Auto"));
            WebElement lastNameInput = waitForAnyVisible(browser, wait, LAST_NAME, LAST_NAME_ATTRS);
            lastNameInput.sendKeys("Test-Auto");

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

            WebElement marketingLabel = browser.findElement(MARKETING_CHECKBOX);
            scroller.scrollToElement(marketingLabel).perform();
            String marketingFor = marketingLabel.getAttribute("for");
            WebElement marketingInput = marketingFor != null && !marketingFor.isEmpty()
                    ? browser.findElement(By.id(marketingFor))
                    : marketingLabel.findElement(By.xpath("preceding-sibling::div[contains(@class,'v-input--selection-controls__input')]/input[@type='checkbox']"));
            clickCheckboxSafely(browser, marketingInput);

            WebElement policyLabel = browser.findElement(POLICY_CHECKBOX);
            scroller.scrollToElement(policyLabel).perform();
            String policyFor = policyLabel.getAttribute("for");
            WebElement policyInput = policyFor != null && !policyFor.isEmpty()
                    ? browser.findElement(By.id(policyFor))
                    : policyLabel.findElement(By.xpath("preceding-sibling::div[contains(@class,'v-input--selection-controls__input')]/input[@type='checkbox']"));
            clickCheckboxSafely(browser, policyInput);

            // Submit
            WebElement createAccountBtn = browser.findElement(CREATE_ACCOUNT_BTN);
            scroller.scrollToElement(createAccountBtn).perform();
            createAccountBtn.click();
        } finally {
            browser.quit();
        }
    }
}

