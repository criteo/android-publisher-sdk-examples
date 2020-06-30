package com.criteo.publisher.samples.standalone;

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

  // This is a test Criteo Publisher ID that works for this sample app
  // In your application, update this value with your own Criteo Publisher ID
  private static final String CRITEO_PUBLISHER_ID = "B-000000";

  // Here we use test Criteo Ad Unit IDs in order to return test ads
  private static final String CRITEO_BANNER_AD_UNIT_ID = "30s6zt3ayypfyemwjvmp";
  private static final String CRITEO_INTERSTITIAL_AD_UNIT_ID = "6yws53jyfjgoq1ghnuqb";

  static final BannerAdUnit CRITEO_BANNER_AD_UNIT = new BannerAdUnit(CRITEO_BANNER_AD_UNIT_ID,
      new AdSize(320, 50));
  static final InterstitialAdUnit CRITEO_INTERSTITIAL_AD_UNIT = new InterstitialAdUnit(CRITEO_INTERSTITIAL_AD_UNIT_ID);

  @Override
  public void onCreate() {
    super.onCreate();

    List<AdUnit> criteoAdUnits = new ArrayList<>();
    criteoAdUnits.add(CRITEO_BANNER_AD_UNIT);
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
