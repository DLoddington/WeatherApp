$(function(){
	
	$(window).on('load',function(){
		generateCountryList();
		generateStateList();
	});
	
	$('#search-button').on('click', function(){
		searchCity();
	});
	
});

function cityError(){
	$('#city-name').addClass('error');
}

function countryError(){
	$('#country-name').addClass('error');
}

function stateError(){
	$('#state-name').addClass('error');
}


//handle errors later
function searchCity(){
	$('#city-name').removeClass('error');
	$('#country-name').removeClass('error');
	$('#state-name').removeClass('error');
	
	var queryString = getQueryString();
	
	if(queryString === "error1"){
		cityError();
	} else if(queryString === "error2"){
		countryError();
	} else if(queryString === "error3"){
		stateError();
	} else{
		$.ajax({
			type: 'GET',
			url: "clientapi?" + queryString,
			success: function(data){
				if(data === "city search error"){
					parseCitySearchErrorToScreen()
				} else {
					getWeatherFromJson(data);
				}
				
			}
		});
	}
}

function getQueryString(){
	var cityName = escape($('#city-name').val());
	var stateName = $('#state-name').val();
	var countryName = $('#country-name').val();

	let queryString;
	
	if(!cityName){
		queryString = "error1"
	} else if (!countryName) {
		queryString = "error2"
	} else if ((countryName === "United States") && (!stateName)){
		queryString = "error3"
	} else {
		queryString = "city=" + cityName +"&country=" + countryName + "&state=" + stateName;
	}
	
	return queryString; 
}

function generateCountryList(){
	
	$.ajax({
		type:'GET',
		url: "countrylist?",
		success: function(data){
			const countryArray = getListAsArray(data);
			generateCountrySelects(countryArray);		
		}
	})
}

function generateStateList(){
	
	$.ajax({
		type:'GET',
		url: "statelist?",
		success: function(data){
			const stateArray = getListAsArray(data);
			generateStateSelects(stateArray);
		}
	})
}

function getListAsArray(data){
	const myArray = data.split("&&");
	return myArray;
}

function generateCountrySelects(array){
	
	$('#country-name').empty();
	$('#country-name').append('<option disabled selected>Select a country</option>');
	for(i=0;i<array.length;i++){
		$('#country-name').append('<option>'+array[i]+'</option')
	}
}

function generateStateSelects(array){
	
	$('#state-name').empty();
	$('#state-name').append('<option disabled selected>Select a state (US only)</option>');
	for(i=0;i<array.length;i++){
		$('#state-name').append('<option>'+array[i]+'</option')
	}
}


function getWeatherFromJson(data){
	var weatherMain = data.weatherMain;
	var weatherDescription = data.weatherDescription;
	var temperature = Math.round(data.temperature);
	var humidity = data.humidity;
	var windSpeed = Math.round(data.windSpeed);
	
	parseWeatherToScreen(weatherMain,weatherDescription,temperature,humidity,windSpeed);
}

function getIconFromWeather(weather){
	var weatherIcon;
	if(weather==="Thunderstorm"){
		weatherIcon = "icons/thunder.svg";
	}
	if(weather==="Drizzle"){
		weatherIcon="icons/drizzle.svg";
	}
	if(weather==="Rain"){
		weatherIcon="icons/rain.svg";
	}
	if(weather==="Snow"){
		weatherIcon="icons/snow.svg";
	}
	if(weather==="Fog"){
		weatherIcon="icons/fog.svg";
	}
	if(weather==="Clear"){
		weatherIcon="icons/sun.svg";
	}
	if(weather==="Clouds"){
		weatherIcon="icons/cloudy.svg";
	}
	
	return weatherIcon;
}

function parseCitySearchErrorToScreen(){
	var $output = $('#output-div');
	$output.empty();
	
	$output.append(
		'<h2 class="city">Error: City not found</h2>' +
			'<div class="search-error">' +
				'<img class="icon-large" src="icons/error.svg">' +
			'</div>'
	)
}

function parseWeatherToScreen(weatherMain,weatherDescription,temperature,humidity,windSpeed){
	var $output = $('#output-div');
	$output.empty();
	var cityName=$('#city-name').val();
	var weatherIcon = getIconFromWeather(weatherMain);
	var weatherDescriptionCapitalised = weatherDescription.charAt(0).toUpperCase() + weatherDescription.slice(1);
	
	$output.append(
		'<h2 class="city">Weather in ' + cityName + '</h2>' +
			'<div class="weather-main">' +
				'<div class="weather">' +
					'<img class="icon" src="' + weatherIcon +'">' +
					 weatherDescriptionCapitalised +
				'</div>' +
				'<div class="temp">' +
					'<img class="icon" src="icons/temp.svg">' +
					temperature + '°C'+
				'</div>' +
				'<div class="humidity">'+
					'<img class="icon" src="icons/humidity.svg">' +
					'Humidity:' + humidity + '%' +
				'</div>' +
				'<div class="wind">' +
					'<img class="icon" src="icons/wind.svg">' +
					'Wind:' + windSpeed + 'mph' +
				'</div>' +
			'</div>'
	)
	
	
}

