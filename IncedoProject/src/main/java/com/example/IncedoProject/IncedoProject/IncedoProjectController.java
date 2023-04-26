package com.example.IncedoProject.IncedoProject;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

public class IncedoProjectController {
	@PostMapping("/validateData")
	
    public ResponseEntity<Object> validateData(@RequestBody Object requestData) throws JsonMappingException, JsonProcessingException {
        // Perform validations on the received data
    	InputData inputObj;

        if (requestData instanceof Map) {
        	 Map<String, Object> inputDataMap = (Map<String, Object>) requestData;
        	 inputObj = new InputData();
             
             inputObj.setName((String) inputDataMap.get("name"));
             inputObj.setAge((String) inputDataMap.get("age"));
             inputObj.setAddress((String) inputDataMap.get("address"));
             
        } else if (requestData instanceof String) {
        	ObjectMapper objectMapper = new ObjectMapper();
            String dataString = (String) requestData;
            // Check if data is JSON or XML
            if (isJson(dataString)) {
            	 String inputDataString = dataString;
                 inputObj = objectMapper.readValue(inputDataString, InputData.class);
            } else if (isXml(dataString)) {
            	  String inputDataString = dataString;
                  inputObj = objectMapper.readValue(inputDataString, InputData.class);
            } else {
                // Invalid data format
                return ResponseEntity.badRequest().body("Invalid data format");
            }
        } else {
            // Invalid data format
            return ResponseEntity.badRequest().body("Invalid data format");
        }

        // If data is valid, return the received data
        return ResponseEntity.ok(requestData);
    }

    // Helper methods to check if a string is JSON or XML
    private boolean isJson(String data) {
        try {
            // Attempt to parse the string as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(data);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private boolean isXml(String data) {
        try {
            // Attempt to parse the string as XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(data)));
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }
}