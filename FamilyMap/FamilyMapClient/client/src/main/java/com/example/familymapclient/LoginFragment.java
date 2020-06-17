package com.example.familymapclient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shared.Model.AuthToken;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Request.RegisterRequest;
import com.example.shared.Result.AllEventsResult;
import com.example.shared.Result.LoginResult;
import com.example.shared.Result.PeopleResult;
import com.example.shared.Result.Result;


public class LoginFragment extends Fragment {

    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    EditText serverHost;
    EditText serverPort;
    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;
    Button loginBtn;
    Button registerBtn;
    Character gender = null;
    ServerProxy proxy = new ServerProxy();
    Toast toast;
    private DataCache dataCache = DataCache.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myInflatedView = inflater.inflate(R.layout.fragment_login, container, false);

        // Get each of the fields and add a watcher on them
        radioGroup = myInflatedView.findViewById(R.id.radioGroup);
        serverHost = myInflatedView.findViewById(R.id.serverHost);
        serverPort = myInflatedView.findViewById(R.id.serverPort);
        username = myInflatedView.findViewById(R.id.UserName);
        password = myInflatedView.findViewById(R.id.password);
        firstName = myInflatedView.findViewById(R.id.FirstName);
        lastName = myInflatedView.findViewById(R.id.LastName);
        email = myInflatedView.findViewById(R.id.email);
        loginBtn = myInflatedView.findViewById(R.id.loginBtn);
        registerBtn = myInflatedView.findViewById(R.id.registerBtn);

        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();
            }
        };

        serverHost.addTextChangedListener(textWatcher);
        serverPort.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.malePerson:
                        gender = 'm';
                        break;
                    case R.id.femalePerson:
                        gender = 'f';
                        break;
                    default:
                        break;
                }
                validateFields();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcessUserRequest().execute("login");
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcessUserRequest().execute("register");
            }
        });
        return myInflatedView;
    }

    public void validateFields(){
        if(serverHost.getText().length() > 0 && serverPort.getText().length() > 0 && username.getText().length() > 0 && password.getText().length() > 0
                && firstName.getText().length() > 0 && lastName.getText().length() > 0 && email.getText().length() > 0 && gender != null){
            loginBtn.setEnabled(true);
            registerBtn.setEnabled(true);
        }
        else if(serverHost.getText().length() > 0 && serverPort.getText().length() > 0 && username.getText().length() > 0 && password.getText().length() > 0){
            registerBtn.setEnabled(false);
            loginBtn.setEnabled(true);
        }
        else {
            loginBtn.setEnabled(false);
            registerBtn.setEnabled(false);
        }

    }

    private class ProcessUserRequest extends AsyncTask<String, Void, LoginResult> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Processing your request. . .");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }

        @Override
        protected LoginResult doInBackground(String... params) {
            String requestURL = "http://" + serverHost.getText().toString() + ":" + serverPort.getText().toString() + "/user/";
            LoginResult result = null;
            if (params[0].equals("login")) {
                requestURL += "login/";
                LoginRequest request = new LoginRequest(username.getText().toString(), password.getText().toString());
                result = proxy.login(request, requestURL);
                if(!result.isSuccess()){
                    return result;
                }
                Log.i("Success", "We were able to login with the user");
            } else if (params[0].equals("register")) {
                requestURL +="register/";
                RegisterRequest request = new RegisterRequest(username.getText().toString(), password.getText().toString(), email.getText().toString(),
                        firstName.getText().toString(), lastName.getText().toString(), gender);
                result = proxy.register(request, requestURL);
                if(!result.isSuccess()){
                    return result;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(LoginResult result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(result != null && !result.isSuccess()){
                toast = Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            new GetUserData().execute(result);
        }
    }

    private class GetUserData extends AsyncTask<LoginResult, Void, Result>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Getting your Family Data. . .");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }

        @Override
        protected Result doInBackground(LoginResult... loginResults) {
            String peopleURL = "http://" + serverHost.getText().toString() + ":" + serverPort.getText().toString() + "/person/";
            String eventsURL = "http://" + serverHost.getText().toString() + ":" + serverPort.getText().toString() + "/event/";
            Result newResult = new Result();
            if(loginResults[0] != null){
                AuthToken authToken = new AuthToken(loginResults[0].getPersonID(), loginResults[0].getAuthToken());
                PeopleResult peopleResult = proxy.getAllPeople(authToken, peopleURL);
                // if we had a problem getting the people
                if(!peopleResult.isSuccess()){
                    newResult.setMessage(peopleResult.getMessage());
                    newResult.setSuccess(false);
                    return newResult;
                }
                AllEventsResult allEventsResult = proxy.getAllEvents(authToken, eventsURL);
                if(!allEventsResult.isSuccess()){
                    newResult.setMessage(allEventsResult.getMessage());
                    newResult.setSuccess(false);
                    return newResult;
                }
                newResult.setSuccess(true);
                newResult.setMessage("We successfully got the people and events for the logged in user");
                dataCache.populateData(authToken, peopleResult, allEventsResult);
            }
            return newResult;
        }

        // Return result because due to needing to handel the people and event results
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(result != null && !result.isSuccess()){
                toast = Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
            ((MainActivity)getActivity()).switchToMap();
        }
    }

}