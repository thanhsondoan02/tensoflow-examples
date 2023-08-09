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

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver

class HealthServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloWorldService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@HealthServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class HelloWorldService : HealthGrpc.HealthImplBase() {
        override fun check(
            request: HealthCheckRequest?,
            responseObserver: StreamObserver<HealthCheckResponse>?
        ) {
            super.check(request, responseObserver)
        }

        override fun watch(
            request: HealthCheckRequest?,
            responseObserver: StreamObserver<HealthCheckResponse>?
        ) {
            super.watch(request, responseObserver)
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = HealthServer(port)
    server.start()
    server.blockUntilShutdown()
}
