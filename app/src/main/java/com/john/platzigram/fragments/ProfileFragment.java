package com.john.platzigram.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.john.platzigram.R;
import com.john.platzigram.activities.EditAccountActivity;
import com.john.platzigram.activities.LoginActivity;
import com.john.platzigram.adapters.PostAdapterRecyclerView;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pictureProfileRecycler) RecyclerView picturesRecycler;
    @BindView(R.id.collapsingToolbarProfile) CollapsingToolbarLayout ctl;
//    @BindView(R.id.progressBar_profile_fragment) ProgressBar progressBar;
    @BindView(R.id.userProfilePicture) CircleImageView profilePicture;
    @BindView(R.id.refresh_my_posts) SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    AppCompatActivity context;

    AuthenticationService authService;
    UserService userService;

    ArrayList<Post> posts;
    PostAdapterRecyclerView postAdapterRecyclerView;

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
        posts = new ArrayList<>();
        buildPosts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getContext()));
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        postAdapterRecyclerView =
                new PostAdapterRecyclerView(posts, R.layout.cardview_picture, getActivity());

        picturesRecycler.setAdapter(postAdapterRecyclerView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCurrentUserInfo();
                buildPosts();
            }
        });

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
                    Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void buildPosts(){
        beforeMainAction();
        String token = sharedPreferences.getString("token", null);
        String user_id = sharedPreferences.getString("user_id", null);
        if(token != null && !token.isEmpty() && user_id != null && !user_id.isEmpty()){
            Call<List<Post>> call = userService.getAllPosts("Token token=".concat(token), Integer.valueOf(user_id));
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
                new AlertDialog.Builder(context)
                        .setTitle("Logout")
                        .setMessage("Estas seguro que quieres salir de la aplicacion?")
                        .setIcon(R.drawable.logout_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                logout();
                            }})

                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case R.id.editAccount:
                Intent intent = new Intent(context.getApplicationContext(), EditAccountActivity.class);
                startActivity(intent);
                break;

            case R.id.deleteAccount:
                new AlertDialog.Builder(context)
                        .setTitle("Eliminar Cuenta")
                        .setMessage("Estas seguro que quieres eliminar tu cuenta?")
                        .setIcon(R.drawable.trash_black)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteAccount();
                            }})

                        .setNegativeButton(android.R.string.no, null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        beforeMainAction();
        String token = sharedPreferences.getString("token", null);
        if(token != null && !token.isEmpty()){
            Call<ResponseBody> call = authService.logout("Token token=".concat(token));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
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
                        sharedPreferences.edit().clear().commit();
                        afterMainAction();
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        context.finish();
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void deleteAccount(){
        beforeMainAction();
        String token = sharedPreferences.getString("token", null);
        String user_id = sharedPreferences.getString("user_id", null);
        if (token != null && !token.isEmpty() && user_id != null && !user_id.isEmpty()) {
            Call<User> call = userService.deleteUser("Token token=".concat(token), Integer.valueOf(user_id));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(context.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) { // .code() == 200
                        afterMainAction();
                        sharedPreferences.edit().clear().commit();
                        afterMainAction();
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        context.finish();
                    } else {
                        afterMainAction();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    afterMainAction();
                    Toast.makeText(context.getApplicationContext(), "Something went wrong... Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void beforeMainAction(){
//        progressBar.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(true);
    }

    public void afterMainAction(){
//        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }
}
