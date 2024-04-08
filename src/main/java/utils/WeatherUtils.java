package utils;
import Model.Weather;
import java.net.*;
import java.util.*;

import com.google.gson.*;

import java.io.*;

public class WeatherUtils {
	
	public static String getWeather(Map<String,Double> coords) throws IOException {
		
		String apiKey = APIUtils.getAPIKey();
		String lon = coords.get("lon").toString();
		String lat = coords.get("lat").toString();
		
		String weatherURL = "https://api.openweathermap.org/data/2.5/weather?lat="+ lat +"&lon=" + lon + "&appid=" + apiKey;
		
		//make API call
		URL url = new URL(weatherURL);
		HttpURLConnection c = (HttpURLConnection) url.openConnection();
		c.setRequestMethod("GET");
		
		//collect data as string
		InputStream i = c.getInputStream();
		String weatherString = APIUtils.apiReturn(i);
		
		//Typecast into JSonObject as thats how its returned from API
		Gson gson = new Gson();
		JsonObject weatherAsJsonObject = gson.fromJson(weatherString, JsonObject.class);
		
		//generate weather requirements for front end
		Weather currentWeather = generateWeather(weatherAsJsonObject);
		
		//serialize to Json and return
		String json = serializeJson(currentWeather);
		c.disconnect();
	
		return json;
	}
	
	private static Weather generateWeather(JsonObject j) {
		
		JsonArray weatherArray = (JsonArray) j.get("weather");
		JsonObject weatherObject = (JsonObject) weatherArray.get(0);
		JsonObject mainWeather = (JsonObject) j.get("main");
		JsonObject wind = (JsonObject) j.get("wind");
		
		String weatherMain = weatherObject.get("main").getAsString();
		String weatherDescription = weatherObject.get("description").getAsString();
		double temperature = mainWeather.get("temp").getAsDouble();
		int humidity = mainWeather.get("humidity").getAsInt();
		double windSpeed = wind.get("speed").getAsDouble();
		
		Weather currentWeather = new Weather(weatherMain, weatherDescription, temperature, humidity, windSpeed);
		return currentWeather;
	}
	
	public static <T> String serializeJson(T t) {
		Gson gson = new Gson();
		String json = gson.toJson(t);
		return json;
	}

}
