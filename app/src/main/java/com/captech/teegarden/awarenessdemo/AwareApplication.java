package com.captech.teegarden.awarenessdemo;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.captech.teegarden.awarenessdemo.snapshot.SnapShotManager;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotManagerContract;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class AwareApplication extends Application implements GoogleApiClient.ConnectionCallbacks {


    private GoogleApiClient mClient;
    private SnapShotManagerContract mSnapShotManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //build and connect the api client
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .build();
        mClient.connect();

    }

    /**
     * get singleton snapshot manager
     *
     * @return
     */
    public SnapShotManagerContract getmSnapShotManager() {
        if (mSnapShotManager == null) {
            if (getConnectedClient() == null)
                return null;
            mSnapShotManager = new SnapShotManager(this, getConnectedClient());
        }

        return mSnapShotManager;
    }

    /**
     * Method to return API client if it is connected..
     * IF it is not connected, kick off another attempt to connect.
     *
     * @return GoogleAPIClient
     */
    public GoogleApiClient getConnectedClient() {
        if (mClient.isConnected()) {
            return mClient;
        }
        //start connection process....
        mClient.connect();
        return null;
    }

    /**
     * Method to attempt and connect to play services. This provides ability to
     * register a connection callback.
     *
     * @param connectionCallbacks
     */
    public void reconnectApiClient(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        mClient.registerConnectionCallbacks(connectionCallbacks);
        mClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSnapShotManager = new SnapShotManager(this, getConnectedClient());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
