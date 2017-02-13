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


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The class to statically store Config instance.
 */
public class AppConfig extends Configuration{
    @NotEmpty
    private String msbServerAddr;
    @Valid
    private String serviceIp;

    @JsonProperty
    private int timeout = 600;
    @JsonProperty
    private String msbUrl;

    // private static Config config;

    private AppConfig(){}

    @JsonProperty
    public String getMsbServerAddr() {
        return msbServerAddr;
    }

    @JsonProperty
    public void setMsbServerAddr(String msbServerAddr) {
        this.msbServerAddr = msbServerAddr;
    }
    @JsonProperty
    public String getServiceIp() {
        return serviceIp;
    }

    @JsonProperty
    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    @JsonProperty
    public int getTimeout() {
        return timeout;
    }

    @JsonProperty
    public String getMsbUrl() {
        return msbUrl;
    }

}
