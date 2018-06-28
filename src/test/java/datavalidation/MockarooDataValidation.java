package datavalidation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
	WebDriver driver;
//	List<String> cities;
//	List<String> countries;
//	Set<String> citiesSet;
//	Set<String> countriesSet;


	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		driver.get("https://mockaroo.com/");
	}

	@AfterClass
	public void tearDown() throws InterruptedException {
		Thread.sleep(4000);
		driver.quit();
	}

	@BeforeMethod
	public void navigateToMockaroo() {

	}
	// Step 3. Assert title is correct.

	@Test(priority = 1)
	public void titleTest() {
		assertEquals(driver.getTitle(),
				"Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel");
	}

	// Step 4. Assert Mockaroo and realistic data generator are displayed
	@Test(priority = 2)
	public void titleValidation() {
		String expected = "mockaroo";
		String actual = driver.findElement(By.xpath("//div[@class='brand']")).getText();
		assertEquals(actual, expected);
	}

	@Test(priority = 3)
	public void title2Validation() {
		String expected = "realistic data generator";
		String actual = driver.findElement(By.xpath("//div[@class='tagline']")).getText();
		assertEquals(actual, expected);
	}

	// Step 5. Remove all existing fields by clicking on x icon link
	@Test(priority = 4)
	public void remove() throws InterruptedException {

		List<WebElement> buttons = driver

				.findElements(By.xpath("//div[@id='fields']//a[@class='close remove-field remove_nested_fields']"));

		for (WebElement eachButton : buttons) {

			eachButton.click();
		}
	}

	// Step 6. Assert that ‘Field Name’ , ‘Type’, ‘Options’ labels are displayed
	@Test(priority = 5)
	public void labelCheck() {
		// Field Name
		WebElement element1 = driver.findElement(By.xpath("//div[@class='column column-header column-name']"));
		assertTrue(element1.isDisplayed());
		// Type
		WebElement element2 = driver.findElement(By.xpath("//div[@class='column column-header column-type']"));
		assertTrue(element2.isDisplayed());
		// Options
		WebElement element3 = driver.findElement(By.xpath("//div[@class='column column-header column-options']"));
		assertTrue(element3.isDisplayed());
	}

	// Step 7. Assert that ‘Add another field’ button is enabled. Find using xpath
	// with tagname and text. isEnabled() method in selenium
	@Test(priority = 6)
	public void isEnabled() {
		assertTrue(driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']"))
				.isEnabled());
	}

	// Step 8.Assert that default number of rows is 1000.

	@Test(priority = 7)
	public void defaultRows() {
		assertEquals(driver.findElement(By.xpath("//input[@id='num_rows']")).getAttribute("value"), "1000");
	}

	// Step 9.Assert that default format selection is CSV

	@Test(priority = 8)
	public void defaultFormat() {
		assertEquals(driver.findElement(By.xpath("//select[@id='schema_file_format']")).getAttribute("value"), "csv");
	}

	// Step 10.Assert that Line Ending is Unix(LF)

	@Test(priority = 9)
	public void defaultLineEnding() {
		assertEquals(driver.findElement(By.xpath("//select[@id='schema_line_ending']")).getAttribute("value"), "unix");
	}

	// Step 11. Assert that header checkbox is checked and BOM is unchecked
	@Test(priority = 10)
	public void headerIsChecked() {
		assertTrue(driver.findElement(By.xpath("//input[@id='schema_include_header']")).isSelected(), "true");
		assertFalse(driver.findElement(By.xpath("//input[@id='schema_bom']")).isSelected(), "true");
	}

	// 12. Click on ‘Add another field’ and enter name “City”
	@Test(priority = 11)
	public void testAddField() {
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		driver.findElement(By.xpath(
				"(//div[@id='fields']//input[starts-with(@id, 'schema_columns_attributes_')][contains(@id,'name')])[last()]"))
				.sendKeys("City");

	}

	// 13. Click on Choose type and assert that Choose a Type dialog box is
	// displayed.
	@Test(priority = 12)
	public void testIsDisplayed() {
		driver.findElement(By.xpath("(//div[@class='fields']//input[@class='btn btn-default'])[last()]")).click();

		assertTrue(driver.findElement(By.xpath("//body[@class='mockaroo modal-open']")).isDisplayed());

	}

	//// 14. Search for “city” and click on City on s}
	@Test(priority = 13)
	public void testSearchForCity() throws InterruptedException {
		driver.findElement(By.id("type_search_field")).sendKeys("city");
		driver.findElement(By.xpath("//*[text()='City']")).click();
		Thread.sleep(3000);
	}

	// 15. Repeat steps 12-14 with field name and type “Country”
	@Test(priority = 14)
	public void addCountry() throws InterruptedException, IOException {

		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		driver.findElement(By.xpath(
				"(//div[@id='fields']//input[starts-with(@id, 'schema_columns_attributes_')][contains(@id,'name')])[last()]"))
				.sendKeys("Country");

		driver.findElement(By.xpath("(//div[@class='fields']//input[@class='btn btn-default'])[last()]")).click();

		assertTrue(driver.findElement(By.xpath("//body[@class='mockaroo modal-open']")).isDisplayed());
		Thread.sleep(2000);

		driver.findElement(By.id("type_search_field")).clear();

		driver.findElement(By.id("type_search_field")).sendKeys("country");
		driver.findElement(By.xpath("//*[text()='Country']")).click();

		Thread.sleep(1000);
	}

	@Test (priority = 15) // Step 16-17. click on download button, assert 1 row
	public void loadLists() throws IOException {
		driver.findElement(By.id("download")).click();
		String fileName = "/Users/lesia/Downloads/MOCK_DATA (16).csv";
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line = bufferedReader.readLine();
		assertEquals(line, "City,Country");

		// Step 19-21 add countries to country ArrayList, cities to city ArrayList
		List<String> cities = new ArrayList();
		List<String> countries = new ArrayList();
	    int count = 0;
		while ((line = bufferedReader.readLine()) != null) {
			cities.add(line.substring(0, line.indexOf(",")));
			countries.add(line.substring(line.indexOf(",") + 1));

			count++;
		}
		// System.out.println(count);
		assertEquals(count, 1000);
		System.out.println(cities);
		System.out.println(countries);
	
	// Step 22 sort cities, find the shortest and longest city name

	
		Collections.sort(cities);
		
int longestIndex=0;
		int longestName = cities.get(0).length();
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).length() > longestName) {
				longestName = cities.get(i).length();
				longestIndex=i;
			}
		}
		System.out.println(
				cities.get(longestIndex) + " is the longest city name in cities list. It has " + longestName + " letters");;
	
