package com.chattylabs.demo.user.assistant;

import com.chattylabs.sdk.android.common.internal.ILogger;
import com.chattylabs.sdk.android.common.internal.ILoggerImpl;
import com.chattylabs.sdk.android.voice.VoiceInteractionModule;

import javax.inject.Singleton;

import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

public class DemoApplication extends DaggerApplication {

    @dagger.Component(
            modules = {
                    AndroidSupportInjectionModule.class,
                    VoiceInteractionModule.class,
                    DemoModule.class
            }
    )
    @Singleton
    interface Component extends AndroidInjector<DemoApplication> {
        @dagger.Component.Builder
        abstract class Builder extends AndroidInjector.Builder<DemoApplication> {}
    }

    @dagger.Module
    static abstract class DemoModule {

        @dagger.Binds
        @dagger.Reusable
        abstract ILogger provideLogger(ILoggerImpl logger);

        @ContributesAndroidInjector
        abstract MainActivity mainActivity();
    }

    @Override
    protected AndroidInjector<DemoApplication> applicationInjector() {
        return DaggerDemoApplication_Component.builder().create(this);
    }
}