appender('console', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy/MM/dd HH:mm:ss.SSS} %-5level in %logger: %msg%n'
    }
}

root(ALL, ['console'])