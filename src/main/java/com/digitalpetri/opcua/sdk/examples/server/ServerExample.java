package com.digitalpetri.opcua.sdk.examples.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.digitalpetri.opcua.sdk.server.OpcUaServer;
import com.digitalpetri.opcua.sdk.server.api.config.OpcUaServerConfig;
import com.digitalpetri.opcua.server.ctt.CttNamespace;
import com.digitalpetri.opcua.stack.core.types.builtin.LocalizedText;

public class ServerExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ServerExample serverExample = new ServerExample();
        serverExample.startup();

        serverExample.shutdownFuture().get();
    }

    private final OpcUaServer server;

    public ServerExample() {
        OpcUaServerConfig config = OpcUaServerConfig.builder()
                .setApplicationName(LocalizedText.english("digitalpetri opc-ua server"))
                .setApplicationUri("urn:digitalpetri:opcua:server")
                .setProductUri("urn:digitalpetri:opcua:sdk")
                .setServerName("digitalpetri")
                .build();

        server = new OpcUaServer(config);

        // register a CttNamespace so we have some nodes to play with
        server.getNamespaceManager().registerAndAdd(
                CttNamespace.NAMESPACE_URI,
                idx -> new CttNamespace(server, idx));
    }

    public void startup() {
        server.startup();
    }

    private CompletableFuture<Void> shutdownFuture() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            future.complete(null);
        }));

        return future;
    }

}
