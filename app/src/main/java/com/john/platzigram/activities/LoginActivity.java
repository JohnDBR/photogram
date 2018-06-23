package com.john.platzigram.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.models.User;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.AuthenticationService;
import com.john.platzigram.services.UserService;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//METHODS TO MAKE HTTP REQUEST ASYNCTASK AND VOLLEY, RETROFIT SOUNDS REALY GOOD
//METHODS TO GET IMAGES WITH ASYNC GLIDE, PICASSO, NETWORKIMAGEVIEW, UNIVERSAL IMAGE LOADER
//LOCAL DATA PERSISTENT WITH SHARED PREFERENCES
//BROADCAST RECEIVER FOR THE NOTIFICATIONS

//THREADING AVANZADO RX JAVA
//ANDROID ARSENAL TO SEARCH HEAVY STUFF

//SEARCH VIEW ANDROID WIDGET OVER LIBRARIES?
//BOTTOM BAR ANDROID WIDGET OVER LIBRARIES?

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username) TextInputEditText tUsername;
    @BindView(R.id.password) TextInputEditText tPassword;
    @BindView(R.id.errors_login) TextView tErrors;
    @BindView(R.id.createHere) TextView tCreateAccount;
    @BindView(R.id.progressBar_login) ProgressBar progressBar;
    @BindView(R.id.login) Button bLogin;

    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if(token != null && !token.isEmpty()){
            Intent intent = new Intent(getBaseContext(), ContainerActivity.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void goCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void goContainerActivity(View view) {
        beforeMainAction();

        if (validateFields()) {
            JSONObject json = new JSONObject();
            try {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(tUsername.getText().toString()).matches() ) { //tUsername.getText().toString().contains("@")
                    json.put("email", tUsername.getText().toString());
                } else {
                    json.put("username", tUsername.getText().toString());
                }
                json.put("password", tPassword.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody request = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            AuthenticationService service = RetrofitClientInstance.getRetrofitInstance().create(AuthenticationService.class);
            Call<ResponseBody> call = service.login(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful()) { // .code() == 200
                        try {
                            JSONObject responseJson = new JSONObject(new JSONTokener(response.body().string()));

                            SharedPreferences sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor writer = sharedPreferences.edit();
                            writer.putString("token", responseJson.getString("secret"));
                            writer.putString("user_id", responseJson.getString("user_id"));
                            writer.commit();

                            afterMainAction();
                            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        addError("Credenciales invalidas");
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            afterMainAction();
        }
    }

    public void goPlatziWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.platzi.com")));
    }

    public boolean validateFields() {
        boolean valid = true;

        if (tPassword.getText().toString().isEmpty()) {
            tPassword.setError("Contraseña invalida");
            valid = false;
        }

        if (tUsername.getText().toString().isEmpty()){
            tUsername.setError("Username/Email invalido");
            valid = false;
        }

        return valid;
    }

    public void addError(String newError){
        tErrors.setVisibility(View.VISIBLE);
        String currentError = (String) tErrors.getText();
        newError = currentError.concat("\n").concat(newError);
        tErrors.setText(newError);
    }

    public void beforeMainAction(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        progressBar.setVisibility(View.VISIBLE);
        tErrors.setVisibility(View.GONE);
        tErrors.setText("");
        bLogin.setEnabled(false);
        bLogin.setClickable(false);
        tCreateAccount.setEnabled(false);
        tCreateAccount.setClickable(false);
    }

    public void afterMainAction(){
        progressBar.setVisibility(View.GONE);
        bLogin.setEnabled(true);
        bLogin.setClickable(true);
        tCreateAccount.setEnabled(true);
        tCreateAccount.setClickable(true);
    }
}
