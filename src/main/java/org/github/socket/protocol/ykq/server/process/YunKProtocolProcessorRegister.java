package org.github.socket.protocol.ykq.server.process;

import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;

import java.util.Map;

public class YunKProtocolProcessorRegister {
    public static final Map<String, MessageProcessor<XPacket>> PROCESS_STRATEGY = Map.of(
            "01", new LoginMessageProcessor(),
            "03", new HeartbeatFeelingProcessor(),
            "05", new BillingModelValidationRequestProcessor(),
            "09", new BillingModelRequestProcessor(),
            "13", new RealTimeDataProcessor()
    );
}
