/*
 *    Copyright 2021 Criteo
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

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_REWARDED_NATIVE_AD_UNIT_ID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeActivity extends AppCompatActivity {

  TextView statusText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_native);

    statusText = findViewById(R.id.statusText);

    loadNative();
  }

  private void loadNative() {
    statusText.setText("Native Ad is Loading");

    VideoOptions videoOptions = new VideoOptions.Builder()
        .setStartMuted(false)
        .build();

    AdLoader adLoader = new AdLoader.Builder(this, GAM_REWARDED_NATIVE_AD_UNIT_ID)
        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
          @Override
          public void onNativeAdLoaded(NativeAd nativeAd) {
            // Show the ad.
            statusText.setText("Native Ad is Loaded");
            FrameLayout frameLayout = findViewById(R.id.flNativeAd);
            displayNativeAd(frameLayout, nativeAd);
          }
        })
        .withAdListener(new AdListener() {
          @Override
          public void onAdFailedToLoad(LoadAdError adError) {
            statusText.setText("Native Ad failed to load");
            // Handle the failure by logging, altering the UI, and so on.
          }
        })
        .withNativeAdOptions(new NativeAdOptions.Builder()
            // Methods in the NativeAdOptions.Builder class can be
            // used here to specify individual options settings.
            .setVideoOptions(videoOptions)
            .build())
        .build();

    adLoader.loadAd(new AdManagerAdRequest.Builder().build()); // TODO: Enrich ad request with Criteo keyvalues
  }

  private void displayNativeAd(ViewGroup parent, NativeAd ad) {
    LayoutInflater inflater = (LayoutInflater) parent.getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    NativeAdView adView = (NativeAdView) inflater
        .inflate(R.layout.ad_native_layout, null);

    TextView headlineView = adView.findViewById(R.id.ad_headline);
    headlineView.setText(ad.getHeadline());
    adView.setHeadlineView(headlineView);

    MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
    mediaView.setMediaContent(ad.getMediaContent());
    adView.setMediaView(mediaView);

    adView.setNativeAd(ad);

    parent.removeAllViews();
    parent.addView(adView);
  }
}