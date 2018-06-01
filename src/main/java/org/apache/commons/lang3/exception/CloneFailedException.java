
package org.apache.commons.lang3.exception;


public class CloneFailedException extends RuntimeException {
    // ~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 20091223L;

    // ~ Constructors -----------------------------------------------------------

    
    public CloneFailedException(final String message) {
        super(message);
    }

    
    public CloneFailedException(final Throwable cause) {
        super(cause);
    }

    
    public CloneFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
