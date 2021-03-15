package br.com.zup.config

import br.com.zup.KeyServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun keyClientStub(@GrpcChannel("key") channel: ManagedChannel) : KeyServiceGrpc.KeyServiceBlockingStub {
        return KeyServiceGrpc.newBlockingStub(channel)
    }
}