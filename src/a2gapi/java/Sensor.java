/**
 * @author Alert2Gain
 */

package a2gapi.java;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;

/* Demo object named sensor, with 4 attributes related to weather stations */
public class Sensor
{
    public String name;
    public double temperature;
    public double humidity;
    public Date lastread;
    
    public Sensor(String name, double temperature, double humidity, Date lastread)
    {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.lastread = lastread;
    }
    
    /* Method to create a sensor. */
    static void createSensor()
    {
        /* Here we initialize the sensor with some values. */
        /* Initialize the sensor with your own logic. */
        Sensor sensor = new Sensor("Station 1", 10, 15, new Date());
        
        /* This is to make sure that the sensor has been sent successfully. */
	/* You can use your own logic to do this. */
        boolean done = false;
        while (true)
        {
            done = sendData(sensor);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /* Method to send data to A2G InputStream API */
    static boolean sendData(Sensor sensor)
    {
        /* Here we put the url */
        String url = "https://listen.a2g.io/v1/testing/inputstream";
        
        /* Here we create a Map with the header content */
        Map<String, String> header = new HashMap<>();
        header.put("x-api-key", "[YOUR_API_KEY]");
        
        /* Here we create another Map to put the data of the sensor */
        Map<String, Object> stationData = new HashMap<>();
        Map<String, Object> extraData = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        stationData.put("Station", sensor.name);
        extraData.put("temperature", sensor.temperature);
        extraData.put("humidity", sensor.humidity);
        extraData.put("updated", formatter.format(sensor.lastread));
        stationData.put("data", extraData);
        
        /* Here we create a Gson to parse the stationData */
        Gson gson = new Gson();
        String stationJson = gson.toJson(stationData);
        
        /* And here we add the previous data to the data that we will send to the API */
        Map<String, Object> data = new HashMap<>();
        data.put("IKEY", "[YOUR_INPUTSTREAM_KEY]");
        data.put("Data", stationJson);
        
        /* Here we parse the data again */
        String dataJson = gson.toJson(data);
        
        /* And finally, we send the data to the API. */
        RawResponse response = Requests.post(url).headers(header).body(dataJson).send();
        
        System.out.println("Response: " + response.statusLine() + " - " + response.readToText());
        
        if (response.statusCode() == 200) {
            return true;
        }
        
        return false;
    }

    public static void main(String[] args)
    {
	/* Here we call the method to start the test. */
        createSensor();
    }   
}