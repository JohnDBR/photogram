package com.john.platzigram.activities;

import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.platzigram.R;
import com.john.platzigram.models.Post;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.image_header) ImageView picture;
    @BindView(R.id.usernameDetail) TextView username;
    @BindView(R.id.likeNumberDetail) TextView likes;
    @BindView(R.id.titleImage) TextView title;
    @BindView(R.id.textContentImageDetail) TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        ButterKnife.bind(this);

        showToolbar("", true);

        loadPost();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
        }
    }

    public void showToolbar(String tittle, boolean upButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        //collapsingToolbarLayout
    }

    public void loadPost(){
        Post post = (Post) getIntent().getSerializableExtra("post");
        Picasso.get().load(post.getPicture().getUrl()).into(picture);
        title.setText(post.getTitle());
        username.setText(post.getUser().getUsername());
        likes.setText("10");
        description.setText(post.getDescription());
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