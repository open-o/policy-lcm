/*
 * Copyright 2017 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.policy.lcm;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.openo.policy.lcm.common.Config;
import org.openo.policy.lcm.common.ServiceRegistrer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

/**
 *
 */
public class App extends Application<AppConfig> {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(App.class);
    private static String driverInstanceId;
    private Thread driverManagerRegister;

    /**
     * Main function.
     *
     * @param args arguments, input by users
     */
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    public static String getDriverInstanceId() {
        return driverInstanceId;
    }

    public static void setDriverInstanceId(String driverInstanceId) {
        App.driverInstanceId = driverInstanceId;
    }

    /**
     * Initialize before the service started.
     *
     * @param bootstrap Bootstrap.
     */
    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
    }

    /**
     * Run application.
     *
     * @param config      configuration settings read from configuration file.
     * @param environment Environment.
     */
    @Override
    public void run(final AppConfig config, Environment environment) {
        LOGGER.info("Method App#run() called");
        // AppConfig.setConfig(config);

        CustomHealthCheck healthCheck = new CustomHealthCheck();
        environment.healthChecks().register("healthcheck", healthCheck);

        // Add the resource to the environment
        environment.jersey().register(new LcmResource());

        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Config.setConfigration(config);
        initService();

        initSwaggerConfig(config, environment);
    }

    private void initService() {
        Thread registerService = new Thread(new ServiceRegistrer());
        registerService.setName("register policy lcm service to Microservice Bus");
        registerService.start();
    }

    private void initSwaggerConfig(AppConfig configuration, Environment environment) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle("Open-O POLICY-LCM API");
        config.setVersion("1.1.0");
        config.setResourcePackage("org.openo.policy.lcm");
        // set rest api base path in swagger
        SimpleServerFactory serverFactory =
            (SimpleServerFactory) configuration.getServerFactory();
        String basePath = serverFactory.getApplicationContextPath();
        String rootPath = serverFactory.getJerseyRootPath();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
        basePath =
            ("/").equals(basePath) ? rootPath : (new StringBuilder()).append(basePath).append(rootPath)
                .toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

}
