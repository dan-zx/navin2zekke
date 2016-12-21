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

import com.zekke.navin2zekke.domain.Coordinates;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.zekke.navin2zekke.util.Strings.EMPTY;

/**
 * Generic point in a map with name.
 *
 * @author Daniel Pedraza-Arcega
 */
public abstract class Waypoint {

    private String name;
    private Coordinates coordinates;
    private Set<Path> paths = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Waypoint)) return false;

        Waypoint other = (Waypoint) o;

        return Objects.equals(name, other.name) &&
                Objects.equals(coordinates, other.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, coordinates);
    }

    @Override
    public String toString() {
        String extras = toStringExtras();
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", paths=" + paths +
                (extras == null ? EMPTY : ", " + extras) +
                '}';
    }

    /** @return extras to append to the base {@link #toString()} method. */
    protected String toStringExtras() {
        return null;
    }
}
