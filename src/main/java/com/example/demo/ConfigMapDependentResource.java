package com.example.demo;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigMapDependentResource extends CRUDKubernetesDependentResource<ConfigMap, WebPage> {

    @Autowired
    private ExternalService externalService;

    public ConfigMapDependentResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(WebPage webPage, Context<WebPage> context) {
        //spring bean
        externalService.doNothing();
        
        Map<String, String> data = new HashMap<>();
        data.put("index.html", webPage.getSpec().getHtml());
        return new ConfigMapBuilder()
                .withMetadata(
                        new ObjectMetaBuilder()
                                .withName("test-configmap")
                                .withNamespace(webPage.getMetadata().getNamespace())
                                .build())
                .withData(data)
                .build();
    }
}
