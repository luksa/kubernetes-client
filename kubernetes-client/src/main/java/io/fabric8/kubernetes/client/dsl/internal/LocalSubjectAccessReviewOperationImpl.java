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

import io.fabric8.kubernetes.api.model.authorization.LocalSubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.LocalSubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.CreateableLocalSubjectAccessReview;
import io.fabric8.kubernetes.client.dsl.LocalSubjectAccessReviewOperation;
import io.fabric8.kubernetes.client.dsl.base.OperationSupport;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LocalSubjectAccessReviewOperationImpl extends OperationSupport implements LocalSubjectAccessReviewOperation<CreateableLocalSubjectAccessReview> {


  public LocalSubjectAccessReviewOperationImpl(OkHttpClient client, Config config) {
    this(client, config, "v1", null);
  }

  public LocalSubjectAccessReviewOperationImpl(OkHttpClient client, Config config, String apiVersion, String namespace) {
    super(client, config, "authorization.k8s.io", apiVersion, "subjectaccessreviews", namespace, null);
  }

  @Override
  public LocalSubjectAccessReview create(LocalSubjectAccessReview... item) {
    return new CreateableLocalSubjectAccessReviewImpl(client).create(item);
  }

  @Override
  public CreateableLocalSubjectAccessReview createNew() {
    return new CreateableLocalSubjectAccessReviewImpl(client).createNew();
  }

  private class CreateableLocalSubjectAccessReviewImpl extends CreateableLocalSubjectAccessReview {
    private final OkHttpClient client;
    private final LocalSubjectAccessReviewBuilder builder;

    private CreateableLocalSubjectAccessReviewImpl(OkHttpClient client) {
      this.client = client;
      this.builder = new LocalSubjectAccessReviewBuilder(CreateableLocalSubjectAccessReviewImpl.this);
    }

    private CreateableLocalSubjectAccessReviewImpl(OkHttpClient client, LocalSubjectAccessReviewBuilder builder) {
      this.client = client;
      this.builder = builder;
    }

    @Override
    public LocalSubjectAccessReview create(LocalSubjectAccessReview... resources) {
      try {
        if (resources.length > 1) {
          throw new IllegalArgumentException("Too many items to create.");
        } else if (resources.length == 1) {
          return handleCreate(resources[0], LocalSubjectAccessReview.class);
        } else {
          throw new IllegalArgumentException("Nothing to create.");
        }
      } catch (InterruptedException | ExecutionException | IOException e) {
        throw KubernetesClientException.launderThrowable(e);
      }
    }

    @Override
    public CreateableLocalSubjectAccessReview createNew() {
      return this;
    }

    @Override
    public LocalSubjectAccessReview done() {
      return create(builder.build());
    }
  }
}
