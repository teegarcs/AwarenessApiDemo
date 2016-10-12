package com.captech.teegarden.awarenessdemo.snapshot;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 * Created by teegarcs on 10/9/16.
 */

public interface SnapShotManagerContract {

    /**
     * Start request for the current most probably activity & likelihood
     * Set a result callback interface where the result will be delivered to.
     * @param mSnapShotResultCallback
     */
    void getCurrentActivity(SnapShotResultCallback mSnapShotResultCallback);

    /**
     * Start request for the current headphone state.
     * Set a result callback interface where the result will be delivered to.
     * @param mSnapShotResultCallback
     */
    void getCurrentHeadphoneState(SnapShotResultCallback mSnapShotResultCallback);

    /**
     * Start request for the current location state.
     * Set a result callback interface where the result will be delivered to.
     * @param mSnapShotResultCallback
     */
    void getCurrentLocation(SnapShotResultCallback mSnapShotResultCallback);

    /**
     * Start request for current place & likelihood the device is located.
     * Set a result callback interface where result will be delivered to.
     * @param mSnapShotResultCallback
     */
    void getCurrentPlace(SnapShotResultCallback mSnapShotResultCallback);

    /**
     * Start request for current place & likelihood the device is located.
     * Set a result callback interface where result will be delivered to.
     * @param mSnapShotResultCallback
     */
    int WEATHER = 0;
    int PLACE = 1;
    int LOCATION = 2;
    void getCurrentWeather(SnapShotResultCallback mSnapShotResultCallback);

}
