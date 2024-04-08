package Model;

public class Weather {
	
	String weatherMain;
	String weatherDescription;
	double temperature;
	int humidity;
	double windSpeed;
	
	
	public Weather(String weatherMain, String weatherDescription, double temperature, int humidity, double windSpeed) {

		super();
		this.weatherMain = weatherMain;
		this.weatherDescription = weatherDescription;
		//api returns as Kelvin so convert to centigrade
		this.temperature = temperature - 273.15;
		this.humidity = humidity;
		//api returns as meters per second so convert to mph
		this.windSpeed = windSpeed * 2.23694;
	}
	

}
