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
package com.zekke.navin2zekke.database;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonBlank;
import static com.zekke.navin2zekke.util.Messages.getMessage;
import static com.zekke.navin2zekke.util.Strings.BLANK_SPACE;
import static com.zekke.navin2zekke.util.Strings.EMPTY;
import static com.zekke.navin2zekke.util.Strings.isBlank;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Utility class for getting SQL statements from a file.
 *
 * @author Daniel Pedraza-Arcega
 */
class SqlFileParser {

    private static final String SQL_DELIMITER = ";";

    private final URI fileUri;
    private List<String> statements;

    private SqlFileParser(URI fileUri) {
        this.fileUri = fileUri;
    }

    /**
     * Creates a new parser for the given classpath file.
     *
     * @param classpath a classpath file.
     * @return a new parser.
     */
    static SqlFileParser newFromClasspath(String classpath) {
        requireNonBlank(classpath, "error.arg.blank", "Classpath file");
        try {
            return new SqlFileParser(SqlFileParser.class.getClassLoader().getResource(classpath).toURI());
        } catch (URISyntaxException | NullPointerException ex) {
            throw new IllegalArgumentException(getMessage("error.file.not_valid", classpath), ex);
        }
    }

    /** @return the statements of the given file. */
    List<String> getStatements() {
        if (statements == null) parse();
        return statements;
    }

    private void parse() {
        String rawContent = readFileContent();
        if (isBlank(rawContent)) statements = emptyList();
        else {
            rawContent = Patterns.SINGLE_LINE_COMMENT.matcher(rawContent).replaceAll(EMPTY);
            rawContent = Patterns.MULTI_LINE_COMMENT.matcher(rawContent).replaceAll(EMPTY);
            rawContent = Patterns.LINE_BRAKES.matcher(rawContent).replaceAll(BLANK_SPACE);
            rawContent = Patterns.WHITE_SPACES.matcher(rawContent).replaceAll(BLANK_SPACE);
            statements = Arrays.stream(rawContent.split(SQL_DELIMITER))
                    .filter(s -> !isBlank(s))
                    .map(String::trim)
                    .collect(toList());
        }
    }

    private String readFileContent() {
        try {
            FileSystem jarFileSystem = createFileSystemIfNeeded(fileUri);
            String fileContent = new String(Files.readAllBytes(Paths.get(fileUri)));
            if (jarFileSystem != null) jarFileSystem.close();
            return fileContent;
        } catch (IOException | NullPointerException ex) {
            throw new IllegalArgumentException(getMessage("error.file.not_valid", fileUri.toString()), ex);
        }
    }

    // When the file is packaged in a *.jar a FileSystems needs to be created.
    private FileSystem createFileSystemIfNeeded(URI uri) throws IOException {
        try {
            Paths.get(uri);
            return null;
        } catch (FileSystemNotFoundException ex) {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            return FileSystems.newFileSystem(uri, env);
        }
    }

    private static class Patterns {
        private static final Pattern MULTI_LINE_COMMENT = Pattern.compile("\\/\\*([^*]|[\\r\\n]|(\\*+([^*\\/]|[\\r\\n])))*\\*\\/", Pattern.MULTILINE);
        private static final Pattern SINGLE_LINE_COMMENT = Pattern.compile("--.+$", Pattern.MULTILINE);
        private static final Pattern LINE_BRAKES = Pattern.compile("\\r?\\n|\\r", Pattern.MULTILINE);
        private static final Pattern WHITE_SPACES = Pattern.compile("\\s+", Pattern.MULTILINE);
    }
}
