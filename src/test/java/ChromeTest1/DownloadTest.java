package ChromeTest1;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import com.google.common.io.PatternFilenameFilter;

import io.github.bonigarcia.wdm.WebDriverManager;



public class DownloadTest {

	Logger log = Logger.getLogger("rootLogger");
	
	@Test
	public void downloadExampleTest () {
		     	
      	      	
      	String sourceURL = "https://file-examples.com/index.php/text-files-and-archives-download/";
      	String downloadPath = System.getProperty("user.dir")+"\\src\\test\\resources\\TestData";
      	
      	
      	
      	//Chrome Setup
      	WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        options.addArguments("test-type");
        
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        // Setting the file download location for Chrome
       

        chromePrefs.put("download.default_directory", downloadPath);
        options.setExperimentalOption("prefs", chromePrefs);

        WebDriver driver = new ChromeDriver(options); 
        Dimension dim=new Dimension(1382, 744);
        driver.manage().window().setSize(dim);
        driver.navigate().to(sourceURL);
        
        if(SystemUtils.IS_OS_LINUX){
        	options.addArguments("--headless");
        	// this options is for linux environment on docker
         	log.info("connected to the Linux ENV");
         	String currentUser=System.getProperty("user.name");
          	log.info("User Name-"+ currentUser);
         }
        
        this.downloadFileTest(driver, downloadPath);
  
	}
	
	

	private void downloadFileTest(WebDriver driver,String downloadPath) {
    	final File folder = new File(downloadPath);
     	
    	 
         
    	 
     	String currentUser=System.getProperty("user.name");
      	log.info("User Name-"+ currentUser);
        log.info("setting chrome download path:"+downloadPath);
        
        try {
       		if(folder.exists()) {
    			log.info("Download folder exists");
       		}else {
    			log.info("Download folder doesnot exist");	
       		}
    		File[] files = folder.listFiles(new PatternFilenameFilter("file_example_CSV_.*\\.csv"));
    		int len = files.length;
    		log.info("Folder Lenght "+ len);
    		for ( final File file : files ) {
    			if ( !file.delete() ) {
    				log.error( "Can't remove " + file.getAbsolutePath() );
    			}
    				log.info("File Deleted ");
    		 }	
    		//span[text()='Close']
    		driver.findElement(By.xpath("(//td[@class='text-right file-link'])[1]")).click(); 
    		driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    		Thread.sleep(500);
    		Robot r = new Robot();
    		r.keyPress(KeyEvent.VK_ESCAPE);
    		r.keyRelease(KeyEvent.VK_ESCAPE);
    		driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    		
    		Actions action = new Actions(driver);
    		action.sendKeys(Keys.ESCAPE).build().perform();
    		driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    		Thread.sleep(1500);
    		
    		driver.findElement(By.id("aswift_4")).sendKeys(Keys.ESCAPE);
    		driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    		
      		files = folder.listFiles(new PatternFilenameFilter("file_example_CSV_.*\\.csv"));
       		if (files.length > 0) {
       			log.info("File Downloaded Successfully!!!!!!!!!!");
    		}else{
    			log.info("File not downloaded");
 			}
		
    		//This method makes sure that file is not corrupt and it is in a readable form
    		if(files[0].canRead())
    			log.info("File Readable");
    		else
				{log.info("File is unreadable"); 
			}
		} // end of try block
		catch (Throwable e)	{
			log.error("Exception - Location doesnot exist");
		} 
		driver.quit();
    }
}
