package com.example.familymapclient;

import com.example.shared.Model.AuthToken;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Request.RegisterRequest;
import com.example.shared.Result.AllEventsResult;
import com.example.shared.Result.LoginResult;
import com.example.shared.Result.PeopleResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ServerProxyTests {
    String baseURL;
    ServerProxy serverProxy;

    @BeforeEach
    public void setUp() {
        baseURL = "http://127.0.0.1:8080/";
        serverProxy = new ServerProxy();
    }

    @AfterEach
    public void tearDown(){

    }

    @Test
    public void loginPass() {
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(request, baseURL +"user/login/");
        assertNotNull(loginResult);
        assertNotNull(loginResult.getAuthToken());
        assertNotNull(loginResult.getPersonID());
        assertNotNull(loginResult.getUsername());
        assertTrue(loginResult.isSuccess());
        assertTrue(loginResult.getUsername().equals(request.getUserName()));
    }

    @Test
    public void loginFail() {
        LoginRequest request1 = new LoginRequest("sheila", "badPass");
        LoginRequest request2 = new LoginRequest("badUsername", "parker");
        LoginRequest request3 = new LoginRequest("badUsername", "badPass");
        LoginResult loginResult1 = serverProxy.login(request1, baseURL +"user/login/");
        LoginResult loginResult2 = serverProxy.login(request2, baseURL +"user/login/");
        LoginResult loginResult3 = serverProxy.login(request3, baseURL +"user/login/");
        assertFalse(loginResult1.isSuccess());
        assertFalse(loginResult2.isSuccess());
        assertFalse(loginResult3.isSuccess());
    }

    @Test
    public void registerPass() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("morgan");
        registerRequest.setLastName("wright");
        registerRequest.setGender('m');
        registerRequest.setEmail("test@byu.edu");
        registerRequest.setPassword("fakePass");
        String username = UUID.randomUUID().toString();
        registerRequest.setUsername(username);
        LoginResult result = serverProxy.register(registerRequest, baseURL + "user/register/");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(result.getUsername().equals(username));
    }

    @Test
    public void registerFail() {
        RegisterRequest registerRequest1 = new RegisterRequest();
        registerRequest1.setFirstName("morgan");
        registerRequest1.setLastName("wright");
        registerRequest1.setGender('m');
        registerRequest1.setEmail("test@byu.edu");
        registerRequest1.setPassword("fakePass");
        registerRequest1.setUsername("sheila");

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setFirstName("morgan");
        registerRequest2.setLastName("wright");
        registerRequest2.setGender('m');
        registerRequest2.setEmail("test@byu.edu");
        registerRequest2.setPassword("fakePass");
        registerRequest2.setUsername("patrick");

        LoginResult result1 = serverProxy.register(registerRequest1, baseURL + "user/register/");
        LoginResult result2 = serverProxy.register(registerRequest2, baseURL + "user/register/");

        assertNotNull(result1);
        assertNotNull(result2);
        assertFalse(result1.isSuccess());
        assertFalse(result2.isSuccess());
        assertTrue(result1.getMessage().equals("Error: There is already a user registered with that username."));
        assertTrue(result2.getMessage().equals("Error: There is already a user registered with that username."));
    }

    @Test
    public void getAllPeoplePass() {
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(request, baseURL +"user/login/");
        assertTrue(loginResult.isSuccess());
        AuthToken token = new AuthToken("sheila_parker", loginResult.getAuthToken());
        PeopleResult result = serverProxy.getAllPeople(token, baseURL + "person/");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().size() >= 0);
    }

    @Test
    public void getAllPeopleFail() {
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(request, baseURL +"user/login/");
        assertTrue(loginResult.isSuccess());
        AuthToken token = new AuthToken("NoPersonWithThisAuthToken", "NoPersonWithThisAuthToken");
        PeopleResult result = serverProxy.getAllPeople(token, baseURL + "person/");
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getData().size() == 0);
    }

    @Test
    public void getAllEventsPass() {
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(request, baseURL +"user/login/");
        assertTrue(loginResult.isSuccess());
        AuthToken token = new AuthToken("sheila_parker", loginResult.getAuthToken());
        AllEventsResult result = serverProxy.getAllEvents(token, baseURL + "event/");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().size() >= 0);
    }

    @Test
    public void getAllEventsFail() {
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(request, baseURL +"user/login/");
        assertTrue(loginResult.isSuccess());
        AuthToken token = new AuthToken("NoPersonWithThisAuthToken", "NoPersonWithThisAuthToken");
        AllEventsResult result = serverProxy.getAllEvents(token, baseURL + "event/");
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getData().size() == 0);
    }
}