int shortestName = cities.get(0).length();
int shortestIndex=0;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).length() < shortestName) {
				shortestName = cities.get(i).length();
				i = shortestIndex;
			}
		}
		System.out.println(
				cities.get(shortestIndex) + " is the shortest city name in cities list. It has " + shortestName + " letters");

	
	
	//Step 23 Unique countries frequency
	
			Set<String> uniqueCountries = new HashSet(countries);
			for (String each : uniqueCountries) {
				System.out.println(each + " - " + Collections.frequency(countries, each));
			}
			
			//Step 24-24 add all Cities to citiesSet HashSet and all countries to country HashSet
			FileReader fileReader2 = new FileReader(fileName);
			BufferedReader bufferedReader2 = new BufferedReader(fileReader);

			Set<String> citiesSet = new HashSet();
			Set<String> countriesSet = new HashSet();
		    int number = 0;
			while ((line = bufferedReader.readLine()) != null) {
				cities.add(line.substring(0, line.indexOf(",")));
				countries.add(line.substring(line.indexOf(",") + 1));

				number++;
				
				// Step 27. Count how many unique countries are in Countries ArrayList and
			
				List<String> countryCount=new ArrayList();

				for (int i = 0; i < countries.size(); i++) {
					for(int j=0; j<countries.size(); j++) {
						if (!countries.get(i).equals(countries.get(j)) && (!countryCount.contains(countries.get(j)))) {
							countryCount.add(countries.get(j));
						} else {
							continue;
						}
					}
				}
				assertEquals(countryCount.size(),countriesSet.size());
				
				
				
			}
	}
}

	
	


