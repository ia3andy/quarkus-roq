package io.quarkiverse.roq.deployment;

import static java.util.function.Predicate.not;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.quarkiverse.roq.runtime.FixedStaticPagesProvider;
import io.quarkiverse.roq.runtime.RoqGenerator;
import io.quarkiverse.roq.runtime.RoqRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.vertx.http.deployment.devmode.NotFoundPageDisplayableEndpointBuildItem;
import io.quarkus.vertx.http.deployment.spi.StaticResourcesBuildItem;

class RoqProcessor {

    private static final String FEATURE = "roq";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void produceBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanProducer) {
        additionalBeanProducer.produce(AdditionalBeanBuildItem.unremovableOf(RoqGenerator.class));
        additionalBeanProducer.produce(AdditionalBeanBuildItem.unremovableOf(FixedStaticPagesProvider.class));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void initHandler(List<NotFoundPageDisplayableEndpointBuildItem> notFoundPageDisplayableEndpoints,
            StaticResourcesBuildItem staticResourcesBuildItem,
            OutputTargetBuildItem outputTarget,
            RoqRecorder recorder) {
        Set<String> staticPaths = new HashSet<>();
        if (staticResourcesBuildItem != null) {
            staticPaths.addAll(staticResourcesBuildItem.getPaths());
        }
        if (notFoundPageDisplayableEndpoints != null) {
            staticPaths.addAll(notFoundPageDisplayableEndpoints.stream()
                    .filter(not(NotFoundPageDisplayableEndpointBuildItem::isAbsolutePath))
                    .map(NotFoundPageDisplayableEndpointBuildItem::getEndpoint)
                    .toList());
        }
        recorder.setStaticPaths(staticPaths);
        recorder.setOutputTarget(outputTarget.getOutputDirectory().toAbsolutePath().toString());
    }

}
