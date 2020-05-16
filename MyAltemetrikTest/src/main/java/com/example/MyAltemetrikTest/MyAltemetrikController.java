package com.example.MyAltemetrikTest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/")
public class MyAltemetrikController {
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("/getCovidResults/US/{st}")
	public MyResponse getCovidResults(@PathVariable("st") String st, HttpServletResponse httpServletResponse) throws ParseException, IOException {
		String result = "";
		MyResponse myResponse = new MyResponse();
		
		if (st.length()!=2) {
			httpServletResponse.setStatus(400);
			
		} else {
			
			HttpGet request = new HttpGet("https://covidtracking.com/api/v1/states/"+st+"/current.json");
	
	        try (CloseableHttpResponse response = httpClient.execute(request)) {
	
	            // Get HttpResponse Status
	            System.out.println(response.getStatusLine().toString());
	            
	            if (response.getStatusLine().getStatusCode()==200) {
	            	httpServletResponse.setStatus(202);
	            	HttpEntity entity = response.getEntity();
	            
		            if (entity != null) {
		                // return it as a String
		                result = EntityUtils.toString(entity);
		                System.out.println(result);
		                
		                //parsed to get the value
		                JsonNode node = mapper.readTree(result);
		                
		                System.out.println(node.toPrettyString());
		                
		                int totalTestResults = node.get("totalTestResults").intValue();
		                int positive = node.get("positive").intValue();
		                int negative = node.get("negative").intValue();
		                int death = node.get("death").intValue();
		                
		                System.out.println(totalTestResults);
		                
		                myResponse.setTotalTestResults(totalTestResults);
		                myResponse.setPositive(positive);
		                myResponse.setNegative(negative);
		                myResponse.setDeath(death);
		            }
	            } else {
	            	httpServletResponse.setStatus(400);
	            }
	
	
	        }
		}
		
		
		return myResponse;
	}
	
	@GetMapping("/getCovidResultsCountry")
	public MyResponse getCovidResultsCountry(HttpServletResponse httpServletResponse) throws ParseException, IOException {
		String result = "";
		MyResponse myResponse = new MyResponse();
		
		try {
			
			HttpGet request = new HttpGet("https://covidtracking.com/api/v1/US/current.json");
	
	        try (CloseableHttpResponse response = httpClient.execute(request)) {
	
	            // Get HttpResponse Status
	            System.out.println(response.getStatusLine().toString());
	            
	            if (response.getStatusLine().getStatusCode()==200) {
	            	httpServletResponse.setStatus(202);
	            	HttpEntity entity = response.getEntity();
	            
		            if (entity != null) {
		                // return it as a String
		                result = EntityUtils.toString(entity);
		                System.out.println(result);
		                
		                //parsed to get the value
		                JsonNode node = mapper.readTree(result).get(0);
		                
		                System.out.println(node.toPrettyString());
		                
		                int totalTestResults = node.get("totalTestResults").intValue();
		                int positive = node.get("positive").intValue();
		                int negative = node.get("negative").intValue();
		                int death = node.get("death").intValue();
		                
		                System.out.println(totalTestResults);
		                
		                myResponse.setTotalTestResults(totalTestResults);
		                myResponse.setPositive(positive);
		                myResponse.setNegative(negative);
		                myResponse.setDeath(death);
		            }
	            } else {
	            	httpServletResponse.setStatus(400);
	            }
	
	
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return myResponse;
	}

}
