package com.criteo.publisher.samples.appbidding_googleadmanager;

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_BANNER_AD_UNIT_ID;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.Criteo;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class BannerActivity extends AppCompatActivity {

  private PublisherAdView publisherAdView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    publisherAdView = new PublisherAdView(this);
    publisherAdView.setAdSizes(AdSize.BANNER);
    publisherAdView.setAdUnitId(GAM_BANNER_AD_UNIT_ID);
    FrameLayout publisherAdViewContainer = findViewById(R.id.publisherAdViewContainer);
    publisherAdViewContainer.addView(publisherAdView);

    displayBanner();
  }

  @Override
  protected void onDestroy() {
    publisherAdView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();

    Criteo.getInstance().setBidsForAdUnit(builder, CRITEO_BANNER_AD_UNIT);

    PublisherAdRequest adRequest = builder.build();
    publisherAdView.loadAd(adRequest);
  }
}
