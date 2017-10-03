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

package io.fabric8.kubernetes.client.dsl.internal;

import io.fabric8.kubernetes.api.model.authentication.TokenReview;
import io.fabric8.kubernetes.api.model.authentication.TokenReviewBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.CreateableTokenReview;
import io.fabric8.kubernetes.client.dsl.TokenReviewOperation;
import io.fabric8.kubernetes.client.dsl.base.OperationSupport;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TokenReviewOperationImpl extends OperationSupport implements TokenReviewOperation<CreateableTokenReview> {


  public TokenReviewOperationImpl(OkHttpClient client, Config config) {
    this(client, config, "v1beta1");
  }

  public TokenReviewOperationImpl(OkHttpClient client, Config config, String apiVersion) {
    super(client, config, "authentication.k8s.io", apiVersion, "tokenreviews", null, null);
  }

  @Override
  public boolean isResourceNamespaced() {
    return false;
  }

  @Override
  public TokenReview create(TokenReview... item) {
    return new CreateableTokenReviewImpl(client).create(item);
  }

  @Override
  public CreateableTokenReview createNew() {
    return new CreateableTokenReviewImpl(client).createNew();
  }

  private class CreateableTokenReviewImpl extends CreateableTokenReview {
    private final OkHttpClient client;
    private final TokenReviewBuilder builder;

    private CreateableTokenReviewImpl(OkHttpClient client) {
      this.client = client;
      this.builder = new TokenReviewBuilder(CreateableTokenReviewImpl.this);
    }

    private CreateableTokenReviewImpl(OkHttpClient client, TokenReviewBuilder builder) {
      this.client = client;
      this.builder = builder;
    }

    @Override
    public TokenReview create(TokenReview... resources) {
      try {
        if (resources.length > 1) {
          throw new IllegalArgumentException("Too many items to create.");
        } else if (resources.length == 1) {
          return handleCreate(resources[0], TokenReview.class);
        } else {
          throw new IllegalArgumentException("Nothing to create.");
        }
      } catch (InterruptedException | ExecutionException | IOException e) {
        throw KubernetesClientException.launderThrowable(e);
      }
    }

    @Override
    public CreateableTokenReview createNew() {
      return this;
    }

    @Override
    public TokenReview done() {
      return create(builder.build());
    }
  }
}
