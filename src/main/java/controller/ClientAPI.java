package controller;
import utils.*;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Servlet implementation class ClientAPI
 */

@WebServlet("/clientapi")
public class ClientAPI extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		Map<String,Double> coords = LocationUtils.getGeolocation(request);
		PrintWriter out = response.getWriter();
		
		
		if(coords.isEmpty()) {
			response.setContentType("application/text");
			out = response.getWriter();
			String error = "city search error";
			out.write(error);
			out.close();
		} else {			
			response.setContentType("application/json");
			String currentWeather = WeatherUtils.getWeather(coords);
			out.write(currentWeather);
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
