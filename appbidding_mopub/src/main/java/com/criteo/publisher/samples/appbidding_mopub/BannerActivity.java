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

package com.criteo.publisher.samples.appbidding_mopub;

import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_BANNER_AD_UNIT_ID;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.Bid;
import com.criteo.publisher.BidResponseListener;
import com.criteo.publisher.Criteo;
import com.criteo.publisher.model.AdUnit;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;
import com.mopub.mobileads.MoPubView.MoPubAdSize;

public class BannerActivity extends AppCompatActivity {

  private MoPubView moPubView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    moPubView = (MoPubView)findViewById(R.id.mopubView);
    moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
    moPubView.setAdSize(MoPubAdSize.HEIGHT_50);
    moPubView.setBannerAdListener(new BannerAdListener() {
      @Override
      public void onBannerLoaded(@NonNull MoPubView banner) {
        refreshCriteoBids(banner, CRITEO_BANNER_AD_UNIT);
      }

      @Override
      public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        refreshCriteoBids(banner, CRITEO_BANNER_AD_UNIT);
      }

      @Override
      public void onBannerClicked(MoPubView banner) {

      }

      @Override
      public void onBannerExpanded(MoPubView banner) {

      }

      @Override
      public void onBannerCollapsed(MoPubView banner) {

      }
    });

    displayBanner();
  }

  /**
   * Make sure Criteo bids are refreshed when Banner ad unit is auto-refreshing.
   * This method should be called from MoPub's onBannerLoaded and onBannerFailed listener
   * For more information, please refer to https://publisherdocs.criteotilt.com/app/android/app-bidding/mopub/#handle-auto-refresh
   *
   * @param banner MoPubView object returned by the listener
   * @param adUnit Criteo Ad Unit object corresponds to this Banner
   */
  private void refreshCriteoBids(MoPubView banner, AdUnit adUnit) {
    // append new keywords, if available
    Criteo.getInstance().loadBid(adUnit, new BidResponseListener() {
      @Override
      public void onResponse(@Nullable Bid bid) {
        if (bid != null) {
          Criteo.getInstance().enrichAdObjectWithBid(banner, bid);
        }
      }
    });
  }

  @Override
  protected void onDestroy() {
    moPubView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    Criteo.getInstance().loadBid(CRITEO_BANNER_AD_UNIT, new BidResponseListener() {
      @Override
      public void onResponse(@Nullable Bid bid) {
        if (bid != null) {
          Criteo.getInstance().enrichAdObjectWithBid(moPubView, bid);
        }
        moPubView.loadAd();
      }
    });
  }

}
