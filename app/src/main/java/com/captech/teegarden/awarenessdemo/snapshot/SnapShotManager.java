package com.captech.teegarden.awarenessdemo.snapshot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

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

public class SnapShotManager implements SnapShotManagerContract {

    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public SnapShotManager(Context context, GoogleApiClient mGoogleApiClient) {
        this.context = context;
        this.mGoogleApiClient = mGoogleApiClient;
    }

    @Override
    public void getCurrentActivity(final SnapShotResultCallback mSnapShotResultCallback) {
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            mSnapShotResultCallback.setCurrentActivity("Unknown", 0);
                        } else {
                            ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                            DetectedActivity probableActivity = ar.getMostProbableActivity();
                            mSnapShotResultCallback.setCurrentActivity(DetectedActivity.zztz(probableActivity.getType()), probableActivity.getConfidence());
                        }
                    }
                });
    }

    @Override
    public void getCurrentHeadphoneState(final SnapShotResultCallback mSnapShotResultCallback) {
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        if (!headphoneStateResult.getStatus().isSuccess()) {
                            //failed
                            mSnapShotResultCallback.setCurrentHeadphoneState("Unknown");
                        } else {
                            HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                            String state = headphoneState.getState() == 1 ? "Plugged In" : "Unplugged";
                            mSnapShotResultCallback.setCurrentHeadphoneState(state);
                        }
                    }
                });
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void getCurrentLocation(final SnapShotResultCallback mSnapShotResultCallback) {
        if (isLocationPermissionGranted()) {
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                mSnapShotResultCallback.setCurrentLocation(0, 0);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            mSnapShotResultCallback.setCurrentLocation(location.getLatitude(), location.getLongitude());
                        }
                    });
        } else {
            mSnapShotResultCallback.setLocationPermissionRequired(LOCATION);
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void getCurrentPlace(final SnapShotResultCallback mSnapShotResultCallback) {
        if (isLocationPermissionGranted()) {
            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (!placesResult.getStatus().isSuccess()
                                    || placesResult.getPlaceLikelihoods() == null
                                    || placesResult.getPlaceLikelihoods().size() == 0) {
                                mSnapShotResultCallback.setCurrentPlace("unknown", 0);
                                return;
                            }

                            PlaceLikelihood p = placesResult.getPlaceLikelihoods().get(0);
                            mSnapShotResultCallback.setCurrentPlace(p.getPlace().getName().toString(), p.getLikelihood());
                        }
                    });
        } else {
            mSnapShotResultCallback.setLocationPermissionRequired(PLACE);
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void getCurrentWeather(final SnapShotResultCallback mSnapShotResultCallback) {
        if (isLocationPermissionGranted()) {
            Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<WeatherResult>() {
                        @Override
                        public void onResult(@NonNull WeatherResult weatherResult) {
                            if (!weatherResult.getStatus().isSuccess()) {
                                mSnapShotResultCallback.setCurrentWeather("unknown", 0);
                                return;
                            }

                            Weather weather = weatherResult.getWeather();
                            StringBuilder builder = new StringBuilder();
                            for (int i : weather.getConditions()) {
                                switch (i) {
                                    case Weather.CONDITION_CLEAR:
                                        builder.append("Clear ");
                                        break;
                                    case Weather.CONDITION_FOGGY:
                                        builder.append("Foggy ");
                                        break;
                                    case Weather.CONDITION_CLOUDY:
                                        builder.append("Cloudy ");
                                        break;
                                    case Weather.CONDITION_ICY:
                                        builder.append("Icy ");
                                        break;
                                    case Weather.CONDITION_SNOWY:
                                        builder.append("Snowy ");
                                        break;
                                    case Weather.CONDITION_RAINY:
                                        builder.append("Rainy ");
                                        break;
                                    case Weather.CONDITION_STORMY:
                                        builder.append("Stormy ");
                                        break;
                                    case Weather.CONDITION_WINDY:
                                        builder.append("Windy ");
                                        break;
                                }
                            }
                            mSnapShotResultCallback.setCurrentWeather(builder.toString(), weather.getTemperature(Weather.FAHRENHEIT));
                        }
                    });
        } else {
            mSnapShotResultCallback.setLocationPermissionRequired(WEATHER);
        }
    }

    /**
     * helper method to return whether we have permissions to the device's
     * current location.
     *
     * @return true if location permission is granted.
     */
    private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
}
