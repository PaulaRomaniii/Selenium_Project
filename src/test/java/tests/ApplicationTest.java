package tests;

import org.testng.annotations.Test;
import utils.TestData;
import java.io.IOException;

public class ApplicationTest extends BaseTest {

    @Test
    public void testAboutSection()
    {
        helper.clickAboutAndAssert();
    }
    @Test
    public void testHoverFunctionality() {
        helper.assertOnHovering();
    }
    @Test
    public void testFormFilling() throws IOException {
        TestData testData = TestData.loadTestData("src/test/resources/example.json");
        helper.fillFormData(testData.getFormData().toMap());
    }
    @Test
    public void testFileUpload() {
       // helper.fileUpload();
    }

    @Test
    public void testDarkModeToggle() {
        helper.darkModeToggle();
    }
    @Test
    public void testKeyPress() {
        helper.pressKeysAndAssert();
    }
    @Test
    public void testTableSortingFunctionality() {
        helper.testTableSorting();
    }
    @Test
    public void testNotificationsFunctionality() {
        helper.testNotifications();
    }
    @Test
    public void testBrokenImagesFunctionality() {
        helper.testBrokenImages();
    }
    @Test
    public void testCheckboxesFunctionality() {
        helper.testCheckboxes();
    }
}