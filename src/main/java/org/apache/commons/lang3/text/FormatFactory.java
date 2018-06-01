
package org.apache.commons.lang3.text;

import java.text.Format;
import java.util.Locale;


@Deprecated
public interface FormatFactory {

    
    Format getFormat(String name, String arguments, Locale locale);

}
