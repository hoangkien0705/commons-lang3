
package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SerializationUtils {

    
    public SerializationUtils() {
        super();
    }

    // Clone
    //-----------------------------------------------------------------------
    
    public static <T extends Serializable> T clone(final T object) {
        if (object == null) {
            return null;
        }
        final byte[] objectData = serialize(object);
        final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);

        try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais,
                object.getClass().getClassLoader())) {
            
            @SuppressWarnings("unchecked") // see above
            final T readObject = (T) in.readObject();
            return readObject;

        } catch (final ClassNotFoundException ex) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
        } catch (final IOException ex) {
            throw new SerializationException("IOException while reading or closing cloned object data", ex);
        }
    }

    
    @SuppressWarnings("unchecked") // OK, because we serialized a type `T`
    public static <T extends Serializable> T roundtrip(final T msg) {
        return (T) SerializationUtils.deserialize(SerializationUtils.serialize(msg));
    }

    // Serialize
    //-----------------------------------------------------------------------
    
    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        Validate.isTrue(outputStream != null, "The OutputStream must not be null");
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)){
            out.writeObject(obj);
        } catch (final IOException ex) {
            throw new SerializationException(ex);
        }
    }

    
    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    // Deserialize
    //-----------------------------------------------------------------------
    
    public static <T> T deserialize(final InputStream inputStream) {
        Validate.isTrue(inputStream != null, "The InputStream must not be null");
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            @SuppressWarnings("unchecked")
            final T obj = (T) in.readObject();
            return obj;
        } catch (final ClassNotFoundException | IOException ex) {
            throw new SerializationException(ex);
        }
    }

    
    public static <T> T deserialize(final byte[] objectData) {
        Validate.isTrue(objectData != null, "The byte[] must not be null");
        return SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
    }

    
     static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private static final Map<String, Class<?>> primitiveTypes =
                new HashMap<>();

        static {
            primitiveTypes.put("byte", byte.class);
            primitiveTypes.put("short", short.class);
            primitiveTypes.put("int", int.class);
            primitiveTypes.put("long", long.class);
            primitiveTypes.put("float", float.class);
            primitiveTypes.put("double", double.class);
            primitiveTypes.put("boolean", boolean.class);
            primitiveTypes.put("char", char.class);
            primitiveTypes.put("void", void.class);
        }

        private final ClassLoader classLoader;

        
        ClassLoaderAwareObjectInputStream(final InputStream in, final ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        
        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            final String name = desc.getName();
            try {
                return Class.forName(name, false, classLoader);
            } catch (final ClassNotFoundException ex) {
                try {
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                } catch (final ClassNotFoundException cnfe) {
                    final Class<?> cls = primitiveTypes.get(name);
                    if (cls != null) {
                        return cls;
                    }
                    throw cnfe;
                }
            }
        }

    }

}
