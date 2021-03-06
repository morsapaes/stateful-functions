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

package com.ververica.statefun.flink.core.types.protobuf;

import com.google.protobuf.Message;
import java.util.Objects;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;

public class ProtobufTypeInformation<M extends Message> extends TypeInformation<M> {

  private static final long serialVersionUID = 1L;

  private final Class<M> messageTypeClass;

  public ProtobufTypeInformation(Class<M> messageTypeClass) {
    this.messageTypeClass = Objects.requireNonNull(messageTypeClass);
  }

  @Override
  public boolean isBasicType() {
    return false;
  }

  @Override
  public boolean isTupleType() {
    return false;
  }

  @Override
  public int getArity() {
    return 0;
  }

  @Override
  public int getTotalFields() {
    return 0;
  }

  @Override
  public Class<M> getTypeClass() {
    return messageTypeClass;
  }

  @Override
  public boolean isKeyType() {
    return false;
  }

  @Override
  public TypeSerializer<M> createSerializer(ExecutionConfig config) {
    return new ProtobufTypeSerializer<>(messageTypeClass);
  }

  @Override
  public String toString() {
    return "ProtobufTypeInformation(" + messageTypeClass + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProtobufTypeInformation<?> that = (ProtobufTypeInformation<?>) o;
    return messageTypeClass.equals(that.messageTypeClass);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(messageTypeClass);
  }

  @Override
  public boolean canEqual(Object obj) {
    return obj instanceof ProtobufTypeInformation;
  }
}
