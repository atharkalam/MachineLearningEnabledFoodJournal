
package com.fitbit;

import com.fitbit.model.Activity;
import com.fitbit.model.*;
import com.fitbit.model.DailyDataActivity;
import com.fitbit.model.DailySleep;
import com.fitbit.model.LifetimeActivity;
import com.fitbit.model.Sleep;
//import com.fitbit.model.Sleep2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
@EnableWebSecurity
public class FitbitOAuthExample extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2RestTemplate fitbitOAuthRestTemplate;
	
	@Value("${fitbit.api.resource.activitiesUri}")
	String fitbitActivitiesUri;
	
	@Value("${fitbit.api.resource.dailySummaryUri}")
	String fitbitDailySummaryUri;
	
	@Value("${fitbit.api.resource.dailySleepUri}")
	String fitbitDailySleepUri;
	
	@Value("${fitbit.api.resource.dateRangeSleepUri}")
	String fitbitDateRangeSleepUri;

	@RequestMapping("/lifetime-activity")
	public LifetimeActivity lifetimeActivity() {
		LifetimeActivity lifetimeActivity;

		try {
			Activity a = fitbitOAuthRestTemplate.getForObject(fitbitActivitiesUri, Activity.class);
			lifetimeActivity = a.getLifetime().getTotal();
			}
		catch(Exception e) {
			lifetimeActivity = new LifetimeActivity();
		}

		return lifetimeActivity;
	}
	
	/*@RequestMapping("/testSleep")
	public Sleep2 checkRest()
	{	
		Sleep2 sl = new Sleep2();
		return sl;
		
	}*/
	
	@RequestMapping("/date-range-sleep")
	public DailySleep dateRangeSleep(){
		DailySleep dateRangeSleep;
		try {
			Sleep d = fitbitOAuthRestTemplate.getForObject(fitbitDailySleepUri, Sleep.class);
			dateRangeSleep = d.getSummary();
			}
		catch(Exception e) {
			dateRangeSleep = new DailySleep();
			dateRangeSleep.setTotalMinutesAsleep(999);
			e.printStackTrace();
		}
		return dateRangeSleep;
	}
	
	@RequestMapping("/daily-summary")
	public DailyActivities dailyActivities(){
		DailyActivities dailyActivities;
		try {
			Activity a = fitbitOAuthRestTemplate.getForObject(fitbitDailySummaryUri, Activity.class);
			dailyActivities = a.getSummary();
			}
		catch(Exception e) {
			dailyActivities = new DailyActivities();
		}
		return dailyActivities;
	}
	
	@RequestMapping("/daily-sleep")
	public DailySleep dailySleep(){
		DailySleep dailySleep;
		try {
			Sleep s = fitbitOAuthRestTemplate.getForObject(fitbitDailySleepUri, Sleep.class);
			dailySleep = s.getSummary();
			
			/*String sl  = s.getSleep();
			
			System.out.println(sl);*/
			
			}
		catch(Exception e) {
			dailySleep = new DailySleep();
			dailySleep.setTotalMinutesAsleep(999);
			e.printStackTrace();
		}
		return dailySleep;
	}
	
	@RequestMapping("/daily-data")
	public DailyDataActivity dailyData(){
		DailySleep dailySleep;
//		LifetimeActivity lifetimeActivity;
		DailyDataActivity dailyDataActivity = new DailyDataActivity();
		try {
			
			Date date = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//			https://api.fitbit.com/1.2/user/-/sleep/date/2017-05-02.json
//			https://api.fitbit.com/1.2/user/-/sleep/date/2017-05-03.json
			
			Date utilDate = cal.getTime();
			Date sqlStartDate = new Date(utilDate.getTime());
			System.out.println("sqlStartDate-----"+sqlStartDate);
			String reqDate= format1.format(cal.getTime());
			String formatted = "https://api.fitbit.com/1.2/user/-/sleep/date/" 
					+ reqDate + ".json";
			//2017-05-03
			
			Sleep s = fitbitOAuthRestTemplate.getForObject(formatted, Sleep.class);
			dailySleep = s.getSummary();
			Activity a = fitbitOAuthRestTemplate.getForObject(fitbitDailySummaryUri, Activity.class);
//			lifetimeActivity = a.getLifetime().getTotal();
			DailyActivities dailyActivities= a.getSummary();
			dailyDataActivity.setDate(reqDate);
			dailyDataActivity.setCaloriesOut(dailyActivities.getCaloriesOut());
			dailyDataActivity.setTotalMinutesAsleep(dailySleep.getTotalMinutesAsleep());
			
			String UserID=null;
			String CalorieIntake=null;
			String CalorieBurnt=String.valueOf((dailyDataActivity.getCaloriesOut()));
			String SleepHours=String.valueOf(dailyDataActivity.getTotalMinutesAsleep());
			String ActivityDate=dailyDataActivity.getDate();
			
			System.out.println("----MySQL JDBC Connection Testing -------");
		    String JDBC_MYSQL="jdbc:mysql://";
		    String PUBLIC_DNS="athar.cj4c4eeohacg.us-west-1.rds.amazonaws.com";
		    String PORT="3306";
		    String DATABASE_NAME="athar_fitbit";
		    String REMOTE_DATABASE_USERNAME="athar_sjsu";
		    String DATABASE_USER_PASSWORD="athar_123";
		    try {
		        Class.forName("com.mysql.jdbc.Driver");
		    } catch (ClassNotFoundException e) {
		        System.out.println("Where is your MySQL JDBC Driver?");
		        e.printStackTrace();
		       
		    }

		    System.out.println("MySQL JDBC Driver Registered!");
		    Connection connection = null;
		    try {
		    	connection = DriverManager.
		                getConnection(JDBC_MYSQL + PUBLIC_DNS + ":" + PORT + "/" + DATABASE_NAME,REMOTE_DATABASE_USERNAME,DATABASE_USER_PASSWORD);
		    	Statement statement=connection.createStatement();
		    	//DateFormat formatter= new SimpleDateFormat("yyyy-mm-dd");
		    	//Date convertedDate=(Date)formatter.parse(ActivityDate);
		    	
		    	//Date ActivityDateConv=new SimpleDateFormat("yyyy-mm-dd").parse(ActivityDate);
		    	//System.out.println(ActivityDateConv);
		    	String InsertQuery="INSERT INTO DailyUserActivity(`UserId`,`CalorieIntake`,`CalorieBurnt`,`Date`,`SleepHours`)VALUES("+UserID+","+CalorieIntake+","+CalorieBurnt+","+sqlStartDate+","+SleepHours+");";
		    	statement.executeUpdate(InsertQuery);	    
		    } catch (SQLException e) {
		        System.out.println("Connection Failed!:\n" + e.getMessage());
		    }

		    if (connection != null) {
		        System.out.println("SUCCESS!!!! You made it, take control     your database now!");
		    } else {
		       System.out.println("FAILURE! Failed to make connection!");
		    }
		    try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


				
			
			
			//connectJDBCToAWSEC2(null, null, String.valueOf((dailyDataActivity.getCaloriesOut())), String.valueOf(dailyDataActivity.getTotalMinutesAsleep()),dailyDataActivity.getDate());
			}
		catch(Exception e) {
			dailySleep = new DailySleep();
			dailySleep.setTotalMinutesAsleep(999);
			e.printStackTrace();
		}
		return dailyDataActivity;
	}
	
	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
				.authenticated();
	}
	
