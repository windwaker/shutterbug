package org.example;

import static org.junit.Assert.assertTrue;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AppTest 
{
    /**
     * http://automationtesting.in/take-screenshot-using-shutterbug/
     * https://github.com/assertthat/selenium-shutterbug
     */

    private static String userDir = System.getProperty("user.dir");
    private static URL homePage;
    private static WebDriver driver;

    static {
        try {
            homePage = new URL("http://www.vhi.ie");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void tearDown(){
        driver.quit();
    }

    @Test
    public void takeScreenshot()
    {
        driver.get(homePage.toString());
        Shutterbug.shootPage(driver).save();
    }

    @Test
    public void takeScreenshotWithDesiredName()
    {
        driver.get(homePage.toString());
        Shutterbug.shootPage(driver).withName("vhi").save();
    }

    @Test
    public void elementScreenshotWithDesiredName()
    {
        driver.get(homePage.toString());
        WebElement logo = driver.findElement(By.id("logoId"));
        Shutterbug.shootElement(driver,logo).withName("LogoElementScreenshot").save();
    }

    @Test
    public void highlightElementScreenshot ()
    {
        driver.get(homePage.toString());
        WebElement logo = driver.findElement(By.id("logoId"));
        Shutterbug.shootPage(driver).highlightWithText(logo, "Logo identified & Validated").withName("Logo validated").save();
    }

    @Test
    public void captureFullPageScreenshotWithDesiredName()
    {
        driver.get(homePage.toString());
        Shutterbug.shootPage(driver, Capture.FULL_SCROLL ,500,true).withName("FullPageScreenshot").save();
    }

    @Test
    public void captureFullPageScreenshotWithMonochrome()
    {
        driver.get(homePage.toString());
        WebElement QuestionsLogo = driver.findElement(By.cssSelector("#promoButtonPrimaryDesktopId"));
        Shutterbug.shootPage(driver, Capture.FULL_SCROLL,500,true)
                .monochrome(QuestionsLogo).withName("FullPageScreenshotMonogram").save();
    }

    @Test
    public void captureFullPageScreenshotWithDesiredNameAndCompare() throws IOException, InterruptedException {
        driver.get(homePage.toString());
        Thread.sleep(3000);
        Shutterbug.shootPage(driver).withName("Expected").save();
        File image = new File(userDir + "/screenshots/Expected.png");
        BufferedImage expectedImage = ImageIO.read(image);
        boolean status = Shutterbug.shootPage(driver).withName("Actual").equals(expectedImage,0.01);
        Assert.assertTrue(status);
    }

    @Test
    public void captureFullPageScreenshotWithDesiredNameAndGetDiff() throws IOException, InterruptedException {
        driver.get(homePage.toString());
        File image = new File(userDir + "/screenshots/Expected.png");
        BufferedImage expectedImage = ImageIO.read(image);
        Thread.sleep(3000);
        boolean status = Shutterbug
            .shootPage(driver)
                .equalsWithDiff(expectedImage, userDir + "/screenshots/Diff", 0.01);
        Assert.assertTrue(status);
    }

    private static void processCSVFile() throws IOException, CsvException {
        //String userDir = System.getProperty("user.dir");
        CSVReader reader = new CSVReader(new FileReader(userDir + "/csv/urls.csv"));
        List list = reader.readAll();

        //Getting the Iterator object
        Iterator it = list.iterator();
        while(it.hasNext()) {
            String[] str = (String[]) it.next();
            System.out.println(Arrays.toString(str));
        }
    }

    public static void main(String args[]) throws Exception {
        processCSVFile();
    }

}
