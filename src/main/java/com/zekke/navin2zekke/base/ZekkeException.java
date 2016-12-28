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

import com.zekke.navin2zekke.util.Messages;

import java.util.Arrays;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonBlank;

/**
 * Base application exception.
 *
 * @author Daniel Pedraza-Arcega
 */
public abstract class ZekkeException extends RuntimeException {

    private static final long serialVersionUID = 23425667534234L;

    private final String messageKey;
    private final Object[] messageArgs;

    /**
     * Buildable constructor.
     *
     * @param builder a builder.
     */
    protected ZekkeException(BaseExceptionBuilder<? extends ZekkeException> builder) {
        super(builder.messageKey == null ? null : Messages.getMessage(builder.messageKey, builder.messageArgs), builder.throwable);
        this.messageKey = builder.messageKey;
        this.messageArgs = builder.messageArgs;
    }

    /** @return the message key. */
    public String getMessageKey() {
        return messageKey;
    }

    /** @return the message arguments. */
    public Object[] getMessageArgs() {
        return messageArgs == null ? null : Arrays.copyOf(messageArgs, messageArgs.length);
    }

    /**
     * Base exception builder.
     *
     * @param <E> exception type.
     * @author Daniel Pedraza-Arcega
     */
    public static abstract class BaseExceptionBuilder<E extends ZekkeException> implements Buildable<E> {

        private String messageKey;
        private Throwable throwable;
        private Object[] messageArgs;

        /** @param messageKey the key for the desired message. */
        public BaseExceptionBuilder<E> messageKey(String messageKey) {
            if (messageKey != null) requireNonBlank(messageKey, "error.arg.blank", "messageKey");
            this.messageKey = messageKey;
            return this;
        }

        /**
         * @param arg1 the object to be formatted and substituted in the message.
         * @param moreArgs other objects to be formatted and substituted in the message.
         */
        public BaseExceptionBuilder<E> messageArgs(Object arg1, Object... moreArgs) {
            if (moreArgs.length == 0) messageArgs = new Object[]{arg1};
            else {
                messageArgs = new Object[moreArgs.length + 1];
                messageArgs[0] = arg1;
                System.arraycopy(moreArgs, 0, messageArgs, 1, moreArgs.length);
            }
            return this;
        }

        /** @param throwable the cause. */
        public BaseExceptionBuilder<E> cause(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }
    }
}
