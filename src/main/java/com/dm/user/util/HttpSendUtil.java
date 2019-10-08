package com.dm.user.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author cui
 * @date 2019/10/8
 * @hour 14
 */
public class HttpSendUtil {

    private static final String URL = "https://device.jpush.cn/v3/";
    private static final String APP_KEY = "f0e7dec1b2aefc534e08fe18";
    private static final String SECRET_KEY = "2464890880bcd3f52e00c1d7";
    /**
     * 构造Basic Auth认证头信息
     * @return authHeader
     */
    private static String getHeader() {
        String auth = APP_KEY + ":" + SECRET_KEY;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    /**
     * 查询别名绑定的设备
     * @param url
     * @param alias
     * @return
     * @throws Exception
     */
    public static String getData(String url, String alias) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpGet post = new HttpGet(URL + url + "/" + alias);
        String result = "";
        try {
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", getHeader());
            HttpResponse resp = closeableHttpClient.execute(post);
            InputStream respIs = resp.getEntity().getContent();
            byte[] respBytes = IOUtils.toByteArray(respIs);
            result = new String(respBytes, Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new Exception(e);
        }
        return result;
    }

    /**
     * 绑定别名
     * @param url
     * @param json
     * @throws Exception
     */
    public static void postData(String url, JSON json) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpPost post = new HttpPost(URL + url);
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder.build()) {
            HttpEntity entity = new StringEntity(json.toString(), "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", getHeader());
            closeableHttpClient.execute(post);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 删除别名
     * @param url
     * @param alias
     * @throws Exception
     */
    public static void deleteData(String url, String alias) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpDelete delete = new HttpDelete(URL + url + "/" + alias);
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder.build()) {
            delete.setHeader("Content-type", "application/json");
            delete.setHeader("Authorization", getHeader());
            closeableHttpClient.execute(delete);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
