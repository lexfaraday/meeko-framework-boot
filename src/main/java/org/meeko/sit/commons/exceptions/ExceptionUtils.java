package org.meeko.sit.commons.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author Al3x G0m3z
 *
 */
public class ExceptionUtils {
    public static String getMessageException(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
