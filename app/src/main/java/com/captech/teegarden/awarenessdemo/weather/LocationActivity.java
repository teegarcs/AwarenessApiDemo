package com.captech.teegarden.awarenessdemo.weather;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.captech.teegarden.awarenessdemo.AwareApplication;
import com.captech.teegarden.awarenessdemo.BuildConfig;
import com.captech.teegarden.awarenessdemo.R;
import com.captech.teegarden.awarenessdemo.databinding.ActivityFenceBinding;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotManagerContract;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotResultCallback;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by teegarcs on 10/9/16.
 * <p>
 * Fence that notifies user when they are leaving the location that they registered the fence in.
 */
public class LocationActivity extends AppCompatActivity implements View.OnClickListener, SnapShotResultCallback, GoogleApiClient.ConnectionCallbacks {
    private static final int LOCATION_PERM_REQUEST = 101;
    private SnapShotManagerContract mSnapShotManager;
    public static final String FENCE_RECEIVER_ACTION = BuildConfig.APPLICATION_ID + ".LOCATIONFENCE";
    public static final String LOCATION_FENCE_KEY = "LOCATION_FENCE_KEY";

    private ActivityFenceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fence);
        binding.registerBtn.setOnClickListener(this);
        binding.unRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                registerFence();
                break;
            case R.id.unRegisterBtn:
                unRegisterFence();
                break;
        }
    }

    /**
     * Register the fence by getting the current location.
     */
    private void registerFence() {
        mSnapShotManager = ((AwareApplication) getApplication()).getmSnapShotManager();
        if (mSnapShotManager == null) {
            //the client must not be connected.. attempt to reconnect and proceed.
            ((AwareApplication) getApplication()).reconnectApiClient(this);
            return;
        }
        //we need current location for this one.
        mSnapShotManager.getCurrentLocation(this);
    }

    /**
     * unregister the location fence.
     */
    private void unRegisterFence() {
        GoogleApiClient mApiClient = ((AwareApplication) getApplication()).getConnectedClient();
        if (mApiClient == null) {
            //the client must be null.
            //handling should be added to reconnect.
            Toast.makeText(LocationActivity.this, "Location Fence Still Registered!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //unregester the location fence
        Awareness.FenceApi.updateFences(
                mApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(LOCATION_FENCE_KEY)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Toast.makeText(LocationActivity.this, "Location Fence Un-Registered!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Toast.makeText(LocationActivity.this, "Location Fence Still Registered!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setCurrentActivity(String currentActivity, int confidence) {

    }

    @Override
    public void setCurrentHeadphoneState(String headphoneState) {

    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void setCurrentLocation(double latitude, double longitude) {
        //do registering for fence here..

        //create a fence in the current location with 10 meter radius
        AwarenessFence locationFence = LocationFence.exiting(latitude, longitude, 10);
        Intent intent = new Intent(FENCE_RECEIVER_ACTION);
        //set up the pending intent to register the receiver with the fence
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        GoogleApiClient mApiClient = ((AwareApplication) getApplication()).getConnectedClient();
        if (mApiClient == null) {
            //we need to register
            ((AwareApplication) getApplication()).reconnectApiClient(this);
            return;
        }
        //we are connected and able to register our fence now
        Awareness.FenceApi.updateFences(
                mApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(LOCATION_FENCE_KEY, locationFence, mPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(LocationActivity.this, "Location Fence Registered!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LocationActivity.this, "Location Fence Un-Registered!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void setCurrentPlace(String place, float likelihood) {

    }

    @Override
    public void setCurrentWeather(String weatherCondition, float temperature) {

    }

    @Override
    public void setLocationPermissionRequired(int origination) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERM_REQUEST);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        registerFence();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERM_REQUEST) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Required!",
                        Toast.LENGTH_LONG).show();
            } else {
                mSnapShotManager.getCurrentLocation(this);
            }
        }
    }
}
