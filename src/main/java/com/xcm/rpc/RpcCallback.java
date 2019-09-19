package com.xcm.rpc;

import com.xcm.proto.Protocol;

/**
 * @author xuecm
 */
public interface RpcCallback {
    void onFail(Protocol.Response response);

    void onSuccess(Protocol.Response response);
}
