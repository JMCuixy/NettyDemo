package org.netty.demo.netbase;

import java.io.*;
import java.net.*;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */

public class NetTest {

    public static void main(String[] args) {
        uRLTest();
    }


    public static void uRLTest(){
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
            String line;
            while ((line = bufferedReader.readLine())!=null){
                System.out.println(line);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uRLConnectionTest(){
        try {
            URL url = new URL("http://restapi.amap.com/v3/config/district?key=2c95fdacd3f72bdbfec55bd7eac7b5c0");
            //1、创建连接对象
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //2、设置参数和一般请求属性
            //应用程序要将参数写入URL连接
            httpURLConnection.setDoOutput(true);
            //应用程序要向URL连接读取数据
            httpURLConnection.setDoInput(true);
            //不使用缓存
            httpURLConnection.setUseCaches(false);
            //设置请求参数
            httpURLConnection.setRequestProperty("Content-type","application/json");
            //设置请求方法
            httpURLConnection.setRequestMethod("POST");
            //3、使用connect方法建立到远程对象的实际链接
            httpURLConnection.connect();
            //4、远程对象变为输入输出流，根据需求进一步操作
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\data.json"));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
