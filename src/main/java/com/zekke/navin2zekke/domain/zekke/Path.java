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
package com.zekke.navin2zekke.domain.zekke;

import java.util.Objects;

/**
 * A connection between two waypoints.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Path {

    private Waypoint from;
    private Waypoint to;
    private double distance;

    public Waypoint getFrom() {
        return from;
    }

    public void setFrom(Waypoint from) {
        this.from = from;
    }

    public Waypoint getTo() {
        return to;
    }

    public void setTo(Waypoint to) {
        this.to = to;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;

        Path other = (Path) o;

        return Objects.equals(from, other.from) &&
                Objects.equals(to, other.to) &&
                Objects.equals(distance, other.distance);

    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "(" + (from == null ? null : from.getName()) +
                ")->(" + (to == null ? null : to.getName()) +
                "), distance=" + distance +
                '}';
    }
}
