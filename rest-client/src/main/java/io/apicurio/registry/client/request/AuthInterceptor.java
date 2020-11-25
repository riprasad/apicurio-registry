/*
 * Copyright 2020 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.registry.client.request;

import io.apicurio.registry.auth.Auth;
import io.apicurio.registry.auth.BasicAuth;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private final Auth auth;

    public AuthInterceptor(Auth auth) {
        this.auth = auth;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request request = chain.request();

        String authValue = "";

        if (auth.getAuthStrategy() instanceof BasicAuth) {
            final BasicAuth basicAuth = (BasicAuth) auth.getAuthStrategy();
            authValue = Credentials.basic(basicAuth.getUsername(), basicAuth.getPassword());

        } else {
            authValue = auth.getAuthStrategy().getAuthValue();
        }

        final Headers requestHeaders = request.headers().newBuilder()
                .add("Authorization", authValue).build();

        final Request requestWithHeathers = request.newBuilder()
                .headers(requestHeaders)
                .build();

        return chain.proceed(requestWithHeathers);
    }
}