package chattylabs.assistant.demo;

import com.chattylabs.android.commons.internal.ILogger;
import com.chattylabs.android.commons.internal.ILoggerImpl;

import java.util.Locale;

import javax.inject.Singleton;

import chattylabs.conversations.AndroidSpeechRecognizer;
import chattylabs.conversations.AndroidSpeechSynthesizer;
import chattylabs.conversations.ConversationalFlow;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

public class MyApplication extends DaggerApplication {

    @dagger.Component(
            modules = {
                    AndroidSupportInjectionModule.class,
                    MyApplicationModule.class
            }
    )
    @Singleton
    interface Component extends AndroidInjector<MyApplication> {
        @dagger.Component.Builder
        abstract class Builder extends AndroidInjector.Builder<MyApplication> {}
    }

    @dagger.Module
    static abstract class MyApplicationModule {

        @dagger.Binds
        @dagger.Reusable
        abstract ILogger provideLogger(ILoggerImpl logger);

        @ContributesAndroidInjector
        abstract AssistantActivity mainActivity();

        @dagger.Provides
        @dagger.Reusable
        public static ConversationalFlow provideConversationalFlow(ILogger logger) {
            ConversationalFlow component = ConversationalFlow.provide(logger);
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
    protected AndroidInjector<MyApplication> applicationInjector() {
        return DaggerMyApplication_Component.builder().create(this);
    }
}