package com.john.platzigram.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.activities.NewPostActivity;
import com.john.platzigram.adapters.PostAdapterRecyclerView;
import com.john.platzigram.models.Post;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.PostService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pictureRecycler) RecyclerView picturesRecycler;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.refresh_posts) SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    AppCompatActivity context;

    PostService postService;

    ArrayList<Post> posts;
    PostAdapterRecyclerView postAdapterRecyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        showToolbar(getResources().getString(R.string.tab_home), false);

        postService = RetrofitClientInstance.getRetrofitInstance().create(PostService.class);

        context = (AppCompatActivity) getActivity();
        sharedPreferences = context.getSharedPreferences("user_pref", context.getApplicationContext().MODE_PRIVATE);

        posts = new ArrayList<>();
        buildPosts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getContext()));
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        postAdapterRecyclerView =
                new PostAdapterRecyclerView(posts, R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(postAdapterRecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NewPostFragment newPostFragment = new NewPostFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, newPostFragment)
////                        .addToBackStack(null)
//                        .commit();

//                startActivity(new Intent(getActivity(), NewPostActivity.class));

                Intent intent = new Intent(getActivity(), NewPostActivity.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    getActivity().getWindow().setExitTransition(explode);
                    getActivity().startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    getActivity(),
                                    fab,
                                    getActivity().getString(R.string.transitionName_picture)
                            ).toBundle());
                } else {
                    getActivity().startActivity(intent);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buildPosts();
            }
        });

        return view;
    }

    public void buildPosts(){
        beforeMainAction();
        String token = sharedPreferences.getString("token", null);
        String user_id = sharedPreferences.getString("user_id", null);
        if(token != null && !token.isEmpty() && user_id != null && !user_id.isEmpty()){
            Call<List<Post>> call = postService.getAllPosts("Token token=".concat(token));
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) { // .code() == 200
                        afterMainAction();
                        posts.clear();
                        posts.addAll(response.body());
                        postAdapterRecyclerView.notifyDataSetChanged();
                    } else {
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void showToolbar(String tittle, boolean upButton){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void beforeMainAction(){
    }

    public void afterMainAction(){
        refreshLayout.setRefreshing(false);
    }
}
