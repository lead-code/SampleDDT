package mypack;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.tcs.utilities.Library;

public class login_page extends Library {
//updated
	@FindBy(xpath = "//input[contains(text(),'signInName')]")
	public WebElement web_username;

	@FindBy(id = "password")
	public WebElement web_password;

	@FindBy(id = "next")
	public WebElement web_submit;

	@FindBy(xpath = "//span[contains(text(),'Sign In')]")
	public WebElement normal_signin;

	@FindBy(id = "j_id0:commentForm:error_msg_box")
	public WebElement loginError;

	@FindBy(xpath = "//input[@id='username']")
	public WebElement cola_userid;

	@FindBy(xpath = "//button[@id='valuser']")
	public WebElement submit_usr;

	@FindBy(xpath = "//input[@id='password']")
	public WebElement cola_password;

	@FindBy(xpath = "//button[@id='submitbtn']")
	public WebElement cola_Submit;

	Library lib = new Library();

	public boolean verify_login_error() throws Exception {
		WebElement error = getByAttributes("span", "id@j_id\\d*:commentForm:error_msg_box");

		if (error != null) {
			return true;
		}
		return false;
	}

	public void setUserName(String username) throws Exception {
		lib.getByAttributes("input", "id@signInName").sendKeys(username);
	}

	public void setPassword(String password) throws Exception {
		web_password.click();
		web_password.sendKeys(password);
	}

	public void clickSubmit() throws Exception {
		web_submit.click();
		WaitUntilPageLoad();
	}

}
