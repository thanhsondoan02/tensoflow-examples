/*
 * Copyright 2020 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.audio

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.tensorflow.lite.examples.audio.HealthGrpc.HealthStub
import java.io.Closeable
import java.util.concurrent.TimeUnit

class HealthClient(private val channel: ManagedChannel) : Closeable {
    private val stub: HealthStub = HealthGrpc.newStub(channel)

    fun greet(callback: (String) -> Unit = {}) {
        stub.check(HealthCheckRequest.newBuilder().build(), object :
            StreamObserver<HealthCheckResponse> {
            override fun onNext(value: HealthCheckResponse?) {
                println("Received: ${value?.status}")
                callback.invoke(value?.status.toString())
            }

            override fun onError(t: Throwable?) {
                println("Error: ${t?.message}")
                callback.invoke(t?.message.toString())
            }

            override fun onCompleted() {
                println("Completed")
                callback.invoke("Completed")
            }
        })
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

/**
 * Greeter, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
fun main(args: Array<String>) {
    val port = 50051

    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

    val client = HealthClient(channel)

    val user = args.singleOrNull() ?: "world"
//    client.greet(user)
}
