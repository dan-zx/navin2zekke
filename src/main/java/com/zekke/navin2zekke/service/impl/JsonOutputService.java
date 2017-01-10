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
package com.zekke.navin2zekke.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import com.zekke.navin2zekke.domain.Coordinates;
import com.zekke.navin2zekke.domain.zekke.Path;
import com.zekke.navin2zekke.domain.zekke.Waypoint;
import com.zekke.navin2zekke.service.OutputService;
import com.zekke.navin2zekke.service.ServiceException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonNull;
import static com.zekke.navin2zekke.util.Strings.isBlank;
import static java.util.stream.Collectors.toList;

/**
 * Writes ZeKKe graph in a JSON file.
 *
 * @author Daniel Pedraza-Arcega
 */
public class JsonOutputService implements OutputService {

    private static final String DEFAULT_JSON_FILE_PATH = "dist/json";
    private static final String FILE_NAME = "zekke_graph.json";

    private Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create();
    private String jsonFilePath = DEFAULT_JSON_FILE_PATH;

    /**
     * Writes ZeKKe graph in a JSON file. The outpile is always named {@value #FILE_NAME}.
     *
     * @param waypoints a ZeKKe graph.
     */
    @Override
    public void write(Collection<Waypoint> waypoints) {
        requireNonNull(waypoints, "error.arg.null", "Waypoints");
        List<OutputWaypoint> outputWaypoints = waypoints.stream().map(OutputWaypoint::new).collect(toList());
        File jsonFile = createJsonFile();
        try (OutputStream stream = new FileOutputStream(jsonFile)) {
            stream.write(gsonPrinter.toJson(outputWaypoints).getBytes());
            stream.flush();
        } catch (IOException ex) {
            throw new ServiceException.Builder()
                    .messageKey("error.json_file.cannot_create")
                    .messageArgs(jsonFilePath, FILE_NAME)
                    .cause(ex)
                    .build();
        }
    }

    private File createJsonFile() {
        try {
            File jsonFileDirectory = new File(jsonFilePath);
            if (!jsonFileDirectory.exists()) jsonFileDirectory.mkdirs();
            File jsonFile = new File(jsonFileDirectory, FILE_NAME);
            if (jsonFile.exists()) jsonFile.delete();
            jsonFile.createNewFile();
            return jsonFile;
        } catch (IOException ex) {
            throw new ServiceException.Builder()
                    .messageKey("error.json_file.cannot_create")
                    .messageArgs(jsonFilePath, FILE_NAME)
                    .cause(ex)
                    .build();
        }
    }

    /**
     * Sets the output file path.
     *
     * @param jsonFilePath file path.
     */
    public @Inject(optional = true) void setJsonFilePath(@Named("jsonFilePath") String jsonFilePath) {
        if (!isBlank(jsonFilePath)) this.jsonFilePath = jsonFilePath;
    }

    public @Inject(optional = true) void setEnableJsonPrettyPrinting(@Named("enableJsonPrettyPrinting") boolean enableJsonPrettyPrinting) {
        if (enableJsonPrettyPrinting) gsonPrinter = new GsonBuilder().setPrettyPrinting().create();
        else gsonPrinter = new Gson();
    }

    private static class OutputWaypoint {
        private int _id;
        private String name;
        private Coordinates coordinates;
        private Waypoint.Type type;
        private List<OutputPath> paths;

        private OutputWaypoint(Waypoint waypoint) {
            _id = waypoint.getId();
            name = waypoint.getName();
            coordinates = waypoint.getCoordinates();
            type = waypoint.getType();
            paths = waypoint.getPaths().stream().map(OutputPath::new).collect(toList());
        }
    }

    private static class OutputPath {
        private int to_waypoint;
        private double distance;

        private OutputPath(Path path) {
            to_waypoint = path.getTo().getId();
            distance = path.getDistance();
        }
    }
}
