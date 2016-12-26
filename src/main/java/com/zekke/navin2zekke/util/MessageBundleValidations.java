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
package com.zekke.navin2zekke.util;

import static com.zekke.navin2zekke.util.Messages.getMessage;

/**
 * Validation class with message bundle support.
 *
 * @author Daniel Pedraza-Arcega
 */
public class MessageBundleValidations {

    private MessageBundleValidations() {
        throw new AssertionError();
    }

    /**
     * @param expression a boolean expression.
     * @param messageKey the key for the desired message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void require(boolean expression, String messageKey, Object... messageArguments) {
        SimpleValidations.require(expression, getMessage(messageKey, messageArguments));
    }

    /**
     * @param obj the object to test
     * @param messageKey the key for the desired message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static void requireNonNull(Object obj, String messageKey, Object... messageArguments) {
        SimpleValidations.requireNonNull(obj, getMessage(messageKey, messageArguments));
    }

    /**
     * @param s the string to test
     * @param messageKey the key for the desired message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     * @throws IllegalArgumentException if {@code s} is empty.
     */
    public static void requireNonEmpty(String s, String messageKey, Object... messageArguments) {
        SimpleValidations.requireNonEmpty(s, getMessage(messageKey, messageArguments));
    }

    /**
     * @param s the string to test
     * @param messageKey the key for the desired message.
     * @param messageArguments the objects to be formatted and substituted in the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     * @throws IllegalArgumentException if {@code s} is blank.
     */
    public static void requireNonBlank(String s, String messageKey, Object... messageArguments) {
        SimpleValidations.requireNonBlank(s, getMessage(messageKey, messageArguments));
    }
}
