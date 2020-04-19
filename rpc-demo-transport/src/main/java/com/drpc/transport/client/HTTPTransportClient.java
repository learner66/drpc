package com.drpc.transport.client;

import com.drpc.Peer;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 客户端逻辑的实际实现
 */
public class HTTPTransportClient implements TransportClient {
    private String url;

    /**
     * 连接服务端
     * @param peer 服务端地址
     */
    @Override
    public void connect(Peer peer) {
        this.url = "http://"+peer.getHost()+":"+peer.getPort();
    }

    @Override
    public InputStream write(InputStream data) {
        try {
            //向指定地址开始连接，HttpURLConnection是java.net包下，是JDK支持的，它主要用来服务器端发送http请求。
            //urlConnection对象实际上是根据URL的请求协议生成的URLConnection类，所以可以将urlConnection转为HttpURLConnection
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            // 设置是否向httpUrlConnection输出，默认情况是false；因为该请求为post请求，参数要放在http正文内，
            //因此需要设为true
            httpConn.setDoOutput(true);
            // 设置是否从HttpURLConnection读入，默认情况是true;
            httpConn.setDoInput(true);
            //Post请求不能使用缓存
            httpConn.setUseCaches(false);
            //请求的类型POST
            httpConn.setRequestMethod("POST");
            //连接，getOutputStream会隐含的进行connect，所以开发中不调用connect也可以
            httpConn.connect();
            //这个方法将内容按字节从一个InputStream对象复制到一个OutputStream对象
            IOUtils.copy(data,httpConn.getOutputStream());
            int resultCode = httpConn.getResponseCode();
            if(resultCode == HttpURLConnection.HTTP_OK){
                //获取数据
                return httpConn.getInputStream();
            }else{
                return httpConn.getErrorStream();
            }
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {

    }
}
