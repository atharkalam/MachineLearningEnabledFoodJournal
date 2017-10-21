/*
 * 
 * Author=Athar
 */

package Conversion;

import java.sql.Statement;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DbToCsvConversion 
{
public static void main(String args[])
{	
	System.out.println("----MySQL JDBC Connection Testing -------");
    String JDBC_MYSQL="jdbc:mysql://";
    String PUBLIC_DNS="athar.cj4c4eeohacg.us-west-1.rds.amazonaws.com";
    String PORT="3306";
    String DATABASE_NAME="athar_fitbit";
    String REMOTE_DATABASE_USERNAME="athar_sjsu";
    String DATABASE_USER_PASSWORD="athar_123";
	
        String filename ="DailyActivity.csv";
        try {
            FileWriter fileWriter = new FileWriter(filename);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            Connection connection=null;        
            connection= DriverManager.getConnection(JDBC_MYSQL + PUBLIC_DNS + ":" + PORT + "/" + DATABASE_NAME,REMOTE_DATABASE_USERNAME,DATABASE_USER_PASSWORD);
            String query = "select * from DailyUserActivity";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            	fileWriter.append(resultSet.getString(1));
            	fileWriter.append(',');
            	fileWriter.append(resultSet.getString(2));
            	fileWriter.append(',');
            	fileWriter.append(resultSet.getString(3));
            	fileWriter.append(',');
            	fileWriter.append(resultSet.getString(4));
            	fileWriter.append(',');
            	fileWriter.append(resultSet.getString(5));
            	fileWriter.append(',');
            	fileWriter.append(resultSet.getString(6));
            	fileWriter.append('\n');
               }
            fileWriter.flush();
            fileWriter.close();
            connection.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

