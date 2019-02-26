package com.alibaba.ttl.xiejj;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.Utils;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiejinjun
 * @version 1.0 2018/8/12
 */
public class Test {
    private static TransmittableThreadLocal<Context> contextInfo = new TransmittableThreadLocal<>();
    private static ExecutorService requestThreadPool = Executors.newFixedThreadPool(5);
    private static ExecutorService workThreadPool = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));

    static {
        Utils.expandThreadPool(requestThreadPool);
        Utils.expandThreadPool(workThreadPool);
    }
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++){
            int finalI1 = i;
            requestThreadPool.submit(() -> {
                Context context = new Context();
                context.setBrandId("" + finalI1);
                context.setMsgId("" + finalI1);
                contextInfo.set(context);
                int finalI = finalI1;
                workThreadPool.submit(() -> {
                    System.out.println("brandId应该为：" + finalI + ";实际值为： " + contextInfo.get().getBrandId());
                    System.out.println("msgId应该为：" + finalI + "; 实际值为：" + contextInfo.get().getMsgId());
                    System.out.println();
                });
            });
        }
    }



    static class Context{
        String brandId;
        String msgId;

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
    }
}
