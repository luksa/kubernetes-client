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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.fabric8.kubernetes.api.model.authentication.TokenReview;
import io.fabric8.kubernetes.api.model.authentication.TokenReviewBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.Rule;
import org.junit.Test;

public class TokenReviewTest {
  @Rule
  public KubernetesServer server = new KubernetesServer();

  @Test
  public void testCreate() {
    server.expect().withPath("/apis/authentication.k8s.io/v1beta1/tokenreviews").andReturn(201, new TokenReviewBuilder()
      .withNewSpec().withToken("t1").endSpec()
      .withNewStatus().withAuthenticated(true).endStatus()
      .build()).once();

    KubernetesClient client = server.getClient();

    TokenReview tokenReview = new TokenReviewBuilder().withNewSpec().withToken("t1").endSpec().build();
    TokenReview response = client.authentication().tokenReviews().create(tokenReview);
    assertNotNull(response);
    assertEquals("t1", response.getSpec().getToken());
    assertTrue(response.getStatus().getAuthenticated());
  }

  @Test
  public void testCreateInLine() {
    server.expect().withPath("/apis/authentication.k8s.io/v1beta1/tokenreviews").andReturn(201, new TokenReviewBuilder()
      .withNewSpec().withToken("t1").endSpec()
      .withNewStatus().withAuthenticated(true).endStatus()
      .build()).once();

    KubernetesClient client = server.getClient();

    TokenReview response = client.authentication().tokenReviews().createNew().withNewSpec().withToken("t1").endSpec().done();
    assertNotNull(response);
    assertEquals("t1", response.getSpec().getToken());
    assertTrue(response.getStatus().getAuthenticated());
  }
}
