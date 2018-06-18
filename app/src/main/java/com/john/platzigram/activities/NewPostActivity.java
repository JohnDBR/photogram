package com.john.platzigram.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.btnTakePicture) Button btnTakePicture;
    @BindString(R.string.toolbar_tittle_newPost) String toolbarTittle;

    File imageSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        showToolbar(toolbarTittle, true);
    }

    private void showToolbar(String tittle, boolean upButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
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
}
