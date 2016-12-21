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

    private Waypoint a;
    private Waypoint b;
    private Direction direction;
    private double distance;

    public Waypoint getA() {
        return a;
    }

    public void setA(Waypoint a) {
        this.a = a;
    }

    public Waypoint getB() {
        return b;
    }

    public void setB(Waypoint b) {
        this.b = b;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

        return Objects.equals(a, other.a) &&
                Objects.equals(b, other.b) &&
                Objects.equals(direction, other.direction) &&
                Objects.equals(distance, other.distance);

    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, direction, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "(" + ((a == null) ? null : a.getName()) + ")" +
                direction +
                "(" + ((b == null) ? null : b.getName()) + ")" +
                ", distance=" + distance +
                '}';
    }

    /** Direction of the Path, for example: {@literal A->B, A<-B, A<->B} */
    public enum Direction {
        AB {
            @Override
            public String toString() {
                return "->";
            }
        }, BA {
            @Override
            public String toString() {
                return "<-";
            }
        }, BIDIRECTIONAL {
            @Override
            public String toString() {
                return "<->";
            }
        }
    }
}
