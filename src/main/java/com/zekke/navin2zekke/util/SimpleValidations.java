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

import java.util.Objects;

import static com.zekke.navin2zekke.util.Strings.isBlank;

/**
 * Validation class.
 *
 * @author Daniel Pedraza-Arcega
 */
class SimpleValidations {

    private SimpleValidations() {
        throw new AssertionError();
    }

    /**
     * @param expression a boolean expression.
     * @param message the message.
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    static void require(boolean expression, String message) {
        if (!expression) throw new IllegalArgumentException(message);
    }

    /**
     * @param obj the object to test
     * @param message the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    static void requireNonNull(Object obj, String message) {
        Objects.requireNonNull(obj, message);
    }

    /**
     * @param s the string to test
     * @param message the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     * @throws IllegalArgumentException if {@code s} is empty.
     */
    static void requireNonEmpty(String s, String message) {
        requireNonNull(s, message);
        require(!s.isEmpty(), message);
    }

    /**
     * @param s the string to test
     * @param message the message.
     * @throws NullPointerException if {@code obj} is {@code null}
     * @throws IllegalArgumentException if {@code s} is blank.
     */
    static void requireNonBlank(String s, String message) {
        requireNonNull(s, message);
        require(!isBlank(s), message);
    }
}
