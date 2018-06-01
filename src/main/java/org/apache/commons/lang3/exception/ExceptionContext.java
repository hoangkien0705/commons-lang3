
package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;


public interface ExceptionContext {

    
    ExceptionContext addContextValue(String label, Object value);

    
    ExceptionContext setContextValue(String label, Object value);

    
    List<Object> getContextValues(String label);

    
    Object getFirstContextValue(String label);

    
    Set<String> getContextLabels();

    
    List<Pair<String, Object>> getContextEntries();

    
    String getFormattedExceptionMessage(String baseMessage);

}
