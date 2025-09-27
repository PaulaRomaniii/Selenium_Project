package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class HelperMethods {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private void clickElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void clickAboutAndAssert() {
        try {
            // Click on the About element
            WebElement aboutLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h3[contains(text(),'About')]")
            ));
            aboutLink.click();
            System.out.println("Clicked on About link");

            // Wait for and assert the heading appears
            WebElement aboutHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'About Test Automation Practice')]")
            ));

            Assert.assertTrue(aboutHeading.isDisplayed(), "About heading is not displayed");
            Assert.assertEquals(aboutHeading.getText(), "About Test Automation Practice",
                    "About heading text mismatch");

            System.out.println("About heading verified successfully: " + aboutHeading.getText());

        } catch (TimeoutException e) {
            Assert.fail("About element not found or not clickable: " + e.getMessage());
        }
    }

    public HelperMethods(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    private void navigateToSection(String iconClass) {
        try {
            WebElement navIcon = driver.findElement(By.xpath(
                    "//div[contains(@class,'flex-shrink-0') and .//svg[contains(@class,'" + iconClass + "')]]"
            ));
            navIcon.click();
            System.out.println("Navigated to section: " + iconClass);
        } catch (NoSuchElementException e) {
            System.out.println("⚠ Navigation icon (" + iconClass + ") not found, assuming already in section");
        }
    }

    public void assertOnHovering() {
        WebElement navIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("svg.lucide.lucide-mouse-pointer2")
        ));
        navIcon.click();
        for (int i = 1; i <= 3; i++) {
            WebElement figure = driver.findElement(By.cssSelector("[data-test='hover-figure-" + i + "']"));
            actions.moveToElement(figure).perform();

            WebElement caption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[data-test='hover-caption-" + i + "']")));
            Assert.assertTrue(caption.isDisplayed(), "Caption " + i + " not visible");
            Assert.assertTrue(caption.getText().contains("Figure " + i), "Caption text mismatch for Figure " + i);
        }
    }

    public void fillFormData(java.util.Map<String, String> formData) {
        // كليك على كارت الـ Forms الأول
        WebElement formsCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-test='feature-card-forms']"))
        );
        formsCard.click();

        // نملأ البيانات
        for (String key : formData.keySet()) {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.name(key)
            ));
            input.clear();
            input.sendKeys(formData.get(key));
        }

        // كليك على زرار Sign In
        WebElement signInBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-test='submit-button']"))
        );
        signInBtn.click();

        // مش هنعمل أي تحقق، هنسيب التست يقفل طبيعي بعد الضغط
    }

    public void darkModeToggle() {
        // Locate the theme toggle button
        WebElement toggle = driver.findElement(By.cssSelector("button[data-test='theme-toggle']"));

        // Click it
        toggle.click();

        // Wait for <html> tag to have "dark" class
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.attributeContains(
                driver.findElement(By.tagName("html")), "class", "dark"
        ));

        // Assert dark mode is applied
        String htmlClass = driver.findElement(By.tagName("html")).getAttribute("class");
        Assert.assertTrue(htmlClass.contains("dark"), "Dark mode not applied!");
    }

    public void pressKeysAndAssert() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

        // 🔹 Click on Key Press link
        WebElement keyPressLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h3[normalize-space()='Key Press']")));
        keyPressLink.click();

        // 🔹 Check if input exists, otherwise type into body
        List<WebElement> inputs = driver.findElements(By.cssSelector("input, [data-test='keypress-input']"));
        WebElement target = inputs.isEmpty()
                ? driver.findElement(By.tagName("body"))
                : inputs.get(0);

        target.click(); // focus

        // 🔹 Press keys
        actions.sendKeys("abcde").perform();

        // 🔹 Assert last key visible
        WebElement lastKey = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='last-key-pressed']")));
        System.out.println("Last key pressed: " + lastKey.getText());
    }

    public void testTableSorting() {
        try {
            // Click on the Tables element
            WebElement tablesLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h3[contains(text(),'Tables')]")
            ));
            tablesLink.click();
            System.out.println("Clicked on Tables link");

            // Wait for table to be visible
            WebElement tableBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("tbody.bg-white")
            ));

            // Click on the ID header to sort
            WebElement idHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'flex items-center') and contains(.,'id')]")
            ));
            idHeader.click();
            System.out.println("Clicked on ID header");

            // Wait for sorting to complete and verify ascending
            wait.until(driver -> {
                List<WebElement> cells = driver.findElements(By.cssSelector("[data-test^='table-cell-id-']"));
                List<Integer> ids = cells.stream()
                        .map(WebElement::getText)
                        .map(Integer::parseInt)
                        .toList();
                return isSortedAscending(ids);
            });

            List<WebElement> ascendingCells = driver.findElements(By.cssSelector("[data-test^='table-cell-id-']"));
            List<Integer> ascendingIds = ascendingCells.stream()
                    .map(WebElement::getText)
                    .map(Integer::parseInt)
                    .toList();

            assertSortedAscending(ascendingIds, "First click should sort ascending");
            System.out.println("Ascending order verified: " + ascendingIds);

            // Click again for descending order
            idHeader.click();
            System.out.println("Clicked on ID header again for descending order");

            // Wait for descending sort
            wait.until(driver -> {
                List<WebElement> cells = driver.findElements(By.cssSelector("[data-test^='table-cell-id-']"));
                List<Integer> ids = cells.stream()
                        .map(WebElement::getText)
                        .map(Integer::parseInt)
                        .toList();
                return isSortedDescending(ids);
            });

            List<WebElement> descendingCells = driver.findElements(By.cssSelector("[data-test^='table-cell-id-']"));
            List<Integer> descendingIds = descendingCells.stream()
                    .map(WebElement::getText)
                    .map(Integer::parseInt)
                    .toList();

            assertSortedDescending(descendingIds, "Second click should sort descending");
            System.out.println("Descending order verified: " + descendingIds);

        } catch (TimeoutException e) {
            Assert.fail("Table sorting test failed: " + e.getMessage());
        }
    }

    private boolean isSortedAscending(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i) > numbers.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSortedDescending(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i) < numbers.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void assertSortedAscending(List<Integer> numbers, String message) {
        Assert.assertTrue(isSortedAscending(numbers), message + " - Values: " + numbers);
    }

    private void assertSortedDescending(List<Integer> numbers, String message) {
        Assert.assertTrue(isSortedDescending(numbers), message + " - Values: " + numbers);
    }

    public void testNotifications() {
        // Click on Notifications section
        clickElement(By.xpath("//h3[text()='Notifications']"));

        // Click success message button
        clickElement(By.cssSelector("button[data-test='add-success']"));

        // Assert success message appears
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'This is a success notification message.')]")
        ));

        Assert.assertTrue(successMessage.isDisplayed(), "Success message should be displayed");
    }

    public void testBrokenImages() {
        // Click on Broken Images section
        clickElement(By.xpath("//h3[text()='Broken Images']"));

        // Wait for images to load and then check them
        testImage(0, true, "Valid image should load");
        testImage(1, false, "Broken image 1 should not load");
        testImage(2, false, "Broken image 2 should not load");
    }

    private void testImage(int index, boolean shouldLoad, String message) {
        // Wait for the image to be present
        WebElement img = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-test='image-" + index + "']")
        ));

        // Wait a bit for images to load (especially the valid one)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isLoaded = isImageLoaded(img);

        if (shouldLoad) {
            Assert.assertTrue(isLoaded, message);
        } else {
            Assert.assertFalse(isLoaded, message);
        }
    }

    private boolean isImageLoaded(WebElement img) {
        try {
            // More robust check that handles different scenarios
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "if (!arguments[0].complete) return false; " +
                            "if (typeof arguments[0].naturalWidth == 'undefined') return false; " +
                            "return arguments[0].naturalWidth > 0 && arguments[0].naturalHeight > 0;",
                    img
            );
            return result != null && (Boolean) result;
        } catch (Exception e) {
            return false;
        }
    }

    public void testCheckboxes() {
        // Click on Checkboxes section
        clickElement(By.xpath("//h3[text()='Checkboxes']"));

        // Toggle checkboxes
        toggleCheckbox("Checkbox 1", true);
        toggleCheckbox("Checkbox 2", false);
        toggleCheckbox("Checkbox 3", true);

        // Test Check All
        clickElement(By.cssSelector("[data-test='check-all-button']"));
        assertAllCheckboxesState(true);

        // Test Uncheck All
        clickElement(By.cssSelector("[data-test='uncheck-all-button']"));
        assertAllCheckboxesState(false);
    }

    private void toggleCheckbox(String label, boolean check) {
        WebElement checkbox = driver.findElement(
                By.xpath("//span[text()='" + label + "']/preceding-sibling::input")
        );
        if (checkbox.isSelected() != check) {
            checkbox.click();
        }
    }

    private void assertAllCheckboxesState(boolean expectedState) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        for (WebElement checkbox : checkboxes) {
            Assert.assertEquals(checkbox.isSelected(), expectedState,
                    "Checkbox state mismatch for: " + checkbox.getAttribute("name"));
        }
    }
    }

