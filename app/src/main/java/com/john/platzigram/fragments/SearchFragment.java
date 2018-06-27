package com.john.platzigram.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.platzigram.R;
import com.john.platzigram.adapters.PostAdapterRecyclerView;
import com.john.platzigram.models.Post;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.PostService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.pictureRecycler) RecyclerView picturesRecycler;

    SharedPreferences sharedPreferences;
    AppCompatActivity context;

    PostService postService;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        postService = RetrofitClientInstance.getRetrofitInstance().create(PostService.class);

        context = (AppCompatActivity) getActivity();
        sharedPreferences = context.getSharedPreferences("user_pref", context.getApplicationContext().MODE_PRIVATE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager((getContext()), 2);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(gridLayoutManager);

        PostAdapterRecyclerView postAdapterRecyclerView =
                new PostAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(postAdapterRecyclerView);

        return view;
    }

    public ArrayList<Post> buildPictures(){
        ArrayList<Post> posts = new ArrayList<>();

        //QUERY...
        return posts;
    }

}
