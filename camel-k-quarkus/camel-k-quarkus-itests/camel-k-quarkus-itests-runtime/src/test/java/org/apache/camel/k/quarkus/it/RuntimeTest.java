/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.k.quarkus.it;

import javax.ws.rs.core.MediaType;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.camel.k.quarkus.Application;
import org.apache.camel.quarkus.core.FastCamelContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class RuntimeTest {
    @Test
    public void inspect() {
        JsonPath p = RestAssured.given()
            .accept(MediaType.APPLICATION_JSON)
            .get("/test/inspect")
            .then()
                .statusCode(200)
            .extract()
                .body()
                .jsonPath();

        assertThat(p.getString("camel-context")).isEqualTo(FastCamelContext.class.getName());
        assertThat(p.getString("camel-k-runtime")).isEqualTo(Application.Runtime.class.getName());
        assertThat(p.getString("routes-collector")).isEqualTo(Application.NoRoutesCollector.class.getName());
    }

    @Test
    public void configSourceProvider() {
        String result = RestAssured.given()
            .accept(MediaType.TEXT_PLAIN)
            .get("/test/property/quarkus.my-property")
            .then()
                .statusCode(200)
            .extract()
                .body()
                .asString();

        assertThat(result).isEqualTo("my-test-value");
    }
}