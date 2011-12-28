package org.system;

import java.io.File;
import java.util.EventListener;

public interface DragNDropListener extends EventListener {
    public void fileDragged(final File e);
}