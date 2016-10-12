package com.captech.teegarden.awarenessdemo.weather;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.captech.teegarden.awarenessdemo.R;
import com.google.android.gms.awareness.fence.FenceState;

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
 * Fence Receiver that notifies user when they leave the location defined.
 */

public class LocationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TextUtils.equals(LocationActivity.FENCE_RECEIVER_ACTION, intent.getAction())) {
            return;//try to catch any mistaken deliveries
        }

        FenceState fenceState = FenceState.extract(intent);
        //during transition periods the receiver may be hit. You need to ensure the fence state is true
        //which means it meets the criteria you defined for the fence.
        if (TextUtils.equals(fenceState.getFenceKey(), LocationActivity.LOCATION_FENCE_KEY) &&
                fenceState.getCurrentState() == FenceState.TRUE) {
            String message = "Are you leaving?";

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_near_me_black)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("Leaving?")
                    .setTicker("Leaving?")
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(102, builder.build());
        }
    }
}
