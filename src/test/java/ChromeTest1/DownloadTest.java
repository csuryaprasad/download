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
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


import org.openqa.selenium.JavascriptExecutor;

public class DownloadTest {

	Logger log = Logger.getLogger("rootLogger");
	
	@Test
	public void downloadExampleTest () {
		     	
      	      	
      	String sourceURL = "https://chromedriver.storage.googleapis.com/index.html?path=94.0.4606.41/";
      	String downloadPath = System.getProperty("user.dir")+"\\src\\test\\resources\\TestData";
      	
       	//Chrome Setup
      	WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        
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
        driver.manage().window().maximize();
        driver.navigate().to(sourceURL);
        
        if(SystemUtils.IS_OS_LINUX){
          	log.info("connected to the Linux ENV");
         	String currentUser=System.getProperty("user.name");
         	downloadPath = System.getProperty("user.dir")+"\\src\\test\\resources\\TestData";
         	log.info(downloadPath);
         	final File folder = new File(downloadPath);
         	log.info("folderName-"+folder.getName());
            log.info(folder.getAbsoluteFile().length());
            log.info("-----------------------------------");
            
            downloadPath = System.getProperty("user.dir");
            log.info(downloadPath);
            log.info("-----------------------------------");
          }
        
        this.downloadFileTest(driver, downloadPath);
  
	}
	
	

	private void downloadFileTest(WebDriver driver,String downloadPath) {
    	final File folder = new File(downloadPath);
     	//final File folder = new File("\\home\\runner\\work\\download\\download\\src\\test\\resources\\TestData");
    	JavascriptExecutor js = (JavascriptExecutor)driver;
         
    	 
     	String currentUser=System.getProperty("user.name");
      	log.info("User Name-"+ currentUser);
        log.info("setting chrome download path:"+downloadPath);
        log.info("folderName-"+folder.getName());
        log.info(folder.getAbsoluteFile().length());
        
        
        try {
       		if(folder.getAbsoluteFile().exists()) {
    			log.info("Download folder exists");
       		}else {
    			log.info("Download folder doesnot exist");	
       		}
    		File[] files = folder.listFiles();
    		int len = files.length;
    		log.info("Folder Lenght "+ len);
    		for ( final File file : files ) {
    			if ( !file.delete() ) {
    				log.error( "Can't remove " + file.getAbsolutePath() );
    			}
    				log.info("File Deleted ");
    		 }	
    		
      		
      		driver.navigate().refresh();
      		Thread.sleep(10000);
    		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    		WebElement link =driver.findElement(By.xpath("//a[@href='/94.0.4606.41/chromedriver_win32.zip']"));
    		
    		js.executeScript("arguments[0].click();", link);
    		
       		//driver.findElement(By.xpath("//a[@href='/94.0.4606.41/chromedriver_win32.zip']")).click(); 
    		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    		
    		Thread.sleep(10000);
      		files = folder.listFiles();
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
			driver.findElement(By.xpath("//a[text()='chromedriver_win32.zip']")).click(); 
		} 
		driver.quit();
    }
}
