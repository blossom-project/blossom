package fr.mgargadennec.blossom.integration.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by zoula_000 on 19/05/2017.
 */
public class CucumberUtils {

    private final WebDriver driver;

    public CucumberUtils(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement lookForElement(String property, String expectedValue, String listId) {

        WebElement hasNext;

        do {

            hasNext = computeHasElement("nextPage");

            // GETTING THE ITEMS LIST
            List<WebElement> itemsList = driver.findElements(By.cssSelector("#" + listId + " tr"));

            // LOOKING FOR ITEM
            for (WebElement element : itemsList) {
                if (this.containsMatchingProperty(element, property, expectedValue)) {
                    return element;
                }
            }

            if (hasNext != null) {
                hasNext.click();
            }

        } while (hasNext != null);

        return null;

    }

    private WebElement computeHasElement(String elementId) {
        try {
            WebElement element = this.driver.findElement(By.id(elementId));
            if (element.isDisplayed() && !"true".equals(element.getAttribute("disabled"))) {
                return element;
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public boolean containsMatchingProperty(WebElement element, String property, String expectedValue) {
        try {
            WebElement subelement = element.findElement(By.cssSelector("td." + property + "_property"));
            String elementText = subelement.getText().replaceAll("\n", ",");
            if (elementText.equals(expectedValue)) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public WebElement findElement(By by) {
        List<WebElement> elements = driver.findElements(by);
        for (WebElement element : elements) {
            try {
                if (element.isEnabled() && element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {

            }
        }

        return null;
    }

}
