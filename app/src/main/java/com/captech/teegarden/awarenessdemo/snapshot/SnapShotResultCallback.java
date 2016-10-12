package com.captech.teegarden.awarenessdemo.snapshot;

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
 */

public interface SnapShotResultCallback {

    /**
     * Set the most probably current activity from the result callback also giving level of confidence.
     * @param currentActivity
     * @param confidence
     */
    void setCurrentActivity(String currentActivity, int confidence);

    /**
     * Set the current headphone state from the result callback.
     * @param headphoneState
     */
    void setCurrentHeadphoneState(String headphoneState);

    /**
     * Set the current lat/long
     * @param latitude
     * @param longitude
     */
    void setCurrentLocation(double latitude, double longitude);

    /**
     * set the most likely current place and the likelihood of the place.
     * @param place
     * @param likelihood
     */
    void setCurrentPlace(String place, float likelihood);

    /**
     * Set the current weather condition and the temperature
     * @param weatherCondition
     * @param temperature
     */
    void setCurrentWeather(String weatherCondition, float temperature);

    /**
     * Let requesting presenter know location permission has not been granted.
     */
    void setLocationPermissionRequired(int origination);

}