/*	public void connectJDBCToAWSEC2(String UserID,String CalorieIntake,String CalorieBurnt,String SleepHours,String Date) {
			
		    System.out.println("----MySQL JDBC Connection Testing -------");
		    String JDBC_MYSQL="jdbc:mysql://";
		    String PUBLIC_DNS="athar.cj4c4eeohacg.us-west-1.rds.amazonaws.com";
		    String PORT="3306";
		    String DATABASE_NAME="athar_fitbit";
		    String REMOTE_DATABASE_USERNAME="athar_sjsu";
		    String DATABASE_USER_PASSWORD="athar_123";
		    try {
		        Class.forName("com.mysql.jdbc.Driver");
		    } catch (ClassNotFoundException e) {
		        System.out.println("Where is your MySQL JDBC Driver?");
		        e.printStackTrace();
		        return;
		    }

		    System.out.println("MySQL JDBC Driver Registered!");
		    Connection connection = null;
		    try {
		    	connection = DriverManager.
		                getConnection(JDBC_MYSQL + PUBLIC_DNS + ":" + PORT + "/" + DATABASE_NAME,REMOTE_DATABASE_USERNAME,DATABASE_USER_PASSWORD);
		    	Statement statement=connection.createStatement();
		    	String InsertQuery="INSERT INTO Activity(`UserId`,`CalorieIntake`,`CalorieBurnt`,`SleepHours`,`Date`)VALUES("+UserID+","+CalorieIntake+","+CalorieBurnt+","+SleepHours+","+Date+");";
		    	statement.executeUpdate(InsertQuery);	    
		    } catch (SQLException e) {
		        System.out.println("Connection Failed!:\n" + e.getMessage());
		    }

		    if (connection != null) {
		        System.out.println("SUCCESS!!!! You made it, take control     your database now!");
		    } else {
		       System.out.println("FAILURE! Failed to make connection!");
		    }
		    try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	
	public static void main(String[] args) {
		SpringApplication.run(FitbitOAuthExample.class, args);
	}

}
