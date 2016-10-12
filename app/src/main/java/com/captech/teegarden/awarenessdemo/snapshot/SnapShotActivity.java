package com.captech.teegarden.awarenessdemo.snapshot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.captech.teegarden.awarenessdemo.AwareApplication;
import com.captech.teegarden.awarenessdemo.R;
import com.captech.teegarden.awarenessdemo.databinding.ActivitySnapShotBinding;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.captech.teegarden.awarenessdemo.R.id.weatherLoader;

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
 */

public class SnapShotActivity extends AppCompatActivity implements View.OnClickListener, SnapShotResultCallback, GoogleApiClient.ConnectionCallbacks {
    private static final int LOCATION_PERM_REQUEST = 101;
    private int originationVal = -1;
    private SnapShotManagerContract mSnapShotManager;
    private ActivitySnapShotBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSnapShotManager = ((AwareApplication) getApplication()).getmSnapShotManager();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_snap_shot);
        binding.weatherBtn.setOnClickListener(this);
        binding.placeBtn.setOnClickListener(this);
        binding.locationBtn.setOnClickListener(this);
        binding.headphoneBtn.setOnClickListener(this);
        binding.activityBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (mSnapShotManager == null) {
            //attempt to reconnect...
            ((AwareApplication) getApplication()).reconnectApiClient(this);
            //you might want to put a loader/error handler here
            return;
        }
        switch (v.getId()) {
            case R.id.weatherBtn:
                binding.weatherBtn.setEnabled(false);
                binding.weatherLoader.setVisibility(View.VISIBLE);
                mSnapShotManager.getCurrentWeather(this);
                break;
            case R.id.placeBtn:
                binding.placeBtn.setEnabled(false);
                binding.placeLoader.setVisibility(View.VISIBLE);
                mSnapShotManager.getCurrentPlace(this);
                break;
            case R.id.locationBtn:
                binding.locationBtn.setEnabled(false);
                binding.locationLoader.setVisibility(View.VISIBLE);
                mSnapShotManager.getCurrentLocation(this);
                break;
            case R.id.headphoneBtn:
                binding.headphoneBtn.setEnabled(false);
                binding.headphoneLoader.setVisibility(View.VISIBLE);
                mSnapShotManager.getCurrentHeadphoneState(this);
                break;
            case R.id.activityBtn:
                binding.activityBtn.setEnabled(false);
                binding.activityLoader.setVisibility(View.VISIBLE);
                mSnapShotManager.getCurrentActivity(this);
                break;
        }

    }

    @Override
    public void setCurrentActivity(String activity, int confidence) {
        binding.activityBtn.setEnabled(true);
        binding.activityLoader.setVisibility(View.GONE);
        binding.currentActivity.setText(activity);
        binding.activityLikelihood.setText(String.valueOf(confidence));
    }

    @Override
    public void setCurrentHeadphoneState(String headphoneState) {
        binding.headphoneBtn.setEnabled(true);
        binding.headphoneLoader.setVisibility(View.GONE);
        binding.currentHeadphoneState.setText(headphoneState);
    }

    @Override
    public void setCurrentLocation(double latitude, double longitude) {
        binding.locationBtn.setEnabled(true);
        binding.locationLoader.setVisibility(View.GONE);
        binding.latitudeValue.setText(String.valueOf(latitude));
        binding.longitudeValue.setText(String.valueOf(longitude));
    }

    @Override
    public void setCurrentPlace(String place, float likelihood) {
        binding.placeBtn.setEnabled(true);
        binding.placeValue.setText(place);
        binding.placeLikelihoodValue.setText(String.valueOf(likelihood));
        binding.placeLoader.setVisibility(View.GONE);
    }

    @Override
    public void setCurrentWeather(String weatherCondition, float temperature) {
        binding.weatherBtn.setEnabled(true);
        binding.weatherValue.setText(weatherCondition);
        binding.temperatureValue.setText(String.valueOf(temperature) + " F");
        binding.weatherLoader.setVisibility(View.GONE);

    }

    @Override
    public void setLocationPermissionRequired(int origination) {
        this.originationVal = origination;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERM_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERM_REQUEST) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //they declined
                switch (originationVal) {
                    case SnapShotManager.WEATHER:
                        binding.weatherLoader.setVisibility(View.GONE);
                        binding.weatherBtn.setEnabled(true);
                        break;
                    case SnapShotManagerContract.PLACE:
                        binding.placeLoader.setVisibility(View.GONE);
                        binding.placeBtn.setEnabled(true);
                        break;
                    case SnapShotManagerContract.LOCATION:
                        binding.locationLoader.setVisibility(View.GONE);
                        binding.locationBtn.setEnabled(true);
                        break;
                }
                this.originationVal = -1;
            } else {
                //they accepted
                switch (originationVal) {
                    case SnapShotManager.WEATHER:
                        mSnapShotManager.getCurrentWeather(this);
                        break;
                    case SnapShotManagerContract.PLACE:
                        mSnapShotManager.getCurrentPlace(this);
                        break;
                    case SnapShotManagerContract.LOCATION:
                        mSnapShotManager.getCurrentLocation(this);
                        break;
                }
                this.originationVal = -1;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSnapShotManager = ((AwareApplication) getApplication()).getmSnapShotManager();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
