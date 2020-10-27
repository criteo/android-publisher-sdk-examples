package com.criteo.publisher.samples.standalone;

import static com.criteo.publisher.samples.standalone.CriteoSampleApplication.CRITEO_NATIVE_AD_UNIT;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.criteo.publisher.CriteoErrorCode;
import com.criteo.publisher.advancednative.CriteoMediaView;
import com.criteo.publisher.advancednative.CriteoNativeAd;
import com.criteo.publisher.advancednative.CriteoNativeAdListener;
import com.criteo.publisher.advancednative.CriteoNativeLoader;
import com.criteo.publisher.advancednative.CriteoNativeRenderer;
import com.criteo.publisher.advancednative.RendererHelper;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class NativeRecyclerViewActivity extends AppCompatActivity {

  private MyAdapter adapter;
  private CriteoNativeLoader nativeLoader;

  private static final int DUMMY_CONTENT_COUNT = 12;
  private static final int NATIVE_AD_POSITION = 3; // zero-indexed position

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_native_recycler_view);

    adapter = new MyAdapter();
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    // add some dummy content
    for (int i = 0; i < DUMMY_CONTENT_COUNT; i++) {
      adapter.addContent(new ContentItem(
          String.valueOf(i + 1) + ". Lorem ipsum dolor sit amet",
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed elementum augue dignissim mi ullamcorper cursus. Nulla varius vulputate elit. Nullam.",
          "https://img.icons8.com/cotton/2x/news.png"));
    }

    // add Criteo native ad
    nativeLoader = new CriteoNativeLoader(CRITEO_NATIVE_AD_UNIT,
        new MyNativeAdListener(), new MyNativeRenderer());
    nativeLoader.loadAd();
  }

  private class MyNativeAdListener implements CriteoNativeAdListener {

    @Override
    public void onAdReceived(@NonNull CriteoNativeAd nativeAd) {
      adapter.addCriteoNativeAd(NATIVE_AD_POSITION, nativeAd);
    }

    @Override
    public void onAdFailedToReceive(@NonNull CriteoErrorCode errorCode) {

    }

    @Override
    public void onAdImpression() {

    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdLeftApplication() {

    }

    @Override
    public void onAdClosed() {

    }
  }

  private static class MyNativeRenderer implements CriteoNativeRenderer {

    @Override
    public View createNativeView(@NonNull Context context, @Nullable ViewGroup viewGroup) {
      return LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false);
    }

    @Override
    public void renderNativeView(@NonNull RendererHelper rendererHelper, @NonNull View view,
        @NonNull CriteoNativeAd criteoNativeAd) {
      view.<TextView>findViewById(R.id.ad_title).setText(criteoNativeAd.getTitle());
      view.<TextView>findViewById(R.id.ad_description).setText(criteoNativeAd.getDescription());
      view.<TextView>findViewById(R.id.ad_attribution)
          .setText(String.format("Ads by %s", criteoNativeAd.getAdvertiserDomain()));
      view.<Button>findViewById(R.id.ad_calltoaction).setText(criteoNativeAd.getCallToAction());

      rendererHelper.setMediaInView(criteoNativeAd.getProductMedia(),
          view.<CriteoMediaView>findViewById(R.id.ad_media));
    }
  }

  class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int CONTENT = 1;
    private static final int CRITEO_AD = 2;

    private final List<Object> dataset = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
      if (dataset.get(position) instanceof CriteoNativeAd) {
        return CRITEO_AD;
      }
      return CONTENT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if (viewType == CRITEO_AD) {
        return new ViewHolder(nativeLoader.createEmptyNativeView(parent.getContext(), parent)) {};
      } else {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false)) {};
      }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      int viewType = getItemViewType(position);
      if (viewType == CRITEO_AD) {
        CriteoNativeAd criteoNativeAd = (CriteoNativeAd) dataset.get(position);
        criteoNativeAd.renderNativeView(holder.itemView);
      } else {
        ContentItem contentItem = (ContentItem) dataset.get(position);
        ((TextView) holder.itemView.findViewById(R.id.content_title)).setText(contentItem.getTitle());
        ((TextView) holder.itemView.findViewById(R.id.content_description))
            .setText(contentItem.getDescription());
        Picasso.get().load(contentItem.getImageUrl())
            .into((ImageView) holder.itemView.findViewById(R.id.content_image));
      }
    }

    @Override
    public int getItemCount() {
      return dataset.size();
    }

    void addCriteoNativeAd(int position, CriteoNativeAd nativeAd) {
      dataset.add(position, nativeAd);
      notifyItemInserted(position);
    }

    void addContent(ContentItem contentItem) {
      dataset.add(contentItem);
      notifyItemInserted(dataset.size() - 1);
    }

  }
}
