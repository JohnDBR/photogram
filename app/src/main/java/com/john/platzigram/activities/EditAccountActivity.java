package com.john.platzigram.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.models.User;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.AuthenticationService;
import com.john.platzigram.services.UserService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    @BindString(R.string.toolbar_tittle_editAccount) String toolbarTittle;
    @BindString(R.string.edit_account_button) String mainButtonText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.email_edit) TextInputEditText tEmail;
    @BindView(R.id.name_edit) TextInputEditText tName;
    @BindView(R.id.user_edit) TextInputEditText tUsername;
    @BindView(R.id.password_editAccount) TextInputEditText tPassword;
    @BindView(R.id.confirm_edit_password) TextInputEditText tConfirmPassword;
    @BindView(R.id.edit) Button bEdit;
    @BindView(R.id.progressBar_edit_account) ProgressBar progressBar;
    @BindView(R.id.confirm_password_edit_layout) TextInputLayout lConfirmPassword;
    @BindView(R.id.password_editAccount_layout) TextInputLayout lPassword;
    @BindView(R.id.email_edit_layout) TextInputLayout lEmail;
    @BindView(R.id.user_edit_layout) TextInputLayout lUsername;
    @BindView(R.id.name_edit_layout) TextInputLayout lName;
    @BindView(R.id.ivPicture_editAccount) ImageView ivPicture;
    @BindView(R.id.errors_edit_account) TextView tErrors;

    File imageSelected = null;
    User currentUser = null;

    SharedPreferences sharedPreferences;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
        showToolbar(toolbarTittle, true);

        bEdit.setText(mainButtonText);

        userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);

        sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);

        loadCurrentUserInfo();
    }

    public void showToolbar(String tittle, boolean upButton){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void loadCurrentUserInfo() {
        String user_id = sharedPreferences.getString("user_id", null);
        if (user_id != null && !user_id.isEmpty()) {
            Call<User> call = userService.getUser(Integer.valueOf(user_id));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) { // .code() == 200
                        currentUser = response.body();
                        tEmail.setText(currentUser.getEmail());
                        tName.setText(currentUser.getName());
                        tUsername.setText(currentUser.getUsername());
                        if (currentUser.getPicture() != null){
                                Picasso.get().load(currentUser.getPicture().getUrl()).into(ivPicture);
                        }
                        afterMainAction();
                    } else {
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void editAccount(View view){
        beforeMainAction();

        if (validateFields()) {
            UserService service = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageSelected);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("image", imageSelected.getName(), requestFile);

            RequestBody email =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tEmail.getText().toString());

            RequestBody name =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tName.getText().toString());

            RequestBody username =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tUsername.getText().toString());

            RequestBody password =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tPassword.getText().toString());

            String user_id = sharedPreferences.getString("user_id", null);
            String token = sharedPreferences.getString("token", null);

            Call<User> call = service.updateUser(
                    "Token token=".concat(token),
                    Integer.valueOf(user_id),
                    email,
                    username,
                    name,
                    password,
                    image);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) { // .code() == 200
                        finish();
                    } else {
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
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

        if (imageSelected == null){
            if (currentUser.getPicture() != null) {
                try {
                    ivPicture.setDrawingCacheEnabled(true);
                    ivPicture.buildDrawingCache();
                    imageSelected = new File(getCacheDir(), "loadedImage.jpg"); // getAppContext().getFilesDir() getCacheDir()
                    imageSelected.createNewFile();

//                    FileOutputStream fos = new FileOutputStream(imageSelected);
//                    Bitmap bm = ivPicture.getDrawingCache();
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();

                    //Convert bitmap to byte array
                    Bitmap bitmap = ivPicture.getDrawingCache();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    //write the bytes in file
                    FileOutputStream fos = new FileOutputStream(imageSelected);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                addError("Seleccione/Tome una foto");
                valid = false;
            }
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
        bEdit.setEnabled(false);
        bEdit.setClickable(false);
    }

    public void afterMainAction(){
        progressBar.setVisibility(View.GONE);
        bEdit.setEnabled(true);
        bEdit.setClickable(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(getApplicationContext(), "Image Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the images

                imageSelected = imageFile;
                setImage(imageSelected);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (imageSelected != null){
            setImage(imageSelected);
        }
    }

    public void pictureHandler(View view){
        EasyImage.openChooserWithGallery(this, "Selecciona una imagen", 0);
    }

    public void setImage(File imageFile){
        Picasso.get().load(imageFile).into(ivPicture);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
