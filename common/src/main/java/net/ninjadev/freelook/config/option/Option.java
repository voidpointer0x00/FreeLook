package net.ninjadev.freelook.config.option;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class Option<T> {
    @Expose protected T value;
    @Expose protected String comment;

    public Option() {
    }

    public Option(T value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public T getValue() {
        return value;
    }

    public String getComment() {
        return this.comment;
    }

    public static class IntValue extends Option<Integer> {

        public IntValue(Integer value, String comment) {
            super(value, comment);
        }
    }

    public static class DoubleValue extends Option<Double> {
        public DoubleValue(Double value, String comment) {
            super(value, comment);
        }
    }

    public static class StringValue extends Option<String> {
        public StringValue(String value, String comment) {
            super(value, comment);
        }
    }

    public static class BooleanValue extends Option<Boolean> {
        public BooleanValue(Boolean value, String comment) {
            super(value, comment);
        }

        public void set(boolean value) {
            this.value = value;
        }
    }

    public static class ListValue<V> extends Option<List<V>> {
        public ListValue(List<V> value, String comment) {
            super(value, comment);
        }
    }


    public static class MapValue<K, V> extends Option<Map<K, V>> {
        public MapValue(Map<K, V> value, String comment) {
            super(value, comment);
        }
    }
}
