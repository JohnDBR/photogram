package com.john.platzigram.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.activities.ContainerActivity;
import com.john.platzigram.activities.LoginActivity;
import com.john.platzigram.adapters.PictureAdapterRecyclerView;
import com.john.platzigram.models.Picture;
import com.john.platzigram.models.Post;
import com.john.platzigram.models.User;
import com.john.platzigram.network.RetrofitClientInstance;
import com.john.platzigram.services.AuthenticationService;
import com.john.platzigram.services.UserService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pictureProfileRecycler) RecyclerView picturesRecycler;
    @BindView(R.id.collapsingToolbarProfile) CollapsingToolbarLayout ctl;
    @BindView(R.id.progressBar_profile_fragment) ProgressBar progressBar;
    @BindView(R.id.userProfilePicture) CircleImageView profilePicture;

    SharedPreferences sharedPreferences;
    AppCompatActivity context;

    AuthenticationService authService;
    UserService userService;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        showToolbar("",false);

        userService = RetrofitClientInstance.getRetrofitInstance().create(UserService.class);
        authService = RetrofitClientInstance.getRetrofitInstance().create(AuthenticationService.class);

        context = (AppCompatActivity) getActivity();
        sharedPreferences = context.getSharedPreferences("user_pref", context.getApplicationContext().MODE_PRIVATE);

        loadCurrentUserInfo();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getContext()));
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView =
                new PictureAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(pictureAdapterRecyclerView);

        return view;
    }

    public void loadCurrentUserInfo() {
        beforeMainAction();
        String user_id = sharedPreferences.getString("user_id", null);
        if (user_id != null && !user_id.isEmpty()) {
            Call<User> call = userService.getUser(Integer.valueOf(user_id));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    if (response.isSuccessful()) { // .code() == 200
                        User currentUser = response.body();
                        ctl.setTitle(currentUser.getName());
                        if (currentUser.getPicture() != null){
                            Picasso.get().load(currentUser.getPicture().getUrl()).into(profilePicture);
                        }
                        afterMainAction();
                    } else {
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public ArrayList<Post> buildPictures(){
        ArrayList<Post> pictures = new ArrayList<>();
        pictures.add(new Post("http://res.cloudinary.com/johndbr/image/upload/v1525377721/c3e1473e7672485d864ff010ccb59633.jpg", "Uriel Ramirez", "4 dias", "3 Me Gusta"));
        pictures.add(new Post("http://res.cloudinary.com/johndbr/image/upload/v1526056420/f381eb8f1cec4fec81e61677e1ade7f5.jpg", "Juan Pablo", "3 dias", "10 Me Gusta"));
        pictures.add(new Post("http://res.cloudinary.com/johndbr/image/upload/v1525377544/985994d7924a43788275f2400f73c40d.jpg", "Anahi Salgado", "2 dias", "9 Me Gusta"));
        return pictures;
    }

    public void showToolbar(String tittle, boolean upButton){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        setHasOptionsMenu(true);
//        ctl.setTitle("Uriel Ramirez");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                beforeMainAction();
                String token = sharedPreferences.getString("token", null);
                if(token != null && !token.isEmpty()){
                    Call<ResponseBody> call = authService.logout("Token token=".concat(token));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                            if (response.isSuccessful()) { // .code() == 200
                                try {
                                    JSONObject responseJson = new JSONObject(new JSONTokener(response.body().string()));
                                    sharedPreferences.edit().clear().commit();
                                    afterMainAction();
                                    Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    context.finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                afterMainAction();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            afterMainAction();
                            Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void beforeMainAction(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void afterMainAction(){
        progressBar.setVisibility(View.GONE);
    }
}
