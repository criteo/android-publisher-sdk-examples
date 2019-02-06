package com.criteo.pubsdk;

import android.content.Context;
import android.os.AsyncTask;

import com.criteo.pubsdk.Util.AppEventResponseListener;
import com.criteo.pubsdk.Util.DeviceUtil;
import com.criteo.pubsdk.cache.SdkCache;
import com.criteo.pubsdk.model.AdUnit;
import com.criteo.pubsdk.model.Publisher;
import com.criteo.pubsdk.model.Slot;
import com.criteo.pubsdk.model.User;
import com.criteo.pubsdk.network.AppEventTask;
import com.criteo.pubsdk.network.CdbDownloadTask;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;

import java.util.ArrayList;
import java.util.List;

class BidManager implements AppEventResponseListener {
    private static final String CRT_CPM = "crt_cpm";
    private static final String CRT_DISPLAY_URL = "crt_displayUrl";
    private static final int PROFILE_ID = 235;
    private List<AdUnit> adUnits;
    private Context mContext;
    private CdbDownloadTask cdbDownloadTask;
    private AppEventTask eventTask;
    private SdkCache cache;
    private Publisher publisher;
    private User user;
    private int appEventThrottle = -1;
    private long throttleSetTime = 0;

    BidManager(Context context, int networkId, List<AdUnit> adUnits) {
        this.mContext = context;
        this.adUnits = adUnits;
        this.cache = new SdkCache();
        publisher = new Publisher(mContext);
        publisher.setNetworkId(networkId);
        cdbDownloadTask = new CdbDownloadTask(context, this.cache, true, DeviceUtil.getUserAgent(mContext));
        user = new User(mContext);
        eventTask = new AppEventTask(context, this);
    }

    void prefetch() {
        if (cdbDownloadTask.getStatus() != AsyncTask.Status.RUNNING
                && cdbDownloadTask.getStatus() != AsyncTask.Status.FINISHED) {
            cdbDownloadTask.execute(PROFILE_ID, user, publisher, adUnits);
        }
    }

    void cancelLoad() {
        if (cdbDownloadTask.getStatus() == AsyncTask.Status.RUNNING) {
            cdbDownloadTask.cancel(true);
        }
        if (eventTask.getStatus() == AsyncTask.Status.RUNNING) {
            eventTask.cancel(true);
        }
    }

    void postAppEvent(String eventType) {
        if (appEventThrottle > 0 &&
                System.currentTimeMillis() - throttleSetTime < appEventThrottle * 1000) {
            return;
        }
        if (eventTask.getStatus() == AsyncTask.Status.FINISHED) {
            eventTask = new AppEventTask(mContext, this);
        }
        if (eventTask.getStatus() != AsyncTask.Status.RUNNING) {
            eventTask.execute(eventType);
        }

    }

    PublisherAdRequest.Builder enrichBid(PublisherAdRequest.Builder request, AdUnit adUnit) {
        Slot slot = cache.getAdUnit(adUnit.getPlacementId(),
                adUnit.getSize().getWidth(), adUnit.getSize().getHeight());
        if (slot != null) {
            request.addCustomTargeting(CRT_CPM, String.valueOf(slot.getCpm()));
            request.addCustomTargeting(CRT_DISPLAY_URL, DeviceUtil.createDfpCompatibleDisplayUrl(slot.getDisplayUrl()));
        }
        if (cdbDownloadTask.getStatus() != AsyncTask.Status.RUNNING) {
            cdbDownloadTask = new CdbDownloadTask(mContext, this.cache, false, DeviceUtil.getUserAgent(mContext));
            List<AdUnit> adUnits = new ArrayList<AdUnit>();
            adUnits.add(adUnit);
            cdbDownloadTask.execute(PROFILE_ID, user, publisher, adUnits);
        }
        return request;
    }

    @Override
    public void setThreshold(int throttle) {
        this.appEventThrottle = throttle;
        this.throttleSetTime = System.currentTimeMillis();
    }
}