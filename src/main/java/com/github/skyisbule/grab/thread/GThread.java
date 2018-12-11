package com.github.skyisbule.grab.thread;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;


import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class GThread extends Thread{

    public GThread(String propertiesName){
        this.propertiesName = propertiesName;
        try {
            init();
        } catch (IOException e) {
            System.out.println("初始化失败");
            e.printStackTrace();
        }
    }

    private String  url;
    private String  cookie;
    private HashMap<String,Object> param;
    private String propertiesName;
    private String courseName;
    private long   sleepTime;

    public void run(){
        int count=0;
        String result;
        while (true){
            result = doRequest();
            if (courseName == null) getCourseName(result);
            result = result.substring(37,44);
            if (result.indexOf("成功")>0){
                System.out.println(courseName+"抢课结果："+result);
                break;
            }
            System.out.println(count+" "+courseName+" "+result);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }

    }

    private void getCourseName(String result){
        courseName = result.substring(result.indexOf("课程名称")+4,result.indexOf("课程名称")+18);
        courseName = courseName.substring(0,courseName.indexOf("&"));
    }

    private void init() throws IOException {
        ClassPathResource resource = new ClassPathResource(propertiesName);
        Properties properties = new Properties();
        properties.load(resource.getStream());

        url = (String)properties.get("requestUrl");
        String viewState = (String) properties.get("VIEWSTATE");
        String xkkh = (String) properties.get("xkkh");
        cookie = (String)properties.get("cookie");
        sleepTime = Long.parseLong(properties.get("sleepTime").toString());

        param = new HashMap<String, Object>();
        param.put("__EVENTTARGET","Button1");
        param.put("__EVENTARGUMENT","");
        param.put("__VIEWSTATE", viewState);
        param.put("RadioButtonList1",0);
        param.put("xkkh", xkkh);
    }

    private String doRequest(){

        return HttpRequest.post(url)
                .header(Header.USER_AGENT, "chrome")
                .header("Cookie", "ASP.NET_SessionId="+cookie)
                .form(param)
                .timeout(20000)
                .execute().body();
    }

}
