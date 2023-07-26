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

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_BANNER_AD_UNIT_ID;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.criteo.publisher.Bid;
import com.criteo.publisher.BidResponseListener;
import com.criteo.publisher.Criteo;
import com.criteo.publisher.model.BannerAdUnit;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import java.util.logging.Logger;

public class BannerActivity extends AppCompatActivity {

  private AdManagerAdView adManagerAdView;
  private FrameLayout publisherAdViewContainer;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    publisherAdViewContainer = findViewById(R.id.publisherAdViewContainer);

    AdSize adSize = getAdSize();

    adManagerAdView = new AdManagerAdView(this);
    adManagerAdView.setAdSizes(adSize, AdSize.BANNER);
    adManagerAdView.setAdUnitId(GAM_BANNER_AD_UNIT_ID);
    publisherAdViewContainer.addView(adManagerAdView);

    // for standard banners
    //displayBanner(CRITEO_BANNER_AD_UNIT);

    // for adaptive banners
    displayBanner(getCriteoBannerAdUnitFromGAMAdaptiveBanner(
            CRITEO_BANNER_AD_UNIT.getAdUnitId(),
            adSize));
  }

  // Determine the screen width (less decorations) to use for the ad width.
  private AdSize getAdSize() {
    WindowMetrics windowMetrics = null;
    Rect bounds = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
      windowMetrics = getWindowManager().getCurrentWindowMetrics();
      bounds = windowMetrics.getBounds();
    }

    float adWidthPixels = publisherAdViewContainer.getWidth();

    // If the ad hasn't been laid out, default to the full screen width.
    if (adWidthPixels == 0f) {
      adWidthPixels = bounds.width();
    }

    float density = getResources().getDisplayMetrics().density;
    int adWidth = (int) (adWidthPixels / density);

    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
  }

  @Override
  protected void onDestroy() {
    adManagerAdView.destroy();
    super.onDestroy();
  }

  // Dynamically create an instance of Criteo BannerAdUnit based on GAM adaptive banner size
  // All of these 4 sizes must be defined in GAM line items: 300x50, 320x50, 300x100, 320x100
  private BannerAdUnit getCriteoBannerAdUnitFromGAMAdaptiveBanner(String adUnitId, AdSize adSize) {
    int width = adSize.getWidth() >= 320 ? 320 : adSize.getWidth() >= 300 ? 300 : adSize.getWidth();
    int height = adSize.getHeight() >= 100 ? 100 : adSize.getHeight() >= 50 ? 50 : adSize.getHeight();
    BannerAdUnit bannerAdUnit = new BannerAdUnit(adUnitId,
            new com.criteo.publisher.model.AdSize(width, height));
    return bannerAdUnit;
  }

  private void displayBanner(BannerAdUnit bannerAdUnit) {
    Criteo.getInstance().loadBid(bannerAdUnit, new BidResponseListener() {
      @Override
      public void onResponse(@Nullable Bid bid) {
        AdManagerAdRequest.Builder builder = new AdManagerAdRequest.Builder();

        if (bid != null) {
          Criteo.getInstance().enrichAdObjectWithBid(builder, bid);
        }

        AdManagerAdRequest request = builder.build();
        adManagerAdView.loadAd(request);
      }
    });
  }
}
