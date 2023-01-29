package com.qianlihu.solanawallet.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * author : Duan
 * date   : 2021/4/89:31
 * desc   :
 * version: 1.0.0
 */
public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private Type mType;


    BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type mType) {
        this.gson = gson;
        this.adapter = adapter;
        this.mType = mType;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String jsonString = buffer.readUtf8();
        Gson gsons  = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        ApiResponse response = gsons.fromJson(jsonString, ApiResponse.class);
        try {
            JSONObject object = new JSONObject(jsonString);
            int code = response.getCode();
            String msg = response.getMsg();
            if (code != 1) {
                response.setCode(code);
                response.setMsg(msg);
                response.setData(response.getData());
                throw new BaseException(msg, code);
            } else {
                return gson.fromJson(jsonString, mType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //数据解析异常
            throw new BaseException(BaseException.PARSE_ERROR_MSG, BaseException.PARSE_ERROR);
        } finally {
            value.close();
        }
    }
}
