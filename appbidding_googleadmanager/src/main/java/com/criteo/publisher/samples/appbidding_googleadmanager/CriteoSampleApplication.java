/*
 *    Copyright 2020 Criteo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.criteo.publisher.samples.appbidding_googleadmanager;

import android.app.Application;
import android.util.Log;
import com.criteo.publisher.Criteo;
import com.criteo.publisher.CriteoInitException;
import com.criteo.publisher.model.AdSize;
import com.criteo.publisher.model.AdUnit;
import com.criteo.publisher.model.BannerAdUnit;
import com.criteo.publisher.model.InterstitialAdUnit;
import java.util.ArrayList;
import java.util.List;

public class CriteoSampleApplication extends Application {

  // Here we use Google Ad Manager test Ad Unit ID in order to demonstrate the integration
  // If you'd like to see Criteo banner in action, you need to use your own Ad Unit IDs, with Criteo line items targeting the Ad Units.
  // To setup Criteo line items, please refer to https://publisherdocs.criteotilt.com/app/android/app-bidding/google-ad-manager/#ad-server-setup-banner-and-interstitial-
  static final String GAM_INTERSTITIAL_AD_UNIT_ID = "/140800857/AppHBTest_VideoTest";
  static final String GAM_REWARDED_AD_UNIT_ID = "/140800857/AppHBTest_VideoTest";

  // This is a test Criteo Publisher ID that works for this sample app
  // In your application, update this value with your own Criteo Publisher ID
  private static final String CRITEO_PUBLISHER_ID = "B-056946";

  // Here we use test Criteo Ad Unit IDs in order to return test ads
  // In your application, Criteo Ad Unit IDs are typically the same as your Google Ad Manager's Ad Unit ID
  private static final String CRITEO_INTERSTITIAL_AD_UNIT_ID = GAM_INTERSTITIAL_AD_UNIT_ID;

  static final InterstitialAdUnit CRITEO_INTERSTITIAL_AD_UNIT = new InterstitialAdUnit(CRITEO_INTERSTITIAL_AD_UNIT_ID);

  @Override
  public void onCreate() {
    super.onCreate();

    List<AdUnit> criteoAdUnits = new ArrayList<>();
    criteoAdUnits.add(CRITEO_INTERSTITIAL_AD_UNIT);

    try {
      new Criteo.Builder(this, CRITEO_PUBLISHER_ID)
          .adUnits(criteoAdUnits)
          .init();
    } catch (CriteoInitException e) {
      Log.d("Ads", "Failed to initialize Criteo SDK", e);
    }
  }
}
