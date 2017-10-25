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

import io.fabric8.kubernetes.api.model.authorization.SubjectAccessReview;
import io.fabric8.kubernetes.api.model.authorization.SubjectAccessReviewBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.CreateableSubjectAccessReview;
import io.fabric8.kubernetes.client.dsl.SubjectAccessReviewOperation;
import io.fabric8.kubernetes.client.dsl.base.OperationSupport;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SubjectAccessReviewOperationImpl extends OperationSupport implements SubjectAccessReviewOperation<CreateableSubjectAccessReview> {


  public SubjectAccessReviewOperationImpl(OkHttpClient client, Config config) {
    this(client, config, "v1");
  }

  public SubjectAccessReviewOperationImpl(OkHttpClient client, Config config, String apiVersion) {
    super(client, config, "authorization.k8s.io", apiVersion, "subjectaccessreviews", null, null);
  }

  @Override
  public boolean isResourceNamespaced() {
    return false;
  }

  @Override
  public SubjectAccessReview create(SubjectAccessReview... item) {
    return new CreateableSubjectAccessReviewImpl(client).create(item);
  }

  @Override
  public CreateableSubjectAccessReview createNew() {
    return new CreateableSubjectAccessReviewImpl(client).createNew();
  }

  private class CreateableSubjectAccessReviewImpl extends CreateableSubjectAccessReview {
    private final OkHttpClient client;
    private final SubjectAccessReviewBuilder builder;

    private CreateableSubjectAccessReviewImpl(OkHttpClient client) {
      this.client = client;
      this.builder = new SubjectAccessReviewBuilder(CreateableSubjectAccessReviewImpl.this);
    }

    private CreateableSubjectAccessReviewImpl(OkHttpClient client, SubjectAccessReviewBuilder builder) {
      this.client = client;
      this.builder = builder;
    }

    @Override
    public SubjectAccessReview create(SubjectAccessReview... resources) {
      try {
        if (resources.length > 1) {
          throw new IllegalArgumentException("Too many items to create.");
        } else if (resources.length == 1) {
          return handleCreate(resources[0], SubjectAccessReview.class);
        } else {
          throw new IllegalArgumentException("Nothing to create.");
        }
      } catch (InterruptedException | ExecutionException | IOException e) {
        throw KubernetesClientException.launderThrowable(e);
      }
    }

    @Override
    public CreateableSubjectAccessReview createNew() {
      return this;
    }

    @Override
    public SubjectAccessReview done() {
      return create(builder.build());
    }
  }
}
