package com.jikexueyuan.rpc.invoke;

import com.jikexueyuan.rpc.exception.RpcException;
import com.jikexueyuan.rpc.exception.RpcExceptionCodeEnum;
import com.jikexueyuan.rpc.zookeeper.ZookeeperClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by version_z on 2015/8/22.
 */
public class ConsumerConfig
{
    private String url;
    private ZookeeperClient client;
    private final ConcurrentHashMap<Class,AtomicInteger> invokeCount = new ConcurrentHashMap<Class, AtomicInteger>();

    
    public String getUrl(Class clazz) {
    	if (client != null)
        {
            List<String> urlList = new ArrayList<String>();
            List<String> pathList = client.getChildren("/rpc/"+clazz.getName().replaceAll("\\.","/"));
            for (String path:pathList)
            {
                String httpUrl = client.getData("/rpc/"+clazz.getName().replaceAll("\\.","/")+"/"+path);
                if (httpUrl != null)
                {
                    urlList.add(httpUrl);
                }
            }
            return getCurrentUrl(clazz, urlList);
        }
        else
        {
            return url;
        }
    	
    }

    public String getCurrentUrl(Class clazz,List<String> urlList) throws RpcException {
        if (invokeCount.get(clazz) == null)
        {
            invokeCount.putIfAbsent(clazz,new AtomicInteger(1));
            return urlList.get(0);
        }
        else
        {
            int i = invokeCount.get(clazz).incrementAndGet();
            return urlList.get(i%urlList.size());
        }
    }
    
    public void setUrl(String url) {
    	this.url = url;
        if (url.toLowerCase().startsWith("zookeeper://"))
        {
            client = new ZookeeperClient(url.toLowerCase().replaceFirst("zookeeper://",""));
        }
    }
}
