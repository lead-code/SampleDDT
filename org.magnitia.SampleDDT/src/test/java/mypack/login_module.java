package mypack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.amazonaws.services.athena.AmazonAthena;
import com.amazonaws.services.athena.AmazonAthenaClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.tcs.awsutils.*;

import com.tcs.awsutils.AWSParamStoreUtil;
import com.tcs.pages.login_page;
import com.tcs.utilities.*;

import com.tcs.reports.TestListener;

public class login_module extends Library {
	public login_page loginPage;
	public TestListener step;
	public WebDriver browser;
	public Library lib;
	public Dynamodb dynamo;
	private static AmazonAthena athenaClient;
	private static AmazonDynamoDB dynamoClient;
	public static AWSParamStoreUtil awsparam = new AWSParamStoreUtil();

	public login_module() throws Exception {
		loginPage = PageFactory.initElements(getDriver(), login_page.class);
		step = new TestListener();
		lib = new Library();
		dynamoClient = AmazonDynamoDBClientBuilder.defaultClient();
		lib = new Library();
		step = new TestListener();
		dynamo = new Dynamodb(dynamoClient);
		athenaClient = AmazonAthenaClientBuilder.defaultClient();
		new Athena(athenaClient);
		awsparam = new AWSParamStoreUtil();
	}

	public void validateLoginWithInvalidCred() throws Exception {
		loginPage.setUserName("testcare@cummins.com.eidmtst");
		loginPage.setPassword("dummypass");
		loginPage.clickSubmit();
		Sleep(3);
		step.assertTrue(loginPage.loginError.isDisplayed(), "Error message shown for invalid login as expected",
				"No error message shown for invalid credentials", true);
		step.assertTrue(loginPage.loginError.getAttribute("textContent").toLowerCase().toString().length() > 1,
				"Error message string is as expected: "
						+ loginPage.loginError.getAttribute("textContent").toLowerCase().toString().trim(),
				"Error message string mismatch", true);
	}

	public void blank_password() throws Exception {
		loginPage.setUserName("testcare@cummins.com.eidmtst");
		loginPage.setPassword("");
		loginPage.clickSubmit();
		Sleep(3);
		step.assertTrue(loginPage.loginError.isDisplayed(),
				"Error message :\n" + loginPage.loginError.getText() + " shown for blank password entry",
				"No error message shown when blank password is entered", true);
	}

	public void blank_userid() throws Exception {
		navigateURL();
		loginPage.setUserName("");
		loginPage.setPassword("TstPass");
		loginPage.clickSubmit();
		Sleep(3);
		step.assertTrue(loginPage.loginError.isDisplayed(),
				"Error message :\n" + loginPage.loginError.getText() + " shown for blank password entry",
				"No error message shown when blank password is entered", true);
	}

	public void validatTheUser(String role, String accId, String type) throws Exception {
		DataAPI dataApi = new DataAPI();
		String query = null;
		if (type.equalsIgnoreCase("customer")) {
			query = "Select users.\"firstName\",users.\"lastName\",users.email,acct.* ,roles.* from \r\n"
					+ "pcc.accounts acct \r\n" + " join pcc.users_accountsubscriptions useracct \r\n"
					+ "on (acct.\"id\" = useracct.\"accountId\")\r\n"
					+ "join pcc.users users on ( users.\"id\" = useracct.\"userId\")\r\n"
					+ "join pcc.users_accountsubscriptions_accountroles on (useracct.\"id\" = \"usersAccountsubscriptionsId\")\r\n"
					+ "join pcc.roles roles on (\"rolesId\" = roles.\"id\")\r\n" + "where \r\n"
					+ "useracct.\"accountId\"='" + accId + "' and \"inheritsId\" is not null";
		}
		System.out.println("query :" + query);
		dataApi
				.retreiveExecResult(dataApi.retreiveExecQuery(dataApi.retreiveExecRequest(), query));

		dynamo.queryTargetDeviceId("", "".substring(1, "".length() - 1));
	}

}
