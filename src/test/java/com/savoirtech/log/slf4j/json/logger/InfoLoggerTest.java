/*
 * Copyright (c) 2016 Savoir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.savoirtech.log.slf4j.json.logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Verify all of the operations of the InfoLogger itself.  Note this intentionally does not
 * test any of AbstractJsonLogger.
 *
 * Created by art on 3/28/16.
 */
public class InfoLoggerTest {

  private InfoLogger logger;

  private String testMessage;

  private org.slf4j.Logger slf4jLogger;

  @Before
  public void setupTest() throws Exception {
    this.testMessage = "x-test-formatted-message-x";
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);

    this.logger = new InfoLogger(slf4jLogger, null, null) {
      @Override
      protected String formatMessage(String level) {
        if (level.equals(InfoLogger.LOG_LEVEL)) {
          return testMessage;
        } else {
          throw new RuntimeException("unexpected log level " + level);
        }
      }
    };
  }

  @Test
  public void testLog() throws Exception {
    this.logger.log();

    Mockito.verify(slf4jLogger).info(this.testMessage);
  }

  @Test
  public void testLogException() throws Exception {
    RuntimeException rtExc = new RuntimeException("x-rt-exc-x");

    Mockito.doThrow(rtExc).when(this.slf4jLogger).info(this.testMessage);

    this.logger.log();

    Mockito.verify(slf4jLogger).info("{\"" + rtExc + "\"}");
  }

  @Test
  public void testToString() throws Exception {
    assertEquals(this.testMessage, this.logger.toString());
  }
}