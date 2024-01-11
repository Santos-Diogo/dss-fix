package uminho.dss.esideal.ui.Parse;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Parse 
{
    public static Time parseInputTime(String inputTime) throws SQLException 
    {
        // Define the expected time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            // Parse the input time string to LocalTime
            LocalTime localTime = LocalTime.parse(inputTime, formatter);

            // Convert LocalTime to java.sql.Time
            return Time.valueOf(localTime);

        } catch (Exception e) {
            // Handle parsing exceptions or rethrow as SQLException
            e.printStackTrace();
            throw new SQLException("Error parsing input time: " + e.getMessage());
        }
    }    
}
