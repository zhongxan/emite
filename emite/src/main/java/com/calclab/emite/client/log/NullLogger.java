package com.calclab.emite.client.log;

public class NullLogger implements Logger {

    public final void debug(final String pattern, final Object... params) {
    }

    public final void info(final String pattern, final Object... params) {
    }

    public final void log(final int level, final String pattern, final Object... params) {
    }

    public void error(final String pattern, final Object... params) {
    }

    public void fatal(final String pattern, final Object... params) {
    }

    public void warn(final String pattern, final Object... params) {
    }
}
