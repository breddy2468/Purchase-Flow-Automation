package Demo;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import base.BaseTest;
import pages.Home;

public class DemoTest extends BaseTest {
	
	private Home home;
  

    @Test
    public void VerifyDemoTest() throws Exception {
    	test.get().log(Status.INFO, "VerifyDemoTest");
        home = new Home(driver);
        home.selectBackproduct();
        home.enterPaymentDetails();
        home.verifyOrderSuccess();
        test.get().log(Status.PASS, "VerifyDemoTest successfully Verified");
    }
}

