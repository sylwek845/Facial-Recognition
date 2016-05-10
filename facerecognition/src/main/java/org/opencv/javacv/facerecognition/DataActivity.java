package org.opencv.javacv.facerecognition;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sylwester Zalewski on 15/12/2015.
 */
public class DataActivity extends Activity {
    /**
     * private double coordLon;
     private double coordLat;
     private String weatherID;
     private String weatherMain;
     private String weatherDesc;
     private double mainTemp;
     private String windSpeed;
     private String clouds;
     private String rain3h;
     private String snow3h;
     private String sysSunrise;
     private String sysSunset;
     private String ID;
     private String City;
     private String Icon;
     private Bitmap image;
     */

    private TextView text_city;
    private TextView text_main;
    private TextView text_desc;
    private TextView text_main_temp;
    private TextView text_windSpeed;
    private TextView text_clouds;
    private TextView text_rain;
    private TextView text_snow;
    private TextView text_sunrise;
    private TextView text_sunset;
    private TextView msg;
    private Button yesbut;
    private Button nobut;
    private ImageView image_icon;
    private boolean celsius = true;
    private boolean mph =true;
    private DB mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_data);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getCity());
        text_city =  (TextView) findViewById(R.id.city_name);
        text_desc = (TextView) findViewById(R.id.city_desc);
        text_main_temp = (TextView) findViewById(R.id.city_temp);
        text_windSpeed  = (TextView) findViewById(R.id.city_wind);
        text_clouds = (TextView) findViewById(R.id.city_clouds);
        text_rain = (TextView) findViewById(R.id.city_rain);
        text_snow  = (TextView) findViewById(R.id.city_snow);
        text_sunrise  = (TextView) findViewById(R.id.city_sunrise);
        text_sunset = (TextView) findViewById(R.id.city_sunset);
        image_icon  = (ImageView) findViewById(R.id.city_icon);
        msg= (TextView) findViewById(R.id.msg);
        yesbut =  (Button) findViewById(R.id.yesbut);
        nobut =  (Button) findViewById(R.id.nobut);
       // Toast.makeText(getApplicationContext(),"S= "+ String.valueOf(DataExchanger.getSuc())+ ", U= " + String.valueOf(DataExchanger.getUnsuc()),Toast.LENGTH_LONG);
        mydb = new DB(getApplicationContext()); // create new instance of DB
        try {
            if (databaseExist(this, "WeatherDB.db"))
               getStats();
        }
        catch (Exception e){}
        celsius = DataExchanger.isCelsius();
        mph = DataExchanger.isMph();
        addValues(); // call add Vales

        yesbut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataExchanger.setSuc(DataExchanger.getSuc() + 1);
                msg.setVisibility(View.INVISIBLE);
                yesbut.setVisibility(View.INVISIBLE);
                nobut.setVisibility(View.INVISIBLE);
                AddDBStarts();
            }
        });

        nobut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataExchanger.setUnsuc(DataExchanger.getUnsuc() + 1);
                msg.setVisibility(View.INVISIBLE);
                yesbut.setVisibility(View.INVISIBLE);
                nobut.setVisibility(View.INVISIBLE);
                AddDBStarts();
            }
        });
    }
    public void getStats()
    {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mydb.getweather(999);
        if(today.monthDay != DataExchanger.getDay())
        {
            DataExchanger.setUnsuc(0);
            DataExchanger.setSuc(0);
        }

    }
    private static boolean databaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    public void addValues() {
        msg.setText("Hi " + DataExchanger.getName()+ " is that you??");
        if (DataExchanger.getWeatherDatas() != null) {
            if (celsius)
                text_main_temp.setText("Temperature: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getMainTempString() + "°C"); // set temperature
            else {
                //convert to F and set
                double temp = DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getMainTemp();
                temp = temp * 9 / 5 + 32;
                text_main_temp.setText("Temperature: " + String.valueOf(Math.round(temp)) + "°F");
            }
            text_city.setText(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getCity());
            text_desc.setText("Weather Description: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getWeatherDesc());
            if (mph)
                text_windSpeed.setText("Wind: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getWindSpeed() + " mph");
            else {
                //convert to kph and set
                double wind = Double.parseDouble(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getWindSpeed());
                wind = Math.round(wind * 1.609344);//
                text_windSpeed.setText("Wind: " + String.valueOf(wind) + " kph");
            }
            text_clouds.setText("Clouds: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getClouds() + "%");
            try {
                if (DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getRain3h().isEmpty())
                    text_rain.setText("Rain: N/A");
                else
                    text_rain.setText("Rain: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getRain3h() + " mm");
            } catch (Exception e) {
                text_rain.setText("Rain: N/A");
            }
            try {
                if (DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getSnow3h().isEmpty())
                    text_snow.setText("Snow: N/A");

                else
                    text_snow.setText("Snow: " + DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getSnow3h() + " mm");
            } catch (Exception e) {
                text_snow.setText("Snow: N/A");
            }
            /**
             * This converts Unix time To UTC
             */
            long unixTimestamp = Long.parseLong(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getSysSunrise());
            long javaTimestamp = unixTimestamp * 1000L;
            Date date = new Date(javaTimestamp);
            String sunrise = new SimpleDateFormat("hh:mm").format(date);
            unixTimestamp = Long.parseLong(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getSysSunset());
            javaTimestamp = unixTimestamp * 1000L;
            date = new Date(javaTimestamp);
            String sunset = new SimpleDateFormat("hh:mm").format(date);
            text_sunrise.setText("Sunrise: " + sunrise); // set sunrise
            text_sunset.setText("Sunset: " + sunset);//set sunset
            //set Image
            image_icon.setImageBitmap(DataExchanger.getWeatherDatas()[DataExchanger.getElement()].getImage());
        }
        else
        {
            Toast.makeText(this,"Cannot Download weather Data, Sorry",Toast.LENGTH_SHORT);
        }
    }
    private void getLatLonFromGoogleAddDB(String weatherData)
    {

        try{
            WeatherData weatherDataTemp = new WeatherData(); //create Temp object
            Geocoder geocoder = new Geocoder(this, Locale.getDefault()); //get local cities (e.g for glasgow it will get Glasgow lat long from local UK position, not for example US glasgow)
            List<Address> addresses; //list of addresses
            addresses = geocoder.getFromLocationName(weatherData, 1);
            if (addresses.size() > 0) { //only first one if any exist
                weatherDataTemp.setCoordLat(addresses.get(0).getLatitude());
                weatherDataTemp.setCoordLon(addresses.get(0).getLongitude());
                weatherDataTemp.setCity(weatherData);
                mydb.addWeather(weatherDataTemp);// add to database
            }
        }
        catch (Exception e) {}
    }

    private void AddDBStarts()
    {


        try{
                String suc = String.valueOf(DataExchanger.getSuc());
                String unsuc = String.valueOf(DataExchanger.getUnsuc());
              //  mydb = new DB(this);
                mydb.dropDB();
                mydb = new DB(this);
                mydb.reCreateTable(mydb.getReadableDatabase());
                mydb.insertdata("Glasgow"," "," ",suc,unsuc);

        }
        catch (Exception e) {}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_stats) {
            if(databaseExist(this, "WeatherDB.db"))
            getStats();
            msg.setText("S= "+ String.valueOf(DataExchanger.getSuc())+ ", U= " + String.valueOf(DataExchanger.getUnsuc()));
            msg.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }
    public void InputDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter City Name");
        builder.setMessage("App needs to be restarted before any changes appear. Sorry ;(");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String city = input.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (!city.isEmpty()) {
                        getLatLonFromGoogleAddDB(city);
                        Toast.makeText(getApplicationContext(), "City Added to Database", Toast.LENGTH_SHORT).show(); //test
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
