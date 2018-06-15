package Stages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;


public class CommunicationStage implements IStage {

    private SocketChannel clientChannel;
    private SocketChannel serverChannel;
    private ByteBuffer clientBuffer;
    private ByteBuffer serverBuffer;

    public CommunicationStage(SocketChannel clientChannel, SocketChannel serverChannel, ByteBuffer clientBuffer, ByteBuffer serverBuffer) {
        this.clientChannel = clientChannel;
        this.serverChannel = serverChannel;
        this.clientBuffer = clientBuffer;
        this.serverBuffer = serverBuffer;
    }

    @Override
    public void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages) {
        Selector selector = selectionKey.selector();
        try {
            if (!clientChannel.isOpen()) {
                if (serverChannel.isOpen()) {
                    serverChannel.close();
                }
                return;
            }
            if (!serverChannel.isOpen()) {
                if (clientChannel.isOpen()) {
                    clientChannel.close();
                }
                return;
            }
            if (selectionKey.isReadable()) {
                if (clientBuffer.hasRemaining()) {
                    return;
                }
                clientBuffer.clear();
                int bytes = clientChannel.read(clientBuffer);
                clientBuffer.flip();
                if ( bytes > 0) {
                    serverChannel.register(selector, SelectionKey.OP_WRITE);
                } else {
                    if (clientChannel.isOpen()) {
                        clientChannel.close();
                    }
                    if (selector.isOpen()) {
                        serverChannel.close();
                    }
                }
            } else if(selectionKey.isWritable()) {
                while(serverBuffer.hasRemaining()) {
                    clientChannel.write(serverBuffer);
                }
                clientChannel.register(selector, SelectionKey.OP_READ);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
