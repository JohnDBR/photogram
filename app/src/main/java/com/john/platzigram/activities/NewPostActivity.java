package com.john.platzigram.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.models.Post;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.PostService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

public class NewPostActivity extends AppCompatActivity {

    @BindString(R.string.toolbar_tittle_newPost) String toolbarTittle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.newPost_tittle) TextInputEditText tTitle;
    @BindView(R.id.newPost_description) TextInputEditText tDescription;
    @BindView(R.id.newPost_tittle_layout) TextInputLayout lTitle;
    @BindView(R.id.newPost_description_layout) TextInputLayout lDescription;
    @BindView(R.id.btnTakePicture) Button btnTakePicture;
    @BindView(R.id.errors_new_post) TextView tErrors;
    @BindView(R.id.btnPost) Button bPost;

    File imageSelected = null;

    SharedPreferences sharedPreferences;

    PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        showToolbar(toolbarTittle, true);

        postService = RetrofitClientInstance.getRetrofitInstance().create(PostService.class);

        sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
    }

    private void showToolbar(String tittle, boolean upButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void createPost(View view){
        beforeMainAction();
        if (validateFields()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageSelected);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("image", imageSelected.getName(), requestFile);

            RequestBody title =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tTitle.getText().toString());

            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), tDescription.getText().toString());

            String token = sharedPreferences.getString("token", null);

            if(token != null && !token.isEmpty()){
                Call<Post> call = postService.createPost("Token token=".concat(token), title, description, image);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) { // .code() == 200
                            afterMainAction();
//                            Post post = response.body();
                            finish();
                        } else {
                            afterMainAction();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        afterMainAction();
                        Toast.makeText(getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
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

//                Bitmap image = BitmapFactory.decodeFile(imageFile.getPath());
//                Log.d("tag", imageFile.getPath());

//                Bitmap img = Bitmap.createScaledBitmap(image, 400, 400, true);

//                ivPicture.setImageBitmap(image);

//                android.support.media.ExifInterface exif = new android.support.media.ExifInterface(filePath);
//                int rotation = exif.getAttributeInt(android.support.media.ExifInterface.TAG_ORIENTATION, android.support.media.ExifInterface.ORIENTATION_NORMAL);
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

    public boolean validateFields() {
        boolean valid = true;

        if (tTitle.getText().toString().isEmpty()) {
            lTitle.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (tDescription.getText().toString().isEmpty()) {
            lDescription.setError("Todos los campos son requeridos");
            valid = false;
        }

        if (imageSelected == null){
            addError("Seleccione/Tome una foto");
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

        bPost.setEnabled(false);
        bPost.setClickable(false);
    }

    public void afterMainAction(){
        bPost.setEnabled(true);
        bPost.setClickable(true);
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
