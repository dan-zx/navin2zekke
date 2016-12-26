/*
 * Copyright 2016-2017 Daniel Pedraza-Arcega
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
package com.zekke.navin2zekke.base;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ZekkeExceptionTest {

    @Test
    public void shouldHaveNoCauseNorMessage() {
        assertThat(new ZekkeExceptionImpl.Builder().build()).isNotNull().hasNoCause().hasMessage(null).extracting("messageKey", "messageArgs").containsExactly(null, null);
    }

    @Test
    public void shouldHaveMessage() {
        String messageKey = "test.message";
        assertThat(new ZekkeExceptionImpl.Builder()
                .messageKey(messageKey)
                .build())
                .isNotNull().hasMessage("Test message").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, null);
    }

    @Test
    public void shouldHaveMessageWithArg() {
        String messageKey = "test.message_with_arg";
        Object[] args = {5};
        assertThat(new ZekkeExceptionImpl.Builder()
                .messageKey(messageKey)
                .messageArgs(args[0])
                .build())
                .isNotNull().hasMessage("Test message with 5").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    @Test
    public void shouldHaveCause() {
        assertThat(new ZekkeExceptionImpl.Builder()
                .cause(new NullPointerException())
                .build())
                .isNotNull().hasMessage(null).hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(null, null);
    }

    @Test
    public void shouldHaveMessageWithArgsAndCause() {
        String messageKey = "test.message_with_args";
        Object[] args = {5, "other"};
        assertThat(new ZekkeExceptionImpl.Builder()
                .cause(new NullPointerException())
                .messageKey(messageKey)
                .messageArgs(args[0], args[1])
                .build())
                .isNotNull().hasMessage("Test message with 5 and other").hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    private static class ZekkeExceptionImpl extends ZekkeException {

        private ZekkeExceptionImpl(Builder builder) {
            super(builder);
        }

        private static class Builder extends BaseExceptionBuilder<ZekkeExceptionImpl> {

            @Override
            public ZekkeExceptionImpl build() {
                return new ZekkeExceptionImpl(this);
            }
        }
    }
}
