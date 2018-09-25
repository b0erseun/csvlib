package com.cronjes.csvlib.deserializers;

/**
 *  Deserializer to deserialize primitives.
 */

public class PrimitiveDeserializer implements CsvDeserializer {
    @Override
    public Object deserialize(Class<?> clazz, String serializedData) {

        //If serializedData is empty or null, initialize the primitive with the default value.
        //Not all primitives are handled,

        if (boolean.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Boolean.parseBoolean(serializedData);
            } else {
                return false;
            }
        }
        if (byte.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Byte.parseByte(serializedData);
            } else {
                return (byte)0;
            }
        }
        if (short.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Short.parseShort(serializedData);
            } else {
                return (short)0;
            }
        }
        if (int.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Integer.parseInt(serializedData);
            } else {
                return (int)0;
            }
        }
        if (long.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Long.parseLong(serializedData);
            } else {
                return 0L;
            }
        }
        if (float.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Float.parseFloat(serializedData);
            } else {
                return (float)0;
            }
        }
        if (double.class == clazz) {
            if (serializedData != null && !serializedData.isEmpty()) {
                return Double.parseDouble(serializedData);
            } else {
                return (double)0;
            }
        }

        return serializedData;

    }

    @Override
    public boolean canDeserialize(Class<?> clazz) {

        return clazz.isPrimitive() && (clazz != char.class); // Not supporting char.

    }
}
