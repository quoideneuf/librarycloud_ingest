package edu.harvard.libcomm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.log4j.Logger;

import com.amazonaws.services.sqs.AmazonSQSAsync;

public class AmazonSQSAsyncShim implements InvocationHandler {
    protected Logger log = Logger.getLogger(AmazonSQSAsyncShim.class);

    private AmazonSQSAsync delegate;

    public AmazonSQSAsync bind(AmazonSQSAsync delegate) {
        this.delegate = delegate;
        AmazonSQSAsync proxy = (AmazonSQSAsync) Proxy.newProxyInstance(
                                                                       this.delegate.getClass().getClassLoader(),
                                                                       this.delegate.getClass().getInterfaces(),
                                                                       this);
        return proxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.debug("SQS Client Shim " + method.getName());
        try {
            for (Object arg : args) {
                log.debug(arg);
            }
        } catch (Exception e) {
        }

        Object result = null;
        try {
            if (method.getName() == "deleteMessage") {
                log.debug("replacing deleteMessage with deleteMessageAsync");
                method = this.delegate.getClass().getMethod("deleteMessageAsync", null);
            } else {
                result = method.invoke(this.delegate, args);
            }
        } catch (Exception e) {
            // log.debug("oh no, maybe this SQS client doesn't do: "+method.getName()+"!");
            // log.debug(e.printStackTrace());
        }
        return result;
    }

}
