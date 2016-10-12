package com.captech.teegarden.awarenessdemo.headphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.captech.teegarden.awarenessdemo.AwareApplication;
import com.captech.teegarden.awarenessdemo.R;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotManagerContract;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotResultCallback;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.captech.teegarden.awarenessdemo.headphone.HeadphoneActivity.FENCE_RECEIVER_ACTION;
import static com.captech.teegarden.awarenessdemo.headphone.HeadphoneActivity.HEADPHONE_FENCE_KEY;

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
 * Fence Receiver that notifies user of current weather when the headphones are plugged in.
 */

public class HeadphoneBroadcastReceiver extends BroadcastReceiver implements SnapShotResultCallback, GoogleApiClient.ConnectionCallbacks {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TextUtils.equals(FENCE_RECEIVER_ACTION, intent.getAction())) {
            return;
        }

        // The state information for the given fence is em
        FenceState fenceState = FenceState.extract(intent);
        if (TextUtils.equals(fenceState.getFenceKey(), HEADPHONE_FENCE_KEY) &&
                fenceState.getCurrentState() == FenceState.TRUE) {
            //get weather data...
            this.context = context;
            SnapShotManagerContract mSnapShotManager = ((AwareApplication) context.getApplicationContext()).getmSnapShotManager();
            if (mSnapShotManager != null) {
                mSnapShotManager.getCurrentWeather(this);
            } else {
                ((AwareApplication) context.getApplicationContext()).reconnectApiClient(this);
            }

        }

    }

    @Override
    public void setCurrentActivity(String currentActivity, int confidence) {

    }

    @Override
    public void setCurrentHeadphoneState(String headphoneState) {

    }

    @Override
    public void setCurrentLocation(double latitude, double longitude) {

    }

    @Override
    public void setCurrentPlace(String place, float likelihood) {

    }

    @Override
    public void setCurrentWeather(String weatherCondition, float temperature) {
        sendNotification("Maybe try some jazz music on this " + weatherCondition + "day!");
    }

    @Override
    public void setLocationPermissionRequired(int origination) {
        //we are here because we can't get the weather due to location permission..
        //maybe this isn't extremely important so we wouldn't have asked for the permission if we didn't have it when we registered.
        sendNotification("Maybe try some blues music!");
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_headset_black)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle("Music Suggestion")
                .setTicker("Music Suggestion")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(100, builder.build());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SnapShotManagerContract mSnapShotManager = ((AwareApplication) context.getApplicationContext()).getmSnapShotManager();
        if (mSnapShotManager != null) {
            mSnapShotManager.getCurrentWeather(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
