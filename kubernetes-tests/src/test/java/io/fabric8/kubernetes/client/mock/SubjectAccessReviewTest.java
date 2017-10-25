/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fabric8.kubernetes.client.mock;

import io.fabric8.kubernetes.api.model.authorization.SubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.SubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubjectAccessReviewTest {
  @Rule
  public KubernetesServer server = new KubernetesServer();

  @Test
  public void testCreate() {
    server.expect().withPath("/apis/authorization.k8s.io/v1/subjectaccessreviews").andReturn(201, new SubjectAccessReviewBuilder()
      .withNewSpec().withUser("foo").endSpec()
      .withNewStatus().withAllowed(true).endStatus()
      .build()).once();

    KubernetesClient client = server.getClient();

    SubjectAccessReview subjectAccessReview = new SubjectAccessReviewBuilder()
      .withNewSpec()
      .withUser("foo")
      .withNewNonResourceAttributes().withPath("/enmasse-broker").withVerb("post").endNonResourceAttributes()
      .endSpec().build();
    SubjectAccessReview response = client.authorization().subjectAccessReviews().create(subjectAccessReview);
    assertNotNull(response);
    assertEquals("foo", response.getSpec().getUser());
    assertTrue(response.getStatus().getAllowed());
  }

  @Test
  public void testCreateInLine() {
    server.expect().withPath("/apis/authorization.k8s.io/v1/subjectaccessreviews").andReturn(201, new SubjectAccessReviewBuilder()
      .withNewSpec().withUser("foo").endSpec()
      .withNewStatus().withAllowed(true).endStatus()
      .build()).once();

    KubernetesClient client = server.getClient();

    SubjectAccessReview response = client.authorization().subjectAccessReviews()
      .createNew()
      .withNewSpec()
      .withUser("foo")
      .withNewNonResourceAttributes().withPath("/enmasse-broker").withVerb("post").endNonResourceAttributes()
      .endSpec()
      .done();
    assertNotNull(response);
    assertEquals("foo", response.getSpec().getUser());
    assertTrue(response.getStatus().getAllowed());
  }
}
