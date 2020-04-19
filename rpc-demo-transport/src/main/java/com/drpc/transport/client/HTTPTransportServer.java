package com.drpc.transport.client;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class HTTPTransportServer implements TransportServer {
    private RequestHandler handler;
    private Server server;
    //创建一个ServletContextServer类，用来初始化web应用程序的Context，并且指定Servlet和Servlet匹配的url。
    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.server = new Server(port);
        //servlet 接收请求
        ServletContextHandler ctx = new ServletContextHandler();
        server.setHandler(ctx);
        ServletHolder holder = new ServletHolder(new RequestServlet());
        //http://localhost:3000/*
        ctx.addServlet(holder,"/*");
    }

    @Override
    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
           log.error(e.getMessage(),e);
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
           log.error(e.getMessage(),e);
        }
    }

    class RequestServlet extends HttpServlet{
        // 处理客户端的post请求
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //super.doPost(req, resp);
           InputStream in =  req.getInputStream();
           OutputStream out = resp.getOutputStream();
           if(handler!=null){
               handler.onRequest(in,out);
           }
           out.flush();
        }
    }
}
