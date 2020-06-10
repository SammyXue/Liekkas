package com.liekkas.core.rpc;

import com.liekkas.core.message.proto.Protocol;

/**
 * @author xuecm
 */
public interface RpcCallback {
    void onFail(Protocol.Response response);

    void onSuccess(Protocol.Response response);
}
