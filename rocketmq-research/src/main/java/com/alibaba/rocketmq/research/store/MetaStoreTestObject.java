/**
 * $Id: MetaStoreTestObject.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package com.alibaba.rocketmq.research.store;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.rocketmq.store.DefaultMessageStore;
import com.alibaba.rocketmq.store.MessageExtBrokerInner;
import com.alibaba.rocketmq.store.MessageStore;
import com.alibaba.rocketmq.store.PutMessageResult;
import com.alibaba.rocketmq.store.config.MessageStoreConfig;


/**
 * ���Դ洢�㣬�򵥷�װ
 * 
 * @author vintage.wang@gmail.com shijia.wxr@taobao.com
 * 
 */
public class MetaStoreTestObject {
    // ��Ϣ��С
    protected int MessageSize = 1024 * 2;
    // ���и���
    protected int QUEUE_TOTAL = 1024;
    // �����ĸ�����
    protected AtomicInteger queueId = new AtomicInteger(0);
    // ����������ַ
    protected SocketAddress bornHost;
    // �洢������ַ
    protected SocketAddress storeHost;
    // ��Ϣ��
    protected byte[] messageBody;

    //
    protected final MessageStore messageStore;


    public MetaStoreTestObject(final MessageStoreConfig messageStoreConfig) throws IOException {
        this.storeHost = new InetSocketAddress(InetAddress.getLocalHost(), 8123);
        this.bornHost = new InetSocketAddress(InetAddress.getByName("10.232.102.184"), 0);
        this.messageStore = new DefaultMessageStore(messageStoreConfig);
        this.messageBody = this.buildMessageBody();
    }


    private byte[] buildMessageBody() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MessageSize; i++) {
            sb.append("Y");
        }

        return sb.toString().getBytes();
    }


    public MessageExtBrokerInner buildMessage() {
        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setTopic("AAA");
        msg.setTags("TAG1");
        msg.setKeys(String.valueOf(this.messageStore.now()));
        msg.setBody(messageBody);
        msg.setKeys(String.valueOf(System.currentTimeMillis()));
        msg.setSysFlag(4);
        msg.setBornTimestamp(System.currentTimeMillis());

        return msg;
    }


    public boolean load() {
        return this.messageStore.load();
    }


    public void start() throws Exception {
        this.messageStore.start();
    }


    public boolean sendMessage() {
        PutMessageResult result = this.messageStore.putMessage(buildMessage());
        return result != null && result.isOk();
    }


    public void updateMasterAddress(final String addr) {
        this.messageStore.updateMasterAddress(addr);
    }


    public long now() {
        return this.messageStore.now();
    }
}