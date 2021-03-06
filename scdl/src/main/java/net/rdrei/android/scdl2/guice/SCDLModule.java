package net.rdrei.android.scdl2.guice;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.pm.PackageManager;

import com.android.vending.billing.IabHelper;
import com.google.android.gms.analytics.Tracker;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.squareup.otto.Bus;

import net.rdrei.android.scdl2.PreferenceManagerWrapper;
import net.rdrei.android.scdl2.PreferenceManagerWrapperFactory;
import net.rdrei.android.scdl2.PreferenceManagerWrapperImpl;
import net.rdrei.android.scdl2.TrackDownloader;
import net.rdrei.android.scdl2.TrackDownloaderFactory;
import net.rdrei.android.scdl2.TrackDownloaderImpl;
import net.rdrei.android.scdl2.api.URLConnectionFactory;
import net.rdrei.android.scdl2.api.URLConnectionFactoryImpl;
import net.rdrei.android.scdl2.api.URLWrapper;
import net.rdrei.android.scdl2.api.URLWrapperFactory;
import net.rdrei.android.scdl2.api.URLWrapperImpl;
import net.rdrei.android.scdl2.ui.DownloadPreferencesDelegate;
import net.rdrei.android.scdl2.ui.DownloadPreferencesDelegateFactory;
import net.rdrei.android.scdl2.ui.DownloadPreferencesDelegateImpl;

import org.thoughtcrime.ssl.pinning.PinningTrustManager;

public class SCDLModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(URLConnectionFactory.class).to(URLConnectionFactoryImpl.class);
		bind(PinningTrustManager.class).toProvider(PinningTrustManagerProvider.class);
		bind(DownloadManager.class).toProvider(DownloadManagerProvider.class);
		bind(ActionBar.class).toProvider(ActionBarProvider.class);
		bind(IabHelper.class).toProvider(IabHelperProvider.class);
		bind(PackageManager.class).toProvider(PackageManagerProvider.class);

		// Share one instance of the Bus across the application.
		// If I ever need more than one, I could just use a Named() annotation.
		bind(Bus.class).toProvider(BusProvider.class).in(Singleton.class);
		// This must be a Singleton. Google advices to handle the creation in the Application.
		bind(Tracker.class).toProvider(TrackerProvider.class).in(Singleton.class);

		install(new FactoryModuleBuilder().implement(URLWrapper.class, URLWrapperImpl.class)
				.build(URLWrapperFactory.class));

		install(new FactoryModuleBuilder().implement(TrackDownloader.class,
				TrackDownloaderImpl.class).build(TrackDownloaderFactory.class));

		install(new FactoryModuleBuilder().implement(DownloadPreferencesDelegate.class,
				DownloadPreferencesDelegateImpl.class)
				.build(DownloadPreferencesDelegateFactory.class));

		install(new FactoryModuleBuilder().implement(PreferenceManagerWrapper.class,
				PreferenceManagerWrapperImpl.class).build(PreferenceManagerWrapperFactory.class));
	}
}
