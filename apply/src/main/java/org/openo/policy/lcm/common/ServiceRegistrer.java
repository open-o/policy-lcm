/**
 * Copyright 2016 ZTE Corporation.
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

package org.openo.policy.lcm.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parameters. <br/>
 * 
 * @author sun qi
 * @version ESR V1
 */
public class ServiceRegistrer implements Runnable {
  private final ServiceRegisterEntity lcmEntity = new ServiceRegisterEntity();
  private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistrer.class);

  public ServiceRegistrer() {
    initServiceEntity();
  }

  @Override
  public void run() {
    LOG.info("start extsys microservice register");
    boolean flag = false;
    int retry = 0;
    while (!flag && retry < 1000) {
      LOG.info("extsys microservice register.retry:" + retry);
      retry++;
      flag = MicroserviceBusConsumer.registerService(lcmEntity);
      if (flag == false) {
        LOG.warn("microservice register failed, sleep 30S and try again.");
        threadSleep(30000);
      } else {
        LOG.info("microservice register success!");
        break;
      }
    }
    LOG.info("extsys microservice register end.");
  }

  private void threadSleep(int second) {
    LOG.info("start sleep ....");
    try {
      Thread.sleep(second);
    } catch (InterruptedException error) {
      LOG.error("thread sleep error.errorMsg:" + error.getMessage());
    }
    LOG.info("sleep end .");
  }

  private void initServiceEntity() {
    lcmEntity.setServiceName("policy-lcm");
    lcmEntity.setProtocol("REST");
    lcmEntity.setVersion("v1");
    lcmEntity.setUrl("/openoapi/pollcm/v1");
    lcmEntity.setSingleNode(Config.getConfigration().getServiceIp(), "8903", 0);
    lcmEntity.setVisualRange("1");
  }
}
