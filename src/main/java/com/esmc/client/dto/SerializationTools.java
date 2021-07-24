/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class SerializationTools {

    public static String jsonSerialise(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SerializationTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Object jsonDeserialise(String objectData, Class type) {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            return mapper.readValue(objectData, type);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SerializationTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(SerializationTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Object jsonListDeserialise(String objectData, Class type) {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, type);
            return mapper.readValue(objectData, javaType);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SerializationTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(SerializationTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
