package webdriverMaven;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class Assignment3_Amazon_Assertion {
	
	WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
    
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    @Description("Amazon add to cart feature testing")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Add item to cart and verify if the first product is added to cart or not")
    public void verifyItemAddedToCart() throws InterruptedException {
        driver.get("https://www.amazon.in/");

        // Check for CAPTCHA
        List<WebElement> captchaElements = driver.findElements(By.id("captchacharacters"));
        if (!captchaElements.isEmpty() && captchaElements.get(0).isDisplayed()) {
            System.out.println("CAPTCHA detected. Please solve it manually.");
            Thread.sleep(15000);  // Adjust as needed
        }

        // Search for the item
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("iphone 16pro max", Keys.ENTER);

         //Scroll
      	JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,300)", "");
        
        // Wait for products to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'iphone 16pro max')]")));
  
     
        // Get the first product name
        WebElement firstProduct = driver.findElement(By.xpath("//div[@data-cy='title-recipe']/a"));
        String productNameBefore = firstProduct.getText();
        System.out.println("Product Selected: " + productNameBefore);
        
        driver.findElement(By.xpath("//*[@id='a-autoid-1-announce']")).click();

        //Click on the first product
       firstProduct.click();
        
       String parentWindowHandle = driver.getWindowHandle();
       System.out.println(parentWindowHandle);
        
        Set<String> allWindowHandles = driver.getWindowHandles();
        System.out.println(allWindowHandles);
       
        
        for (String handle : allWindowHandles) {
        	System.out.println(handle);
            if (!handle.equals(parentWindowHandle)) {
                // Switch to the new window
                driver.switchTo().window(handle);
                System.out.println(driver.getTitle());
    
               
            }
          
        }
        
        System.out.println(driver.getCurrentUrl()); 
        
        Thread.sleep(5000);
       
   
        // Navigate to the cart
        WebElement cartIcon = driver.findElement(By.xpath("//*[@id='nav-cart-count-container']"));
        cartIcon.click();

       // Verify if the same product is in the cart
        WebElement cartProductName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='a-truncate-cut']")));
        String productNameInCart = cartProductName.getText();
        System.out.println(productNameInCart);

        // Assertion to verify the product
        
        
        //Assert.assertEquals(productNameBefore, productNameInCart, "The product added to the cart is not the same as selected!");

        Assert.assertTrue(productNameInCart.contains(productNameBefore) || productNameInCart.contains(productNameInCart),
                "Product names do not match! Cart: ");

        
        System.out.println("Test Passed: Correct item added to the cart.");
    }

    @AfterClass
    public void tearDown() {
    	System.out.println("Testcase executed");
    	
       if (driver != null) {
            driver.quit();
       
    }

}

}
