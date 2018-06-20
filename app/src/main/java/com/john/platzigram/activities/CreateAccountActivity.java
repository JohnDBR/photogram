package com.john.platzigram.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    ProgressDialog progressDialog;

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
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        tErrors.setVisibility(view.GONE);
        tErrors.setText("");
        bRegister.setEnabled(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        if (!fieldsEmpty() && passwordsMatch()) {
            User user = new User(tEmail.getText().toString(), tName.getText().toString(), tUsername.getText().toString(), tPassword.getText().toString());

            UserService service = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
            Call<User> call = service.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    bRegister.setEnabled(true);
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), "Estas siendo ridirigido al login", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                    bRegister.setEnabled(true);
                }
            });
        } else {
            progressDialog.dismiss();
            bRegister.setEnabled(true);
        }
    }

    public boolean fieldsEmpty(){
        if (
            tEmail.getText().toString().isEmpty() ||
            tName.getText().toString().isEmpty() ||
            tUsername.getText().toString().isEmpty() ||
            tPassword.getText().toString().isEmpty() ||
            tConfirmPassword.getText().toString().isEmpty()
            ) {
            addError("Todos los campos son requeridos");
            return true;
        }
        return false;
    }

    public boolean passwordsMatch(){
        if (tPassword.getText().toString().equals(tConfirmPassword.getText().toString())) {
            return true;
        }
        addError("Las contraseñas no coinciden");
        return false;
    }

    public void addError(String newError){
        tErrors.setVisibility(View.VISIBLE);
        String currentError = (String) tErrors.getText();
        newError = currentError.concat("\n").concat(newError);
        tErrors.setText(newError);
    }

}
