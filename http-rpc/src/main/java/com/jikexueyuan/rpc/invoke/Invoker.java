package com.jikexueyuan.rpc.invoke;

import com.jikexueyuan.rpc.exception.RpcException;
import com.jikexueyuan.rpc.serialize.Request;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by version_z on 2015/8/22.
 */
public interface Invoker
{
    /**
     * ��������
     * @param request ������
     * @param url ����������
     * @return
     * @throws RpcException
     */
    String request(String request,String url) throws RpcException;

    /**
     * ����Ӧ��
     * @param response ��Ӧ����
     * @param outputStream �����
     * @throws RpcException
     */
    void response(String response,OutputStream outputStream) throws RpcException;
}
