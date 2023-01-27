package com.example.beymenuitest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage {
    @FindBy(xpath = "//*[@id='onetrust-accept-btn-handler']")
    public WebElement acceptCookies;

    @FindBy(xpath = "//*[@id='genderManButton']")
    public WebElement genderManButton;

    @FindBy(xpath = "//div[@data-test='main-menu-item' and @data-test-marker = 'Developer Tools']")
    public WebElement toolsMenu;

    @FindBy(css = "[data-test='site-header-search-action']")
    public WebElement searchButton;

    @FindBy(xpath = "//*[@id='priceNew']")
    public WebElement price;

    @FindBy(xpath = "//*[@id='o-productDetail__description']")
    public WebElement productDetail;

    @FindBy(xpath = "//*[@id='addBasket']")
    public WebElement addBasket;

    @FindBy(xpath = "//*[@id='icon icon-cart icon-cart-active']")
    public WebElement cart;

    @FindBy(xpath = "//*[@id='removeCartItemBtn0-key-0']")
    public WebElement removeItem;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
