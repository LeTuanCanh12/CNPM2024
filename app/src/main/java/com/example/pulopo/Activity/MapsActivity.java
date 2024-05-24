package com.example.pulopo.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Services.Post_Location_Service;
import com.example.pulopo.Utils.LocationUtil;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.ChatMessage;
import com.example.pulopo.model.LocationCurrent;

import com.example.pulopo.model.response.ChatByUserResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pulopo.databinding.ActivityMapsBinding;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;

    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        getMyLocation();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    private void getMyLocation() {
        Intent intentService = new Intent(this, Post_Location_Service.class);
        startService(intentService);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // add location dau tien cua ban

        mMap = googleMap;
        LatLng myHome = new LatLng(LocationCurrent.getLat(),LocationCurrent.getLong() );
        mMap.addMarker(new MarkerOptions().position(myHome).title("Bạn")).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myHome, 14.5f));

        //call api lay locaion
        compositeDisposable.add(apiServer.getChatByUser(LocationUtil.getReciverId(), UserUtil.getId(), 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            ChatByUserResponse chatByUserResponse;
                            chatByUserResponse = userModel;
                            List<ChatMessage> location = new ArrayList<>();
                            for (int i = chatByUserResponse.getData().size() - 1; i >= 0; i--) {
                                ChatMessage chatMessage = new ChatMessage();
                                if(chatByUserResponse.getData().get(i).getMessageType()==1 &&
                                        chatByUserResponse.getData().get(i).getSenderId() == LocationUtil.getReciverId() ){
                                    chatMessage.mess = chatByUserResponse.getData().get(i).getMessage();
                                    location.add(chatMessage);
                                }
                            }
                            try {
                                String rs = location.get(0).getMess();
                                String[] parts = rs.split(",");

                                double latLocation =Double.parseDouble( parts[0]);
                                double longLocation =Double.parseDouble( parts[1]);

                                LatLng myFriend = new LatLng(latLocation,longLocation );
                                mMap.addMarker(new MarkerOptions().position(myFriend).title("Đối phương")).showInfoWindow();

                            }catch (Exception e){
                            }

                        },
                        throwable -> {

                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));


    }




}