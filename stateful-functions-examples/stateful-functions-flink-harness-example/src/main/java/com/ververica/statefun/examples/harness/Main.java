/*
 * Copyright 2019 Ververica GmbH.
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

package com.ververica.statefun.examples.harness;

import com.ververica.statefun.examples.harness.MyMessages.MyInputMessage;
import com.ververica.statefun.examples.harness.MyMessages.MyOutputMessage;
import com.ververica.statefun.flink.harness.Harness;
import com.ververica.statefun.flink.harness.io.SerializableConsumer;
import com.ververica.statefun.flink.harness.io.SerializableSupplier;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import org.apache.flink.util.StringUtils;

public class Main {

  public static void main(String[] args) throws Exception {
    Harness harness =
        new Harness()
            .noCheckpointing()
            .withKryoMessageSerializer()
            .withSupplyingIngress(MyConstants.REQUEST_INGRESS, new MessageGenerator())
            .withConsumingEgress(MyConstants.RESULT_EGRESS, new MessagePrinter());

    harness.start();
  }

  /** generate a random message, once a second a second. */
  private static final class MessageGenerator implements SerializableSupplier<MyInputMessage> {

    private static final long serialVersionUID = 1;

    @Override
    public MyInputMessage get() {
      try {
        Thread.sleep(1_000);
      } catch (InterruptedException e) {
        throw new RuntimeException("Interrupted", e);
      }
      return randomMessage();
    }

    @Nonnull
    private MyInputMessage randomMessage() {
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      final String userId = StringUtils.generateRandomAlphanumericString(random, 2);
      return new MyInputMessage(userId, "hello " + userId);
    }
  }

  /** prints the messages to stdout. */
  private static final class MessagePrinter implements SerializableConsumer<MyOutputMessage> {

    private static final long serialVersionUID = 1;

    @Override
    public void accept(MyOutputMessage message) {
      System.out.println(message);
    }
  }
}
