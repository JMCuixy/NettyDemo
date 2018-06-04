package org.netty.demo.netbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */
public class NetTest {

    public static void main(String[] args) {
        try {
            //1、查看本机的IP地址
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("本地的IP地址：" + localHost.getHostAddress());
            //2、查看某个地址的IP地址
            InetAddress byName = InetAddress.getByName("www.baidu.com");
            System.out.println("查看某个域名的IP地址：" + byName.getHostAddress());
            //3、URL：统一资源定位符（网址）
            URL url = new URL("http://www.baidu.com");
            InputStream inputStream = url.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine())!=null){
                if (line.contains("title")){
                    System.out.println(line);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
