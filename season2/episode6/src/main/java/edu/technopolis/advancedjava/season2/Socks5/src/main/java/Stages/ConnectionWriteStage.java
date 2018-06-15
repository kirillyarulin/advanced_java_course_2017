package Stages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;


public class ConnectionWriteStage implements IStage {

    private SocketChannel clientChannel;
    private SocketChannel serverChannel;
    private ByteBuffer clientBuffer;
    private ByteBuffer serverBuffer;
    private boolean accepted;

    public ConnectionWriteStage(SocketChannel clientChannel, SocketChannel serverChannel, ByteBuffer clientBuffer, ByteBuffer serverBuffer, boolean accepted) {
        this.clientChannel = clientChannel;
        this.serverChannel = serverChannel;
        this.clientBuffer = clientBuffer;
        this.serverBuffer = serverBuffer;
        this.accepted = accepted;
    }

    @Override
    public void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages) {
        Selector selector = selectionKey.selector();
        try {
            if (!clientChannel.isOpen()) {
                if (serverChannel.isOpen()) selector.close();
                return;
            }
            while(clientBuffer.hasRemaining()) {
                clientChannel.write(clientBuffer);
            }
            if (accepted) {
                serverBuffer.flip();
                stages.put(clientChannel, new CommunicationStage(clientChannel, serverChannel, clientBuffer, serverBuffer));
                stages.put(serverChannel, new CommunicationStage(serverChannel, clientChannel, serverBuffer, clientBuffer));
                clientChannel.register(selector, SelectionKey.OP_READ);
                serverChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("Connected " + serverChannel.getRemoteAddress());
            } else {
                System.out.println("Connection rejected " + serverChannel.toString());
            }
        }catch (IOException iOE) {
            iOE.printStackTrace();
        }
    }
}
