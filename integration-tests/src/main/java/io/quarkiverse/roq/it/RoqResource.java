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
package io.quarkiverse.roq.it;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import io.quarkiverse.roq.runtime.StaticPage;
import io.quarkiverse.roq.runtime.StaticPages;

@Path("/roq")
@ApplicationScoped
public class RoqResource {
    // add some rest methods here

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("name") String name) {
        return "Hello " + name;
    }

    @Produces
    @Singleton
    @Transactional
    StaticPages produce() {
        return new StaticPages(List.of(
                StaticPage.builder().html("/roq?name=foo-html").build(),
                StaticPage.builder().path("/roq?name=foo").build(),
                StaticPage.builder().path("/roq?name=bar").build()));
    }

}
