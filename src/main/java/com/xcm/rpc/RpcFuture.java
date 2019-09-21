package com.xcm.rpc;

import com.xcm.proto.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcFuture implements Future<Protocol.Response> {
    private CountDownLatch latch = new CountDownLatch(1);
    private Protocol.Response response;

    private List<RpcCallback> callbackList = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public Protocol.Response get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    @Override
    public Protocol.Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        return response;
    }

    public void done(Protocol.Response response) {
        this.response = response;
        latch.countDown();


    }

    public void doAllCallback() {
        lock.lock();
        try {
            for (RpcCallback rpcCallback : callbackList) {
                doCallback(rpcCallback);

            }
        } finally {
            lock.unlock();
        }
    }

    public void doCallback(RpcCallback rpcCallback) {
        if (response.getHeader().getState() == 1) {
            rpcCallback.onSuccess(response);
        } else {
            rpcCallback.onFail(response);

        }
    }

    public void addCallback(RpcCallback rpcCallback) {
        lock.lock();
        try {
            if (isDone()) {
                doCallback(rpcCallback);
            } else {
                callbackList.add(rpcCallback);
            }
        } finally {
            lock.lock();

        }


    }
}
