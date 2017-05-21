package fr.mgargadennec.blossom.integration;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserRepository;
import fr.mgargadennec.blossom.integration.builder.UserBuilder;
import fr.mgargadennec.blossom.integration.utils.CucumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zoula_000 on 19/05/2017.
 */
@ContextConfiguration(classes = CucumberConfiguration.class)
@CucumberStepsDefinition
public class CucumberSteps {

    private static ChromeDriverService service;
    private WebDriver driver;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    private CucumberUtils cucumberUtils;

    private User temporaryUser;
    private Long selectedItemId;
    private WebElement currentElement;

    @cucumber.api.java.Before
    public void createDriver() throws IOException {

        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("G:\\projects\\utils\\chromedriver.exe"))
                .usingAnyFreePort()
                .build();

        service.start();

        driver = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());

        cucumberUtils = new CucumberUtils(driver);

    }

    @cucumber.api.java.After
    public void closeDriver() throws IOException {
        datasCleanUp();
        driver.close();
        service.stop();
    }

    private void datasCleanUp() {
        if (temporaryUser != null) {
            this.userRepository.delete(temporaryUser.getId());
        }
    }

    private enum ACTION {
        LIST(""),
        CREATE("/create"),
        EDIT("/edit"),
        DETAIL("");

        private String path;

        ACTION(String path) {
            this.path = path;
        }

    }

    private enum PAGE {
        LOGIN("/blossom"),
        HOME("/blossom"),
        LOGIN_ERROR("/blossom/login", "error");

        private final String path;
        private final String query;

        PAGE(String path) {
            this(path, null);
        }

        PAGE(String path, String query) {
            this.path = path;
            this.query = query;
        }

        public String getPath() {
            return this.path;
        }

        boolean isPageMatchesWith(WebDriver driver, ACTION action) {
            try {
                URL thisUrl = new URL(driver.getCurrentUrl());
                return
                        thisUrl.getPath().toLowerCase().trim().equals(this.path + action.path) &&
                                (StringUtils.isBlank(this.query) || thisUrl.getQuery().toLowerCase().trim().equals(this.query));
            } catch (Exception e) {
                throw new IllegalArgumentException("Malformed url " + driver.getCurrentUrl(), e);
            }
        }

        private Pattern createPattern = Pattern.compile(".*/([0-9]+)(/.*)?(\\?.*)?");

        boolean isPageMatchesWith(WebDriver driver, ACTION action, Long elementId) {
            try {
                URL thisUrl = new URL(driver.getCurrentUrl());

                Matcher matcher = createPattern.matcher(thisUrl.getPath());

                if (matcher.find()) {
                    String id = matcher.group(1);
                    boolean idsMatch = elementId == null ? true : elementId.equals(Long.parseLong(id));

                    boolean queryMatch = StringUtils.isBlank(this.query) ? true : thisUrl.getQuery().toLowerCase().trim().contains(this.query);

                    boolean actionMatch = StringUtils.isBlank(action.path) ? true : matcher.group(2).equals(action.path);

                    return queryMatch && idsMatch && actionMatch;

                }
                return false;

            } catch (Exception e) {
                throw new IllegalArgumentException("Malformed url " + driver.getCurrentUrl(), e);
            }
        }

    }

    private Long getItemIdFromUrl(String url) {
        String entityId = url.substring(url.lastIndexOf("/") + 1);
        return Long.parseLong(entityId);
    }

    /****************************************************************************************************
     * WHEN
     ****************************************************************************************************/

    @Given("I am authenticated with \"([^\"]*)\" user on client (\\d+)")
    public void I_am_authenticated_with_role_and_client(String role, long clientId) throws Exception {

        temporaryUser = new UserBuilder()
                .id(19891L)
                .email("newuser19891@yopmail.com")
                .passwordHash("$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C")
                .activated(true)
                .toUser();
        this.userRepository.save(temporaryUser);

        this.I_am_on_page("login");
        this.I_type_in_field("user19891", "username");
        this.I_type_in_field("demo", "password");
        this.I_click_on_element("login");
    }

    @Given("I am authenticated with user \"([^\"]*)\"")
    public void I_am_authenticated(String identifier) throws Exception {
        this.I_am_on_page("login");
        this.I_type_in_field("user1", "username");
        this.I_type_in_field("demo", "password");
        this.I_click_on_element("login");
    }

    @Given("^I am on \"([^\"]*)\" page$")
    public void I_am_on_page(String page) {
        PAGE requestedPage = PAGE.valueOf(page.toUpperCase());
        driver.get("http://localhost:" + randomServerPort + requestedPage.getPath());
    }

    @When("^I type \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void I_type_in_field(String value, String fieldId) {
        WebElement identifier = this.cucumberUtils.findElement(By.name(fieldId));
        identifier.clear();
        identifier.sendKeys(value);
    }

    @When("^I add \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void I_add_in_field(String value, String fieldId) {

        WebElement selectElement = this.cucumberUtils.findElement(By.id(fieldId.replaceAll("\\.", "_") + "_chosen"));

        WebElement selectInput = selectElement.findElement(By.cssSelector(".chosen-choices"));
        selectInput.click();

        List<WebElement> selectOptions = selectElement.findElements(By.cssSelector(".chosen-results li"));

        for (WebElement option : selectOptions) {
            if (value.equals(option.getText())) {
                option.click();
            }
        }

    }

    @When("^I remove \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void I_remove_in_field(String value, String fieldId) {

        WebElement selectElement = this.cucumberUtils.findElement(By.id(fieldId.replaceAll("\\.", "_") + "_chosen"));

        List<WebElement> choices = selectElement.findElements(By.cssSelector(".search-choice"));

        for (WebElement choice : choices) {
            if (value.equals(choice.getText())) {
                choice.findElement(By.className("search-choice-close")).click();
            }
        }

    }

    @When("^I select \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void I_select_in_field(String value, String fieldId) {

        WebElement selectElement = this.cucumberUtils.findElement(By.id(fieldId.replaceAll("\\.", "_") + "_chosen"));

        WebElement selectInput = selectElement.findElement(By.cssSelector(".chosen-single"));
        selectInput.click();

        List<WebElement> selectOptions = selectElement.findElements(By.cssSelector(".chosen-results li"));

        for (WebElement option : selectOptions) {
            if (value.equals(option.getText())) {
                option.click();
            }
        }
    }

    @When("^I type nothing in \"([^\"]*)\" field$")
    public void I_type_nothing_in_field(String fieldId) {
        WebElement identifier = this.cucumberUtils.findElement(By.id(fieldId));
        identifier.clear();
    }

    @When("^I click on \"([^\"]*)\" element")
    public void I_click_on_element(String inputId) {
        WebElement button = this.cucumberUtils.findElement(By.id(inputId));
        button.click();
    }

    @When("^I display import result")
    public void I_display_import_result() {
        this.I_wait_during_x_seconds(200);
        this.I_click_on_element("importResultLink");
    }

    @When("^I select an element with property \"([^\"]*)\" equals to \"([^\"]*)\" in \"([^\"]*)\" list")
    public void I_select_an_element_with_property_equals_to_in(String property, String expectedValue, String listId) {

        WebElement matchingElement = this.cucumberUtils.lookForElement(property, expectedValue, listId);
        if (matchingElement != null) {

            List<WebElement> aList = matchingElement.findElements(By.cssSelector("a"));
            if (!aList.isEmpty()) {
                String href = aList.get(0).getAttribute("href");
                this.selectedItemId = this.getItemIdFromUrl(href);

                aList.get(0).click();
                return;
            }

        }

    }

    @When("^I upload the file \"([^\"]*)\"$")
    public void I_upload_the_file(String filename) {

        this.I_wait_during_x_seconds(200);
        WebElement element = this.cucumberUtils.findElement(By.id("fileupload"));

        try {
            File requestedFile = new ClassPathResource("com/csm/integration/features/data/import/" + filename).getFile();
            element.sendKeys(requestedFile.getAbsolutePath());
        } catch (IOException e) {

        }

    }

    @When("^I wait during (\\d+) milliseconds")
    public void I_wait_during_x_seconds(int millisecondsQuantity) {
        try {
            Thread.sleep(millisecondsQuantity);
        } catch (Exception e) {

        }
    }

    /****************************************************************************************************
     * THEN
     ****************************************************************************************************/

    @Then("^I should be on \"([^\"]*)\" page$")
    public void I_should_be_on_page(String page) {
        PAGE requestedPage = PAGE.valueOf(page.toUpperCase());
        Assert.assertTrue("Current url " + driver.getCurrentUrl() + " and requested url " + requestedPage.getPath() + " doesn't match", requestedPage.isPageMatchesWith(driver, ACTION.LIST));
    }

    @Then("^I should be on create \"([^\"]*)\" page$")
    public void I_should_be_on_create_page(String page) {
        PAGE requestedPage = PAGE.valueOf(page.toUpperCase());
        Assert.assertTrue("Current url " + driver.getCurrentUrl() + " and requested url " + requestedPage.getPath() + " doesn't match", requestedPage.isPageMatchesWith(driver, ACTION.CREATE));
    }

    @Then("^I should be on the \"([^\"]*)\" detail page of the selected item$")
    public void I_should_be_on_selected_item_page(String page) throws Exception {
        PAGE requestedPage = PAGE.valueOf(page.toUpperCase());
        Assert.assertTrue("Current url " + driver.getCurrentUrl() + " and requested url " + requestedPage.getPath() + " on " + this.selectedItemId + " item doesn't match", requestedPage.isPageMatchesWith(driver, ACTION.DETAIL, this.selectedItemId));
    }

    @Then("^the page displays \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void page_display_value_for_property(String expectedValue, String property) {
        WebElement element = this.cucumberUtils.findElement(By.name(property));
        String actualValue = StringUtils.isNotBlank(element.getAttribute("value")) ? element.getAttribute("value") : element.getText();
        Assert.assertTrue("Expected value: " + expectedValue + ". Actual value: " + actualValue, expectedValue.equals(actualValue));
    }

    @Then("^the page displays nothing for property \"([^\"]*)\"$")
    public void page_display_nothing_for_property(String property) {
        WebElement element = this.cucumberUtils.findElement(By.name(property));
        String actualValue = StringUtils.isNotBlank(element.getAttribute("value")) ? element.getAttribute("value") : element.getText();
        Assert.assertTrue("Empty property expected. Actual value " + actualValue, StringUtils.isBlank(actualValue));
    }

    @Then("^import result displays (\\d+) created entities, (\\d+) updated entities and (\\d+) entities in error")
    public void import_result_displays(int createdEntitiesQuantity, int updateEntitiesQuantity, int entitiesInErrorQuantity) {

        this.I_wait_during_x_seconds(200);

        WebElement created = this.cucumberUtils.findElement(By.id("createdEntitiesQuantity"));
        Assert.assertEquals(createdEntitiesQuantity, Integer.parseInt(created.getText()));

        WebElement updated = this.cucumberUtils.findElement(By.id("updatedEntitiesQuantity"));
        Assert.assertEquals(updateEntitiesQuantity, Integer.parseInt(updated.getText()));

        WebElement error = this.cucumberUtils.findElement(By.id("entitiesInErrorQuantity"));
        Assert.assertEquals(entitiesInErrorQuantity, Integer.parseInt(error.getText()));


    }

    @Then("^import result displays (\\d+) errors for line (\\d+)")
    public void import_result_displays_error_for_line(int errorsQuantity, int lineIndex) {

        this.I_wait_during_x_seconds(200);

        WebElement errorLine = this.cucumberUtils.findElement(By.id("error_line_" + lineIndex));
        Assert.assertNotNull(errorLine);

        List<WebElement> errorsItems = errorLine.findElements(By.className("errorItem"));
        Assert.assertEquals(errorsQuantity, errorsItems.size());

    }

    @Then("^import result displays (\\d+) errors for header")
    public void import_result_displays_error_for_header(int errorsQuantity) {
        this.import_result_displays_error_for_line(errorsQuantity, 1);
    }

    @Then("^there is an error on property \"([^\"]*)\"$")
    public void there_is_an_error_on_property(String property) {
        WebElement element = this.cucumberUtils.findElement(By.name(property));

        WebElement parent = (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].parentNode;", element);

        List<WebElement> errors = parent.findElements(By.cssSelector("span.error"));

        Assert.assertTrue(!errors.isEmpty());
    }

    @Then("^there should be an element with property \"([^\"]*)\" equals to \"([^\"]*)\" in list \"([^\"]*)\"$")
    public void there_should_be_an_element_with_property_in_list(String property, String expectedValue, String
            listId) {
        WebElement matchingElement = this.cucumberUtils.lookForElement(property, expectedValue, listId);
        this.currentElement = matchingElement;
        Assert.assertNotNull(matchingElement);
    }

    @Then("^there shouldn't be element with property \"([^\"]*)\" equals to \"([^\"]*)\" in list \"([^\"]*)\"$")
    public void there_shouldnt_element_with_property_in_list(String property, String expectedValue, String listId) {
        WebElement matchingElement = this.cucumberUtils.lookForElement(property, expectedValue, listId);
        this.currentElement = matchingElement;
        Assert.assertNull(matchingElement);
    }

    @Then("^its property \"([^\"]*)\" is equals to \"([^\"]*)\"$")
    public void property_equals_to(String property, String expectedValue) {
        Assert.assertTrue(this.cucumberUtils.containsMatchingProperty(this.currentElement, property, expectedValue));
    }

    @Then("^its property \"([^\"]*)\" is empty$")
    public void property_equals_to(String property) {
        Assert.assertTrue(this.cucumberUtils.containsMatchingProperty(this.currentElement, property, ""));
    }

}
