package com.aomygod.retrofit.rxjava;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

class GSONUtil {

    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new DefaultTypeAdapterFactory())
            .registerTypeAdapter(Double.class, new ShortSerializer())
            .registerTypeAdapter(Double.class, new IntegerSerializer())
            .registerTypeAdapter(Double.class, new LongSerializer())
            .registerTypeAdapter(Double.class, new FloatSerializer())
            .registerTypeAdapter(Double.class, new DoubleSerializer())
            // .registerTypeAdapter(Double.class, new ByteSerializer())
            // .registerTypeAdapter(Double.class, new BooleanSerializer())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    static class ShortSerializer implements JsonSerializer<Short> {

        @Override
        public JsonElement serialize(Short src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class IntegerSerializer implements JsonSerializer<Integer> {

        @Override
        public JsonElement serialize(Integer src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class LongSerializer implements JsonSerializer<Long> {

        @Override
        public JsonElement serialize(Long src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class FloatSerializer implements JsonSerializer<Float> {

        @Override
        public JsonElement serialize(Float src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class DoubleSerializer implements JsonSerializer<Double> {

        @Override
        public JsonElement serialize(Double src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class ByteSerializer implements JsonSerializer<Byte> {

        @Override
        public JsonElement serialize(Byte src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.intValue()) {
                return new JsonPrimitive(src.intValue());
            } else if(src == src.longValue()) {
                return new JsonPrimitive(src.longValue());
            } else if(src == src.shortValue()) {
                return new JsonPrimitive(src.shortValue());
            } else if(src == src.floatValue()) {
                return new JsonPrimitive(src.floatValue());
            } else if(src == src.doubleValue()) {
                return new JsonPrimitive(src.doubleValue());
            } else if(src == src.byteValue()) {
                return new JsonPrimitive(src.byteValue());
            }
            return new JsonPrimitive(src);
        }
    }

    static class BooleanSerializer implements JsonSerializer<Boolean> {

        @Override
        public JsonElement serialize(Boolean src, Type type, JsonSerializationContext jsonSerializationContext) {
            if(src == src.booleanValue()) {
                return new JsonPrimitive(String.valueOf(src.booleanValue()));
            }
            return new JsonPrimitive(src);
        }
    }

    static class DefaultTypeAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType == String.class) {
                return (TypeAdapter<T>) new StringTypeAdapter();
            } else if (rawType == Boolean.class) {
                return (TypeAdapter<T>) new BooleanTypeAdapter();
            }
            return null;
        }
    }

    static class StringTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            } else if (reader.peek() == JsonToken.BOOLEAN) {
                return String.valueOf(reader.nextBoolean());
            } else if (reader.peek() == JsonToken.NUMBER) {
                String s = null;
                try {
                    s = String.valueOf(reader.nextInt());
                } catch (Exception e) {
                }
                try {
                    s = String.valueOf(reader.nextLong());
                } catch (Exception e1) {
                }
                try {
                    s = String.valueOf(reader.nextDouble());
                } catch (Exception e2) {
                }
                if (s == null) {
                    return "0";
                }
                return s;
            }
            return reader.nextString();
        }
    }

    static class BooleanTypeAdapter extends TypeAdapter<Boolean> {
        @Override
        public void write(JsonWriter writer, Boolean value) throws IOException {
            if (value == null) {
                writer.value(false);
                return;
            }
            writer.value(value);
        }
        @Override
        public Boolean read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return false;
            } else if (reader.peek() == JsonToken.STRING) {
                return Boolean.valueOf(reader.nextString());
            }
            return reader.nextBoolean();
        }
    }
}
