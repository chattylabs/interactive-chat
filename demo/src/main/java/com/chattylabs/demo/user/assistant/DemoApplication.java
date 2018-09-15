package com.chattylabs.demo.user.assistant;

import com.chattylabs.android.commons.internal.ILogger;
import com.chattylabs.android.commons.internal.ILoggerImpl;
import com.chattylabs.sdk.android.voice.AndroidSpeechRecognizer;
import com.chattylabs.sdk.android.voice.AndroidSpeechSynthesizer;
import com.chattylabs.sdk.android.voice.ConversationalFlowComponent;
import com.chattylabs.sdk.android.voice.ConversationalFlowModule;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

public class DemoApplication extends DaggerApplication {

    @dagger.Component(
            modules = {
                    AndroidSupportInjectionModule.class,
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

        @dagger.Provides
        @dagger.Reusable
        public static ConversationalFlowComponent provideConversationalFlow(ILogger logger) {
            ConversationalFlowComponent component = ConversationalFlowModule.provideComponent(logger);
            component.updateConfiguration(builder -> {
                builder.setSynthesizerServiceType(() -> AndroidSpeechSynthesizer.class);
                builder.setRecognizerServiceType(() -> AndroidSpeechRecognizer.class);
                builder.setSpeechLanguage(() -> Locale.ENGLISH);
                return builder.build();
            });
            return component;
        }
    }

    @Override
    protected AndroidInjector<DemoApplication> applicationInjector() {
        return DaggerDemoApplication_Component.builder().create(this);
    }
}