package com.example.familymapclient;

import android.util.Log;

import com.example.shared.Model.AuthToken;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Request.RegisterRequest;
import com.example.shared.Result.AllEventsResult;
import com.example.shared.Result.LoginResult;
import com.example.shared.Result.PeopleResult;
import com.example.shared.Result.RegisterResult;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ServerProxy {
    private HttpURLConnection connection = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private BufferedReader reader = null;
    private Gson gson = new Gson();

    public LoginResult login(LoginRequest loginRequest, String url){
        LoginResult loginResult = new LoginResult();
        try {
            setupConnection(url);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            loginResult.setMessage("Sorry we could not connect to the server");
            return loginResult;
        }

        requestWriter(gson.toJson(loginRequest, LoginRequest.class));
        String result = responseWriter();
        loginResult= gson.fromJson(result, LoginResult.class);
        connection.disconnect();
        return loginResult;
    }

    public LoginResult register(RegisterRequest registerRequest, String url){
        LoginResult loginResult = new LoginResult();
        try {
            setupConnection(url);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            loginResult.setMessage("Sorry we could not connect to the server");
            return loginResult;
        }
        requestWriter(gson.toJson(registerRequest, RegisterRequest.class));
        String result = responseWriter();
        loginResult = gson.fromJson(result, LoginResult.class);
        connection.disconnect();
        return loginResult;
    }

    public PeopleResult getAllPeople(AuthToken token, String url){
        PeopleResult peopleResult = new PeopleResult();
        setupAuthConnection(url, token.getAuthToken());
        String result = responseWriter();
        peopleResult = gson.fromJson(result, PeopleResult.class);
        connection.disconnect();
        return peopleResult;
    }

    public AllEventsResult getAllEvents(AuthToken token, String url){
        AllEventsResult allEventsResult = new AllEventsResult();
        setupAuthConnection(url, token.getAuthToken());
        String result = responseWriter();
        allEventsResult = gson.fromJson(result, AllEventsResult.class);
        connection.disconnect();
        return allEventsResult;
    }

    public void setupConnection(String requestURL) throws SocketTimeoutException {
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000); // wait for 5 seconds for response from the server
            connection.connect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SocketTimeoutException();
        }
    }

    public void setupAuthConnection(String requestURL, String authToken){
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authToken);
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            connection.setConnectTimeout(5000);
            connection.connect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestWriter(String request){
        try {
            // Write the request body
            outputStream = connection.getOutputStream();
            byte[] input = request.getBytes();
            outputStream.write(input, 0, input.length);
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String responseWriter(){
        try {
            // Write the response body
            if(connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                inputStream = connection.getErrorStream();
            }
            else {
                inputStream = connection.getInputStream();
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }
            reader.close();
            return buffer.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
