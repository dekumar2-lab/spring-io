package io.spring.start.site

import io.spring.initializr.metadata.InitializrMetadataBuilder
import io.spring.initializr.metadata.InitializrProperties
import io.spring.initializr.web.support.DefaultInitializrMetadataProvider
import io.spring.initializr.web.support.InitializrMetadataUpdateStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CustomInitializrProperties::class)
class CustomInitializrConfiguration {

    @Bean
    fun customInitializrMetadataProvider(initializrProperties: InitializrProperties,
                                         customInitializrProperties: CustomInitializrProperties,
                                         initializrMetadataUpdateStrategy: InitializrMetadataUpdateStrategy) =
            InitializrMetadataBuilder
                    .fromInitializrProperties(customInitializrProperties.initializr)
                    .withInitializrProperties(initializrProperties, true)
                    .build()
                    .let { DefaultInitializrMetadataProvider(it, initializrMetadataUpdateStrategy) }
}

@ConfigurationProperties("custom")
class CustomInitializrProperties {

    @NestedConfigurationProperty
    var initializr = InitializrProperties()
}

