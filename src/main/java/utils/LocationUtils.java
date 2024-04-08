package utils;
import java.io.IOException;
import java.net.*;
import java.util.*;

import com.google.gson.*;

import java.io.*;
import jakarta.servlet.http.HttpServletRequest;

public class LocationUtils {
	
	public static Map<String,Double> getGeolocation(HttpServletRequest request) throws IOException {
		Map<String,Double> coords = new HashMap<String,Double>();
		String apiKey = APIUtils.getAPIKey();
		String cityName = request.getParameter("city");
		String countryName = request.getParameter("country");
		String countryCode = getCountryCode(countryName);
		String stateCode = "";
		if(countryCode.equals("US")) {
			String stateName = request.getParameter("state");
			stateCode = getStateCode(stateName);
		}
		
		//generate url for API call
		String geocoderURL = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "," + stateCode + "," + countryCode + "&limit=1&appid=" + apiKey;
		
		//make API call
		URL url = new URL(geocoderURL);
		HttpURLConnection c = (HttpURLConnection) url.openConnection();
		c.setRequestMethod("GET");
		
		//collect data as string
		InputStream i = c.getInputStream();
		String locationString = APIUtils.apiReturn(i);
		
		//Typecast into JSonArray as thats how its returned from API
		Gson gson = new Gson();
		JsonArray locationAsJsonArray = gson.fromJson(locationString, JsonArray.class);
		
		//get lon and lat as map and return
		coords = getCoords(locationAsJsonArray);
		c.disconnect();
		return coords;
		
	}
	
	private static String getCountryCode(String countryName) {

	    //Parse all country codes in an array from Locale
	    String[] countryCodes = Locale.getISOCountries();
	    String countryCode = "";
	    //Iterate through them:
	    for (String s : countryCodes) {
	        //Create a locale using each country code
	        Locale l = new Locale("", s);
	        //Get country name for each code
	        String displayCountry = l.getDisplayCountry();
	        if(displayCountry.equals(countryName))
	          {
	        	//When input country name is found, return country code
	        	countryCode = s;
	            break;
	          }
	    }
	    return countryCode;  
	}
	
	public static String getCountryList() {
		String[] countries = Locale.getISOCountries();
		int length = countries.length;
		String[] allCountriesArray = new String[length];
		String allCountries="";
		int i=0;
		for (String s : countries) {
			Locale l = new Locale("", s);
			allCountriesArray[i] = l.getDisplayCountry();
			i++;
		}
		Arrays.sort(allCountriesArray);
		int j=0;
		for (String s : allCountriesArray) {
			if(j==0) {
				allCountries = s;
			} else {
				allCountries = allCountries + "&&" + s;
			} j++;
		}
		
		return allCountries;
	}
	
	private static String getStateCode(String stateName) {
		
		Map<String,String> states = getStates();
		String stateCode = "";
		for(Map.Entry<String,String> entry : states.entrySet()) {
			if(entry.getKey().equals(stateName)) {
				stateCode = entry.getValue();
				break;
			}
		}
		return stateCode;
	
	}
	
	
	private static Map<String,Double> getCoords(JsonArray j) {
		Map<String,Double> coords = new HashMap<String,Double>();
		
		if(j.size() == 0) {
			return coords;
		}
		
		JsonObject city = (JsonObject) j.get(0);
		Double longitude = city.get("lon").getAsDouble();
		Double latitude = city.get("lat").getAsDouble();
		
		
		coords.put("lon",longitude);
		coords.put("lat", latitude);
		
		return coords;
	}
	
	public static String getStateList() {
		Map<String,String> states = getStates();
		String allStates = "";
		int size = states.size();
		String[] allStatesArray = new String[size];
		int i = 0;
		for(Map.Entry<String,String> entry : states.entrySet()) {
			allStatesArray[i] = entry.getKey();
			i++;
		}
		Arrays.sort(allStatesArray);
		int j=0;
		for (String s : allStatesArray) {
			if(j==0) {
				allStates = s;
			} else {
				allStates = allStates + "&&" + s;
			}j++; 
		}
		
		return allStates;
	}
	
	
	
	//hardcoded function to generate statecodes of USA - no library available
	private static Map<String,String> getStates(){
		Map<String, String> states = new HashMap<String, String>();
		states.put("Alabama","AL");
		states.put("Alaska","AK");
		states.put("Arizona","AZ");
		states.put("Arkansas","AR");
		states.put("California","CA");
		states.put("Colorado","CO");
		states.put("Connecticut","CT");
		states.put("Delaware","DE");
		states.put("District Of Columbia","DC");
		states.put("Florida","FL");
		states.put("Georgia","GA");
		states.put("Hawaii","HI");
		states.put("Idaho","ID");
		states.put("Illinois","IL");
		states.put("Indiana","IN");
		states.put("Iowa","IA");
		states.put("Kansas","KS");
		states.put("Kentucky","KY");
		states.put("Louisiana","LA");
		states.put("Maine","ME");
		states.put("Maryland","MD");
		states.put("Massachusetts","MA");
		states.put("Michigan","MI");
		states.put("Minnesota","MN");
		states.put("Mississippi","MS");
		states.put("Missouri","MO");
		states.put("Montana","MT");
		states.put("Nebraska","NE");
		states.put("Nevada","NV");
		states.put("New Hampshire","NH");
		states.put("New Jersey","NJ");
		states.put("New Mexico","NM");
		states.put("New York","NY");
		states.put("North Carolina","NC");
		states.put("North Dakota","ND");
		states.put("Ohio","OH");
		states.put("Oklahoma","OK");
		states.put("Oregon","OR");
		states.put("Pennsylvania","PA");
		states.put("Rhode Island","RI");
		states.put("South Carolina","SC");
		states.put("South Dakota","SD");
		states.put("Tennessee","TN");
		states.put("Texas","TX");
		states.put("Utah","UT");
		states.put("Vermont","VT");
		states.put("Washington","WA");
		states.put("West Virginia","WV");
		states.put("Wisconsin","WI");
		states.put("Wyoming","WY");
		
		return states;
	}

}
