package mypack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test116
{
	public static void main(String[] args) throws Exception
	{
		//get browser name and test data from Excel file sheet
		File f=new File("Book1.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows(); //count of used rows
		int nouc=sh.getRow(0).getLastCellNum(); //count of used columns
		//Create result column with current date and time as heading
		SimpleDateFormat sf=new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
		Date dt=new Date();
		Cell rc=sh.getRow(0).createCell(nouc);
		rc.setCellValue(sf.format(dt));
		sh.autoSizeColumn(nouc); //auto fit on column size
		//Define wrap text for remaining cells
		CellStyle cs=wb.createCellStyle();
		cs.setWrapText(true);
		//Create object to Utility class
		TestUtility obj=new TestUtility();
		//Login functional testing with multiple test data in cross browser
		//loop from 2nd row(index=1) in excel file due to 1st row has names of columns
		for(int i=1;i<nour;i++)
		{
			try
			{
				String bn=sh.getRow(i).getCell(0).getStringCellValue();
				String uid;
				try
				{
					uid=sh.getRow(i).getCell(1).getStringCellValue();
				}
				catch(NullPointerException ne1)
				{
					uid="";
				}
				String uidc=sh.getRow(i).getCell(2).getStringCellValue();
				String pwd;
				try
				{
					pwd=sh.getRow(i).getCell(3).getStringCellValue();
				}
				catch(NullPointerException ne2)
				{
					pwd="";
				}
				String pwdc=sh.getRow(i).getCell(4).getStringCellValue();
				//Open browser
				RemoteWebDriver driver=obj.openBrowser(bn);
				obj.launchSite("http://www.gmail.com");
				WebDriverWait w=new WebDriverWait(driver,20);
				w.until(ExpectedConditions.visibilityOfElementLocated(By.name("identifier")));
				driver.findElement(By.name("identifier")).sendKeys(uid); //parameterization
				driver.findElement(By.xpath("//*[text()='Next']")).click();
				if(uid.length()==0)
				{
					try
					{
						w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
								             "//div[contains(text(),'Enter an email')]")));
						Cell c=sh.getRow(i).createCell(nouc);
						c.setCellStyle(cs);
						c.setCellValue("Uid blank test passed");
					}
					catch(Exception ex1)
					{
						Cell c=sh.getRow(i).createCell(nouc);
						c.setCellStyle(cs);
						c.setCellValue("Uid blank test failed and see "+obj.screeshot());
					}
				}
				else if(uid.length()!=0 && uidc.equalsIgnoreCase("invalid"))
				{
					try
					{
						w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
								"//div[contains(text(),'find your Google Account') or contains(text(),'Enter a valid email')]")));
						Cell c=sh.getRow(i).createCell(nouc);
						c.setCellStyle(cs);
						c.setCellValue("Invalid UID test passed");
					}
					catch(Exception ex2)
					{
						Cell c=sh.getRow(i).createCell(nouc);
						c.setCellStyle(cs);
						c.setCellValue("Invalid Uid test failed and see "+obj.screeshot());
					}
				}
				else  //UId is valid
				{
					try
					{
						w.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
						//Password Testing
						driver.findElement(By.name("password")).sendKeys(pwd); //parameterization
						driver.findElement(By.xpath("//*[text()='Next']")).click();
						if(pwd.length()==0)
						{
							try
							{
								w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
										                            "//*[text()='Enter a password']")));
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("PWD blank test passed");
							}
							catch(Exception ex1)
							{
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("PWD blank test failed and see "+obj.screeshot());
							}
						}
						else if(pwd.length()!=0 && pwdc.equalsIgnoreCase("invalid"))
						{
							try
							{
								w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
										"//*[contains(text(),'Wrong password') or contains(text(),'Your password was changed')]")));
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("Invalid PWD test passed");
							}
							catch(Exception ex2)
							{
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("Invalid PWD test failed and see "+obj.screeshot());
							}	
						}
						else //PWD is valid
						{
							try
							{
								w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
										                                 "//*[text()='Compose']")));
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("Valid PWD test passed");
							}
							catch(Exception ex3)
							{
								Cell c=sh.getRow(i).createCell(nouc);
								c.setCellStyle(cs);
								c.setCellValue("Valid PWD test failed and see "+obj.screeshot());
							}
						}
					}
					catch(Exception ex3)
					{
						Cell c=sh.getRow(i).createCell(nouc);
						c.setCellStyle(cs);
						c.setCellValue("Valid Uid test failed and see "+obj.screeshot());
					}
				}
				//close site
				obj.closeSite();
			}
			catch(Exception ex)
			{
				Cell c=sh.getRow(i).createCell(nouc);
				c.setCellStyle(cs);
				c.setCellValue(ex.getMessage());
			}
		} //loop ending
		//Save and close excel file
		FileOutputStream fo=new FileOutputStream(f);
		wb.write(fo); //save
		wb.close();
		fo.close();
		fi.close();
	}
}








