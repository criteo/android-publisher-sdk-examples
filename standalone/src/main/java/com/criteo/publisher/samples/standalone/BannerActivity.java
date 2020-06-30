package com.criteo.publisher.samples.standalone;

import static com.criteo.publisher.samples.standalone.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.CriteoBannerView;

public class BannerActivity extends AppCompatActivity {

  private CriteoBannerView bannerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    bannerView = new CriteoBannerView(this.getBaseContext(), CRITEO_BANNER_AD_UNIT);

    FrameLayout criteoBannerViewContainer = findViewById(R.id.criteoBannerViewContainer);
    criteoBannerViewContainer.addView(bannerView);

    displayBanner();
  }


  @Override
  protected void onDestroy() {
    bannerView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    // Load the bannerView with ad.
    bannerView.loadAd();
  }

}
