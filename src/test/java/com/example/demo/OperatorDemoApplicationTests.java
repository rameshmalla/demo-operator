package com.example.demo;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapVolumeSource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.springboot.starter.test.EnableMockOperator;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableMockOperator(
		crdPaths = "crd/webpages.sample.javaoperatorsdk-v1.yml"
)
class OperatorDemoApplicationTests {
	@Autowired
	private KubernetesClient client;

	@Test
	public void testShouldCreateConfigMapFromCustomResource() {
		// create a CR
		client.load(fetchResource("webpage.yaml"))
				.create();
		await()
				.atMost(5, TimeUnit.SECONDS)
				.untilAsserted(
						() -> assertNotNull(
								client.resources(ConfigMap.class)
										.inNamespace("local")
										.withName("test-configmap").get()));
	}
	private <T extends HasMetadata> T getResource(final String nameSpace,
												  final String name,
												  final Class<T> resourceType) {
		return client.resources(resourceType)
				.inNamespace(nameSpace)
				.withName(name).get();
	}

	private Namespace namespace(String ns) {
		return new NamespaceBuilder()
				.withMetadata(
						new ObjectMetaBuilder().withName(ns).build())
				.build();
	}

	private InputStream fetchResource(String fileName) {
		return OperatorDemoApplicationTests.class.getClassLoader().getResourceAsStream(fileName);
	}


}
