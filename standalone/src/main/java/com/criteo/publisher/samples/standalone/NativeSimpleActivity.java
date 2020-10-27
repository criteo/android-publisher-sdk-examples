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

package com.criteo.publisher.samples.standalone;

import static com.criteo.publisher.samples.standalone.CriteoSampleApplication.CRITEO_NATIVE_AD_UNIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.criteo.publisher.CriteoErrorCode;
import com.criteo.publisher.advancednative.CriteoMediaView;
import com.criteo.publisher.advancednative.CriteoNativeAd;
import com.criteo.publisher.advancednative.CriteoNativeAdListener;
import com.criteo.publisher.advancednative.CriteoNativeLoader;
import com.criteo.publisher.advancednative.CriteoNativeRenderer;
import com.criteo.publisher.advancednative.RendererHelper;

public class NativeSimpleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_native_simple);

    CriteoNativeLoader nativeLoader = new CriteoNativeLoader(CRITEO_NATIVE_AD_UNIT,
        new MyNativeAdListener(), new MyNativeRenderer());
    nativeLoader.loadAd();
  }

  private class MyNativeAdListener implements CriteoNativeAdListener {

    @Override
    public void onAdReceived(CriteoNativeAd nativeAd) {
      // called when Criteo Ad is received and ready to be displayed
      FrameLayout nativeAdContainer = findViewById(R.id.criteoNativeAdContainer);
      View nativeView = nativeAd.createNativeRenderedView(getBaseContext(), nativeAdContainer);
      nativeAdContainer.removeAllViews();
      nativeAdContainer.addView(nativeView);
    }

    @Override
    public void onAdFailedToReceive(CriteoErrorCode errorCode) {
      // called when Criteo returns no ad. The error description gives further details on the failure.
    }

    @Override
    public void onAdImpression() {
      // called when native view is visible on the screen and impression is registered
    }

    @Override
    public void onAdClicked() {
      // called when user clicks on Criteo ad
    }

    @Override
    public void onAdLeftApplication() {
      // called when clicking on Criteo ad result in user leaving your application
    }

    @Override
    public void onAdClosed() {
      // called when Criteo ad is closed
    }
  }

  private static class MyNativeRenderer implements CriteoNativeRenderer {

    @Override
    public View createNativeView(Context context, ViewGroup viewGroup) {
      return LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false);
    }

    @Override
    public void renderNativeView(RendererHelper rendererHelper, View view, CriteoNativeAd criteoNativeAd) {
      view.<TextView>findViewById(R.id.ad_title)
          .setText(criteoNativeAd.getTitle());
      view.<TextView>findViewById(R.id.ad_description)
          .setText(criteoNativeAd.getDescription());
      view.<TextView>findViewById(R.id.ad_attribution)
          .setText(String.format("Ads by %s", criteoNativeAd.getAdvertiserDomain()));
      view.<Button>findViewById(R.id.ad_calltoaction)
          .setText(criteoNativeAd.getCallToAction());

      rendererHelper.setMediaInView(criteoNativeAd.getProductMedia(),
          view.<CriteoMediaView>findViewById(R.id.ad_media));
    }
  }
}
