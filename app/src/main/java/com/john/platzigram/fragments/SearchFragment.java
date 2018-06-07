package com.john.platzigram.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.platzigram.R;
import com.john.platzigram.adapters.PictureAdapterRecyclerView;
import com.john.platzigram.models.Picture;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView picturesRecycler = (RecyclerView) view.findViewById(R.id.pictureRecycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager((getContext()), 2);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(gridLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView =
                new PictureAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(pictureAdapterRecyclerView);

        return view;
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1525377721/c3e1473e7672485d864ff010ccb59633.jpg", "Uriel Ramirez", "4 dias", "3 Me Gusta"));
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1526056420/f381eb8f1cec4fec81e61677e1ade7f5.jpg", "Juan Pablo", "3 dias", "10 Me Gusta"));
        pictures.add(new Picture("http://res.cloudinary.com/johndbr/image/upload/v1525377544/985994d7924a43788275f2400f73c40d.jpg", "Anahi Salgado", "2 dias", "9 Me Gusta"));
        return pictures;
    }

}
