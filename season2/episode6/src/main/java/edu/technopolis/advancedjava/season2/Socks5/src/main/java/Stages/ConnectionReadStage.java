package Stages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;

import static Stages.Constants.*;


public class ConnectionReadStage implements IStage {

    private SocketChannel clientChannel;
    private SocketChannel serverChannel;
    private ByteBuffer byteBuffer;

    public ConnectionReadStage(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.clientChannel = socketChannel;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages) {
        Selector selector = selectionKey.selector();
        try {
            if (!clientChannel.isOpen()) return;
            int bytes = clientChannel.read(byteBuffer);
            byteBuffer.flip();
            if (bytes == -1) {
                if (clientChannel.isOpen()) clientChannel.close();
                System.out.println("Closing " + clientChannel.toString());
                return;
            }

            Optional<ByteBuffer> optional = Optional.of(byteBuffer);
            optional.filter(bb -> bb.position() < 10)
                    .filter(bb -> bb.get() == SOCKS_VERSION)
                    .filter(bb -> bb.get() == CMD_NUMBER)
                    .filter(bb -> bb.get() == RESERVED_BYTE)
                    .filter(bb -> bb.get() == ADDRESS_TYPE);
            if (!optional.isPresent()) {
                byteBuffer.clear();
                reject();
                stages.put(clientChannel, new ConnectionWriteStage(clientChannel, serverChannel, byteBuffer, null, false));
                clientChannel.register(selector, SelectionKey.OP_WRITE);
            }

            byte[] ipv4 = new byte[4];
            byteBuffer.get(ipv4);
            short port = byteBuffer.getShort();

            serverChannel = SocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.connect(new InetSocketAddress(InetAddress.getByAddress(ipv4), port));
            serverChannel.finishConnect();

            byteBuffer.clear();
            accept(ipv4, port);

            IStage stage = new ConnectionPendingStage(clientChannel, serverChannel, byteBuffer);
            stages.put(clientChannel, stage);
            stages.put(serverChannel, stage);

            serverChannel.register(selector, SelectionKey.OP_CONNECT);
            clientChannel.register(selector, SelectionKey.OP_READ);

        } catch (IOException iOE) {
            iOE.printStackTrace();
        }

    }

    private void accept(byte[] ip, short port) {
        byteBuffer.put(SOCKS_VERSION)
                .put(CONNECTION_PROVIDED_CODE)
                .put(RESERVED_BYTE)
                .put(ADDRESS_TYPE)
                .put(ip)
                .putShort(port);
        byteBuffer.flip();
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
