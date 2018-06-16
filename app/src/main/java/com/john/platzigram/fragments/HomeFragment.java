package com.john.platzigram.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;

import com.john.platzigram.R;
import com.john.platzigram.activities.NewPostActivity;
import com.john.platzigram.activities.PictureDetailActivity;
import com.john.platzigram.adapters.PictureAdapterRecyclerView;
import com.john.platzigram.models.Picture;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pictureRecycler) RecyclerView picturesRecycler;
    @BindView(R.id.fab) FloatingActionButton fab;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getContext()));
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView =
                new PictureAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(pictureAdapterRecyclerView);

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

        return view;
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1525377721/c3e1473e7672485d864ff010ccb59633.jpg", "Uriel Ramirez", "4 dias", "3 Me Gusta"));
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1526056420/f381eb8f1cec4fec81e61677e1ade7f5.jpg", "Juan Pablo", "3 dias", "10 Me Gusta"));
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1525377544/985994d7924a43788275f2400f73c40d.jpg", "Anahi Salgado", "2 dias", "9 Me Gusta"));
        return pictures;
    }

    public void showToolbar(String tittle, boolean upButton){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
