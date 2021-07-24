package com.esmc.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	public PropertiesLoader() {
		// TODO Auto-generated constructor stub
	}
	
	public static Properties loadProperties(String resourceFileName){
        Properties configuration = new Properties();
        try(InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName)) {
			configuration.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return configuration;
    }

}
