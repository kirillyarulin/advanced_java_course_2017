package Stages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static Stages.Constants.*;


public class ConnectionPendingStage implements IStage {

    private SocketChannel clientChannel;
    private SocketChannel serverChannel;
    private ByteBuffer byteBuffer;

    public ConnectionPendingStage(SocketChannel clientChannel, SocketChannel serverChannel, ByteBuffer byteBuffer) {
        this.clientChannel = clientChannel;
        this.serverChannel = serverChannel;
        this.byteBuffer = byteBuffer;
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

            if (serverChannel.finishConnect()) {
                IStage stage = new ConnectionWriteStage(clientChannel, serverChannel, byteBuffer, ByteBuffer.allocate(BUFFER_SIZE), true);
                stages.put(clientChannel, stage);
                stages.put(serverChannel, stage);
                serverChannel.register(selector, SelectionKey.OP_READ);
                clientChannel.register(selector, SelectionKey.OP_WRITE);
            } else {
                byteBuffer.clear();
                reject();
                clientChannel.register(selector, SelectionKey.OP_WRITE);
                stages.put(clientChannel, new ConnectionWriteStage(clientChannel, serverChannel, byteBuffer, ByteBuffer.allocate(BUFFER_SIZE), false));
                if(serverChannel.isOpen()) serverChannel.close();
            }
        } catch (IOException iOE) {
            iOE.printStackTrace();
        }
    }

    private void reject() {
        byteBuffer.put(SOCKS_VERSION)
                .put(RESERVED_BYTE)
                .put(ADDRESS_TYPE)
                .put(new byte[4])
                .putShort((short) 0);
        byteBuffer.flip();
    }
}
