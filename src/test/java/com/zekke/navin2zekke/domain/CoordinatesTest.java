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
package com.zekke.navin2zekke.domain;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.zekke.navin2zekke.domain.Coordinates.newLatLng;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

public class CoordinatesTest {

    private static final double INVALID_UPPER_LAT = Coordinates.MAX_LATITUDE + 1;
    private static final double INVALID_LOWER_LAT = Coordinates.MIN_LATITUDE - 1;
    private static final double INVALID_UPPER_LNG = Coordinates.MAX_LONGITUDE + 1;
    private static final double INVALID_LOWER_LNG = Coordinates.MIN_LONGITUDE - 1;
    private static final Coordinates VALID_COORDINATES = newLatLng(19.054492, -98.283176);

    @Test(dataProvider = "invalidLatLng")
    public void shouldThrowIllegalArgumentExceptionWhenLatLngAreOutOfBounds(double lat, double lng) {
        assertThatThrownBy(() -> newLatLng(lat, lng)).isInstanceOf(IllegalArgumentException.class).hasNoCause();
    }

    @Test
    public void shouldCalculateDistanceInMeters() {
        assertThat(VALID_COORDINATES.distanceTo(newLatLng(19.050991, -98.278627))).isCloseTo(616.56, offset(0.01));
    }

    @DataProvider
    public Object[][] invalidLatLng() {
        return new Object[][]{
                {INVALID_UPPER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_UPPER_LAT, INVALID_UPPER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_UPPER_LNG},
                {INVALID_LOWER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_LOWER_LAT, INVALID_UPPER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_UPPER_LNG},
                {INVALID_UPPER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_UPPER_LAT, INVALID_LOWER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_LOWER_LNG},
                {INVALID_LOWER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_LOWER_LAT, INVALID_LOWER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_LOWER_LNG}
        };
    }
}
