package io.spring.start.site

import io.spring.initializr.generator.buildsystem.Build
import io.spring.initializr.generator.buildsystem.maven.MavenBuild
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency
import io.spring.initializr.generator.project.ProjectGenerationConfiguration
import io.spring.initializr.generator.spring.build.BuildCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import kotlin.streams.asSequence

@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("custom-maven-plugin")
class CustomMavenPluginConfiguration {

    @Bean
    fun customPluginConfigurer(): BuildCustomizer<MavenBuild> =
            BuildCustomizer { build ->
                build
                        .dependencies()
                        .ids()
                        .asSequence()
                        .single { it == "custom-maven-plugin" }
                        .let { build.dependencies()[it] }
                        .run {
                            build.plugin(groupId, artifactId, version.value)
                                    .execution("my-execution") {
                                        it.goal("scan")
                                                .configuration {
                                                    it.add("failOnSeverity", "MAJOR")
                                                }
                                    }
                        }
            }

    @Bean
    fun customPluginDependencyRemoval(): BuildCustomizer<Build> =
            object : BuildCustomizer<Build> {
                override fun customize(build: Build) {
                    build.dependencies().remove("custom-maven-plugin")
                }

                override fun getOrder() = Ordered.LOWEST_PRECEDENCE
            }
}