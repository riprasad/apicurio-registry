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

package io.apicurio.registry.auth;

import io.apicurio.registry.auth.config.ClientCredentialsConfig;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;


/**
 * @author carnalca@redhat.com
 */
public class KeycloakAuth implements AuthStrategy {

    public static final String BEARER = "Bearer ";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private final Keycloak keycloak;

    public KeycloakAuth(ClientCredentialsConfig config) {

        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(config.getServerUrl())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .grantType(CLIENT_CREDENTIALS)
                .build();
    }

    @Override
    public String getAuthValue() {
        return BEARER + this.keycloak.tokenManager().getAccessToken().getToken();
    }
}