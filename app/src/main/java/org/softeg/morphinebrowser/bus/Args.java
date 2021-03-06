package org.softeg.morphinebrowser.bus;

public class Args {

    private Object[] objects;

    Args(Object... objects) {
        this.objects = objects;
    }

    public <T> T get(int i) {
        if (i < 0 || i >= objects.length)
            return null;
        return (T) objects[i];
    }

}

