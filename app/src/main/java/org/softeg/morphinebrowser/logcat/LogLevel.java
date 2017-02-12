package org.softeg.morphinebrowser.logcat;

import java.util.EnumSet;

public class LogLevel {
    public static final EnumSet VERBOSE = EnumSet.of(Priority.VERBOSE,
            Priority.DEBUG,
            Priority.INFO,
            Priority.WARNING,
            Priority.ERROR,
            Priority.FATAL,
            Priority.SILENT);

    public static final EnumSet DEBUG = EnumSet.of(Priority.DEBUG,
            Priority.INFO,
            Priority.WARNING,
            Priority.ERROR,
            Priority.FATAL,
            Priority.SILENT);

    public static final EnumSet INFO = EnumSet.of(Priority.INFO,
            Priority.WARNING,
            Priority.ERROR,
            Priority.FATAL,
            Priority.SILENT);

    public static final EnumSet WARNING = EnumSet.of(Priority.WARNING,
            Priority.ERROR,
            Priority.FATAL,
            Priority.SILENT);

    public static final EnumSet ERROR = EnumSet.of(Priority.ERROR,
            Priority.FATAL,
            Priority.SILENT);

    public static final EnumSet ASSERT = EnumSet.of(Priority.FATAL,
            Priority.SILENT);
}