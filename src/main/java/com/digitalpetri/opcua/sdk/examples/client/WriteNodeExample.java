package com.digitalpetri.opcua.sdk.examples.client;

import java.util.Random;

import com.digitalpetri.opcua.sdk.client.OpcUaClient;
import com.digitalpetri.opcua.sdk.client.api.nodes.attached.UaVariableNode;
import com.digitalpetri.opcua.stack.core.security.SecurityPolicy;
import com.digitalpetri.opcua.stack.core.types.builtin.DataValue;
import com.digitalpetri.opcua.stack.core.types.builtin.NodeId;
import com.digitalpetri.opcua.stack.core.types.builtin.StatusCode;
import com.digitalpetri.opcua.stack.core.types.builtin.Variant;

public class WriteNodeExample extends AbstractClientExample {

    public static void main(String[] args) throws Exception {
        String endpointUrl = "opc.tcp://localhost:12685/digitalpetri";
        SecurityPolicy securityPolicy = SecurityPolicy.None;

        WriteNodeExample example = new WriteNodeExample(endpointUrl, securityPolicy);

        example.shutdownFuture(example.client).get();
    }

    private final OpcUaClient client;

    public WriteNodeExample(String endpointUrl, SecurityPolicy securityPolicy) throws Exception {
        client = createClient(endpointUrl, securityPolicy);

        // synchronous connect
        client.connect().get();

        NodeId nodeId = new NodeId(2, "/Static/AllProfiles/Scalar/Int32");

        UaVariableNode variableNode = client.getAddressSpace().getVariableNode(nodeId);

        // read the existing value
        Object valueBefore = variableNode.readValueAttribute().get();
        logger.info("valueBefore={}", valueBefore);

        // write a new random value
        DataValue newValue = new DataValue(new Variant(new Random().nextInt()));
        StatusCode writeStatus = variableNode.writeValue(newValue).get();
        logger.info("writeStatus={}", writeStatus);

        // read the value again
        Object valueAfter = variableNode.readValueAttribute().get();
        logger.info("valueAfter={}", valueAfter);
    }

}
