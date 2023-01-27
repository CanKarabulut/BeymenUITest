package com.example.beymenuitest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainPageTest {

    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.beymen.com");
        mainPage = new MainPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void search() throws InterruptedException {
        List<String> result = readFromExcel();
        mainPage.acceptCookies.click();

        mainPage.genderManButton.click();

        WebElement searchField = driver.findElement(By.cssSelector("[class='default-input o-header__search--input']"));
        searchField.sendKeys(result.get(0));
        Thread.sleep(2000);
        clearInputArea(searchField);
        Thread.sleep(3000);
        searchField.sendKeys(result.get(1));
        searchField.sendKeys(Keys.RETURN);
        Thread.sleep(3000);
        List<WebElement> products = driver.findElement(By.id("productList")).findElements(By.className("o-productList__item"));
        Thread.sleep(2000);
        WebElement randomProduct = products.get(new Random().nextInt(products.size()));

        String productName = randomProduct.findElement(By.className("m-productCard__title")).getText();
        String productPrice = randomProduct.findElement(By.className("m-productCard__newPrice")).getText();
        writeToFile(productName, productPrice);

        randomProduct.click();

        WebElement productSize = driver.findElements(By.className("m-variation__item")).stream().findFirst().get();
        productSize.click();

        Thread.sleep(5000);

        mainPage.addBasket.click();
        Thread.sleep(5000);
        WebElement cart = driver.findElement(By.className("-cart"));
        cart.click();
        Thread.sleep(5000);
        WebElement grandTotal = driver.findElement(By.className("-grandTotal"));
        String finalPrice = grandTotal.findElement(By.className("m-orderSummary__value")).getText();

        assertTrue(finalPrice.equalsIgnoreCase(productPrice));

        Select quantity = new Select(driver.findElement(By.className("a-selectControl")));
        quantity.selectByValue("2");
        String finalQuantity = new Select(driver.findElement(By.className("a-selectControl"))).getAllSelectedOptions().get(0).getText();
        assertTrue(finalQuantity.contains("2"));

        WebElement basketRemove = driver.findElement(By.className("m-basket__remove"));
        basketRemove.click();

        WebElement emptyMessage = driver.findElement(By.className("m-empty__messageTitle"));
        assertNotNull(emptyMessage);
    }

    private static void clearInputArea(WebElement searchField) {
        searchField.sendKeys(Keys.CONTROL + "a");
        searchField.sendKeys(Keys.DELETE);
    }

    private List<String> readFromExcel() {
        List<String> result = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(new File(getClass().getClassLoader().getResource("query.xlsx").toURI()));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    result.add(currentCell.getStringCellValue());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void writeToFile(String productName, String productPrice) {
        try {
            FileWriter myWriter = new FileWriter("product.txt");
            myWriter.write(productName);
            myWriter.write(productPrice);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
