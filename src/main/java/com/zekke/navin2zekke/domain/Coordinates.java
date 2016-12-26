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

import com.zekke.navin2zekke.util.Constants;

import java.util.Objects;

import static com.zekke.navin2zekke.util.MessageBundleValidations.require;

/**
 * Represents the geographic location of a place in a map.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Coordinates {

    public static final double MAX_LATITUDE = 90;
    public static final double MIN_LATITUDE = -MAX_LATITUDE;
    public static final double MAX_LONGITUDE = 180;
    public static final double MIN_LONGITUDE = -MAX_LONGITUDE;

    private final double latitude;
    private final double longitude;

    private Coordinates(double latitude, double longitude) {
        require(MIN_LATITUDE <= latitude && latitude <= MAX_LATITUDE, "error.arg.range", "latitude", MIN_LATITUDE, MAX_LATITUDE);
        require(MIN_LONGITUDE <= longitude && longitude <= MAX_LONGITUDE, "error.arg.range", "longitude", MIN_LONGITUDE, MAX_LONGITUDE);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor.
     *
     * @param latitude a double between {@value #MIN_LATITUDE} and {@value #MAX_LATITUDE}.
     * @param longitude a double between {@value #MIN_LONGITUDE} and {@value #MAX_LONGITUDE}.
     */
    public static Coordinates newLatLng(double latitude, double longitude) {
        return new Coordinates(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Calculates the distance between this location and another.
     *
     * @param other the other location.
     * @return the distance in meters.
     */
    public double distanceTo(Coordinates other) {
        double lat1rad = Math.toRadians(latitude);
        double lat2rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - latitude);
        double deltaLon = Math.toRadians(other.longitude - longitude);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1rad) * Math.cos(lat2rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = Constants.EARTH_RADIUS * c;
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;

        Coordinates other = (Coordinates) o;

        return Objects.equals(latitude, other.latitude) &&
                Objects.equals(longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{\"latitude\": " + latitude + ", \"longitude\": " + longitude + "}";
    }
}
