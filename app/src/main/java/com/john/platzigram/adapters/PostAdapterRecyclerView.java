package com.john.platzigram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.activities.PictureDetailActivity;
import com.john.platzigram.helpers.TimestampFormater;
import com.john.platzigram.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by John on 6/5/2018.
 */

public class PostAdapterRecyclerView extends RecyclerView.Adapter<PostAdapterRecyclerView.PostViewHolder> {

    private ArrayList<Post> posts;
    private int resource;
    private Activity activity;

    public PostAdapterRecyclerView(ArrayList<Post> posts, int resource, Activity activity) {
        this.posts = posts;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.usernameCard.setText(post.getUser().getUsername());
        holder.timeCard.setText(TimestampFormater.convertTimestampToAgoString(post.getCreated_at()));
        holder.likeNumberCard.setText("10 Me Gusta");
        Picasso.get().load(post.getPicture().getUrl()).into(holder.pictureCard);

        holder.pictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PictureDetailActivity.class);
                intent.putExtra("post", post);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    activity,
                                    view,
                                    activity.getString(R.string.transitionName_picture)
                            ).toBundle());
                } else {
                    activity.startActivity(intent);
                }

//                Toast.makeText(activity, "PROBANDO!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.picture) ImageView pictureCard;
        @BindView(R.id.userNameCard) TextView usernameCard;
        @BindView(R.id.timeCard) TextView timeCard;
        @BindView(R.id.likeNumberCard) TextView likeNumberCard;

        public PostViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
