package com.captech.teegarden.awarenessdemo.headphone;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.captech.teegarden.awarenessdemo.AwareApplication;
import com.captech.teegarden.awarenessdemo.BuildConfig;
import com.captech.teegarden.awarenessdemo.R;
import com.captech.teegarden.awarenessdemo.databinding.ActivityFenceBinding;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
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
 * Fence that notifies user when headphones are plugged in. Let them know the weather.
 *
 * Note that the weather data needed for the corresponding notification needs the location permission
 * The location permission check is not defined here as an alternative route is taken in the receiver if the
 * permission does not exist.
 */
public class HeadphoneActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {
    private ActivityFenceBinding binding;
    public static final String FENCE_RECEIVER_ACTION = BuildConfig.APPLICATION_ID + ".HEADPHONEFENCE";
    public static final String HEADPHONE_FENCE_KEY = "HEADPHONE_FENCE_KEY";

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

    private void registerFence() {
        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        Intent intent = new Intent(FENCE_RECEIVER_ACTION);
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
                        .addFence(HEADPHONE_FENCE_KEY, headphoneFence, mPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(HeadphoneActivity.this, "Headphone Fence Registered!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HeadphoneActivity.this, "Headphone Fence Un-Registered!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void unRegisterFence() {
        GoogleApiClient mApiClient = ((AwareApplication) getApplication()).getConnectedClient();
        if (mApiClient == null) {
            Toast.makeText(HeadphoneActivity.this, "Headphone Fence Still Registered!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Awareness.FenceApi.updateFences(
                mApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(HEADPHONE_FENCE_KEY)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Toast.makeText(HeadphoneActivity.this, "Headphone Fence Un-Registered!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Toast.makeText(HeadphoneActivity.this, "Headphone Fence Still Registered!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        registerFence();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
