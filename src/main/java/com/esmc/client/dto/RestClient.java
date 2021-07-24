/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.client.dto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    public static int status;
    private static int timeout = 3;
    
    /*public static String executePost(String URLAddress,
            String param) throws MalformedURLException, UnsupportedEncodingException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("mawuli", "mawuli");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
       
        HttpPost httpPost = new HttpPost(URLAddress); //or HttpPost
        
        RequestConfig requestConfig = RequestConfig.custom().
          setConnectTimeout(timeout * 1000).
          setConnectionRequestTimeout(timeout * 1000).
          setSocketTimeout(timeout * 1000).build();
        
        StringEntity json = new StringEntity(param);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(json);
        httpPost.setConfig(requestConfig);
       
        
        
       // httpPost.setHeader("Accept", "application/json");
      //  httpPost.setHeader("Content-type", "application/json");
        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            status = response.getStatusLine().getStatusCode();

            LOGGER.info("Status: " + status);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
        String responseBody = null;
        try {
        	responseBody = client.execute(httpPost, handler);
            System.out.println("responseBody= "+responseBody);
        } catch (ClientProtocolException e) {
            LOGGER.error("Error sending request :", e);
        } catch (IOException e) {
            LOGGER.error("Error sending request :", e);
        }
        return responseBody;
     
    }*/
    public static String executePost(String URLAddress,
            String param) throws MalformedURLException, UnsupportedEncodingException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("mawuli", "mawuli");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpPost httpPost = new HttpPost(URLAddress);
        StringEntity json = new StringEntity(param);
        System.out.println("json= "+json);
        httpPost.setEntity(json);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            status = response.getStatusLine().getStatusCode();

            LOGGER.info("Status: " + status);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
        String responseBody = null;
        try {
            responseBody = client.execute(httpPost, handler);
            System.out.println("responseBody= "+responseBody);
        } catch (ClientProtocolException e) {
            LOGGER.error("Error sending request :", e);
        } catch (IOException e) {
            LOGGER.error("Error sending request :", e);
        }
        return responseBody;
    }

    public static String executePost(String URLAddress,
            Map<String, String> headerParams,
            List<NameValuePair> postParams) throws MalformedURLException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("consommer", "consommer");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpPost httpPost = new HttpPost(URLAddress);

        if (headerParams != null) {
            headerParams.keySet().forEach((header) -> {
                httpPost.addHeader(header, headerParams.get(header));
            });
        }

        // Set UTF-8 character encoding to ensure proper 
        // encoding structure in your posts
        if (postParams != null) {
            try {
                httpPost
                        .setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                 System.out.println("Error sending request :"+ e);
            }
        }

        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();

            System.out.println("Status: " + status);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
        String responseBody = null;
        try {
            responseBody = client.execute(httpPost, handler);
        } catch (ClientProtocolException e) {
            System.out.println("Error sending request :"+ e);
        } catch (IOException e) {
           System.out.println("Error sending request :"+ e);
        }
        return responseBody;
    }

    public static String executeGet(String URLAddress) throws MalformedURLException {
        System.out.println("URL : " + URLAddress);
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("mawuli", "mawuli");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpGet httpGet = new HttpGet(URLAddress);

        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();

           // LOGGER.info("Status: " + status);
            System.out.println("Status :"+ status);
      
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
        String responseBody = null;
        try {
            responseBody = client.execute(httpGet, handler);
        } catch (ClientProtocolException e) {
            LOGGER.error("Error sending request :", e);
        } catch (IOException e) {
            LOGGER.error("Error sending request :", e);
        }
        return responseBody;
    }

    public String executeGet(String URLAddress,
            Map<String, String> headerParams) throws MalformedURLException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("mawuli", "mawuli");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpGet httpGet = new HttpGet(URLAddress);

        if (headerParams != null) {
            headerParams.keySet().forEach((header) -> {
                httpGet.addHeader(header, headerParams.get(header));
            });
        }

        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();

            LOGGER.info("Status: " + status);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
        String responseBody = null;
        try {
            responseBody = client.execute(httpGet, handler);
        } catch (ClientProtocolException e) {
            LOGGER.error("Error sending request :", e);
        } catch (IOException e) {
            LOGGER.error("Error sending request :", e);
        }
        return responseBody;
    }

    public String executeSSLPost(String URLAddress,
            Map<String, String> headerParams,
            List<NameValuePair> postParams) throws MalformedURLException,
            IOException, NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException {

        HttpClientBuilder builder = HttpClientBuilder.create();

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (X509Certificate[] arg0, String arg1) -> true).build();

        @SuppressWarnings("deprecation")
        HostnameVerifier hostnameVerifier
                = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLConnectionSocketFactory sslSocketFactory
                = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http",
                        PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();

        PoolingHttpClientConnectionManager connectionMgr
                = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        builder.setConnectionManager(connectionMgr);

        builder.setSslcontext(sslContext);

        HttpClient client = builder.build();
        HttpPost httpPost = new HttpPost(URLAddress);

        if (headerParams != null) {
            headerParams.keySet().forEach((header) -> {
                httpPost.addHeader(header, headerParams.get(header));
            });
        }

        // Set UTF-8 character encoding to ensure proper 
        // encoding structure in your posts
        if (postParams != null) {
            httpPost
                    .setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
        }

        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();

            LOGGER.info("Status: " + status);
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException(
                        "Unexpected response status: " + status);
            }
        };
        String responseBody = client.execute(httpPost, handler);
        return responseBody;
    }

    public String executeSSLGet(String URLAddress,
            Map<String, String> headerParams) throws MalformedURLException,
            IOException, KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(URLAddress);

        if (headerParams != null) {
            headerParams.keySet().forEach((header) -> {
                httpget.addHeader(header, headerParams.get(header));
            });
        }

        LOGGER.info("Executing request " + httpget.getRequestLine());
        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();
            LOGGER.info("Status: " + status);
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException(
                        "Unexpected response status: " + status);
            }
        };
        String responseBody = client.execute(httpget, handler);
        return responseBody;
    }

    public String executeSSLGetAllTrusting(String URLAddress)
            throws MalformedURLException, IOException,
            KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException {
        TrustManager[] allTrustingCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    LOGGER.info("Inside TrustManager getAcceptedIssuers...");
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) throws CertificateException {
                    LOGGER.info("Inside TrustManager checkClientTrusted...");
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                    LOGGER.info("Inside TrustManager checkServerTrusted...");
                    LOGGER.info("certs......: " + Arrays.toString(certs));
                    LOGGER.info("authType...: " + authType);
                }
            }};

        SSLContextBuilder sslBuilder = new SSLContextBuilder();
        sslBuilder.loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true);

        @SuppressWarnings("deprecation")
        HostnameVerifier hostnameVerifier
                = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslSocketFactory
                = new SSLConnectionSocketFactory(sslBuilder.build(), hostnameVerifier);
        CloseableHttpClient client = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory).build();
        HttpGet httpget = new HttpGet(URLAddress);

        LOGGER.info("Executing request " + httpget.getRequestLine());

        // Set up the response handler
        ResponseHandler<String> handler = (final HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();

            LOGGER.info("Status: " + status);
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException(
                        "Unexpected response status: " + status);
            }
        };
        String responseBody = client.execute(httpget, handler);
        return responseBody;
    }
}
