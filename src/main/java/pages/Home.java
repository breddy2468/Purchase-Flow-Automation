package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {

    private WebDriver driver;
    private WebDriverWait wait;

    private By backpackproduct = By.xpath("//p[text()='Backpack']");
    private By addtocart = By.xpath("//button[text()='Add to cart']");
    private By cartbutton = By.xpath("//span[@class='shopping-cart__notifications']");
    private By continuToCheckOut = By.xpath("//a[text()='Continue to Checkout']");
    private By cards = By.xpath("//span[text()='Cards']");
    private By cardNumberFrame = By.xpath("//iframe[@title='Iframe for card number']");
    private By expiryFrame = By.xpath("//iframe[@title='Iframe for expiry date']");
    private By cvcFrame = By.xpath("//iframe[@title='Iframe for security code']");
    private By ccnumber = By.xpath("//input[@data-fieldtype='encryptedCardNumber']");
    private By expirtDate = By.xpath("//input[@data-fieldtype='encryptedExpiryDate']");
    private By securityCode = By.xpath("//input[@data-fieldtype='encryptedSecurityCode']");
    private By name = By.xpath("//input[@name='holderName']");
    private By payButton = By.xpath("//span[contains(text(),'Pay')]");
    private By successMessage = By.xpath("//p[contains(text(),'Your order has been successfully placed.')]");

    public Home(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public Home selectBackproduct() {
        WebElement backpackProduct = wait.until(ExpectedConditions.elementToBeClickable(backpackproduct));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backpackProduct);
        backpackProduct.click();
        wait.until(ExpectedConditions.elementToBeClickable(addtocart)).click();
        wait.until(ExpectedConditions.elementToBeClickable(cartbutton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(continuToCheckOut)).click();
        wait.until(ExpectedConditions.elementToBeClickable(cards)).click();
        return this;
    }

    public Home enterPaymentDetails() {
        WebElement cardFrame = wait.until(ExpectedConditions.presenceOfElementLocated(cardNumberFrame));
        driver.switchTo().frame(cardFrame);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ccnumber)).sendKeys("4400002000000004");
        driver.switchTo().defaultContent();

        WebElement expiry = wait.until(ExpectedConditions.presenceOfElementLocated(expiryFrame));
        driver.switchTo().frame(expiry);
        wait.until(ExpectedConditions.visibilityOfElementLocated(expirtDate)).sendKeys("0330");
        driver.switchTo().defaultContent();

        WebElement cvc = wait.until(ExpectedConditions.presenceOfElementLocated(cvcFrame));
        driver.switchTo().frame(cvc);
        wait.until(ExpectedConditions.visibilityOfElementLocated(securityCode)).sendKeys("737");
        driver.switchTo().defaultContent();

        wait.until(ExpectedConditions.visibilityOfElementLocated(name)).sendKeys("Test");
        wait.until(ExpectedConditions.elementToBeClickable(payButton)).click();
        return this;
    }

    public boolean verifyOrderSuccess() {
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return success.isDisplayed();
    }
}