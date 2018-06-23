package com.john.platzigram.activities;

import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.models.User;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.UserService;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {

    @BindString(R.string.toolbar_tittle_createAccount) String toolbarTittle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.email) TextInputEditText tEmail;
    @BindView(R.id.name) TextInputEditText tName;
    @BindView(R.id.user) TextInputEditText tUsername;
    @BindView(R.id.password_createAccount) TextInputEditText tPassword;
    @BindView(R.id.confirm_password) TextInputEditText tConfirmPassword;
    @BindView(R.id.errors_create_account) TextView tErrors;
    @BindView(R.id.joinUs) Button bRegister;
    @BindView(R.id.progressBar_create_account) ProgressBar progressBar;
    @BindView(R.id.confirm_password_layout) TextInputLayout lConfirmPassword;
    @BindView(R.id.password_createAccount_layout) TextInputLayout lPassword;
    @BindView(R.id.email_layout) TextInputLayout lEmail;
    @BindView(R.id.user_layout) TextInputLayout lUsername;
    @BindView(R.id.name_layout) TextInputLayout lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        showToolbar(toolbarTittle, true);
    }

    public void showToolbar(String tittle, boolean upButton){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void register(View view){
        beforeMainAction();

        if (validateFields()) {
                User user = new User(tEmail.getText().toString(), tName.getText().toString(), tUsername.getText().toString(), tPassword.getText().toString());

                UserService service = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
                Call<User> call = service.createUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        if (response.isSuccessful()) { // .code() == 200
                            Toast.makeText(getApplicationContext(), "Fuiste redirigido al login, inicia con tu nueva cuenta", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            afterMainAction();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        afterMainAction();
                        Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            afterMainAction();
        }
    }

    public boolean validateFields() {
        boolean valid = true;

        if (tName.getText().toString().isEmpty()) {
            lName.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (tUsername.getText().toString().isEmpty()) {
            lUsername.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (tPassword.getText().toString().isEmpty()) {
            lPassword.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (tConfirmPassword.getText().toString().isEmpty()) {
            lConfirmPassword.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (!tPassword.getText().toString().equals(tConfirmPassword.getText().toString())) {
            lPassword.setError("Las contraseñas no coinciden");
            lConfirmPassword.setError("Las contraseñas no coinciden");
            valid = false;
        }

        if (
                tEmail.getText().toString().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(tEmail.getText().toString()).matches()
            ){
//            requestFocus(lEmail);
//            lEmail.setErrorTextAppearance(R.style.error_appearance);
//            lEmail.setErrorEnabled(true);
            lEmail.setError("Email invalido");
            valid = false;
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
        bRegister.setEnabled(false);
        bRegister.setClickable(false);
    }

    public void afterMainAction(){
        progressBar.setVisibility(View.GONE);
        bRegister.setEnabled(true);
        bRegister.setClickable(true);
    }

}
