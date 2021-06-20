package com.nuce.duantp.sunshine.kafka;

import com.google.gson.Gson;

public class KafkaConvertResult<T> {
    private Class<T> deserializedClass;

    public KafkaConvertResult(Class<T> deserializedClass) {
        this.deserializedClass = deserializedClass;
    }

    public T convert(String data) {
        Gson gson = new Gson();
        T res = gson.fromJson(new String(data), deserializedClass);
        return res;
    }
}
