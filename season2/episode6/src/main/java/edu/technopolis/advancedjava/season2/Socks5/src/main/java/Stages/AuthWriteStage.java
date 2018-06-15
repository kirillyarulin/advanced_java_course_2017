package Stages;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class AuthWriteStage implements IStage {

    private boolean accepted;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer;

    public AuthWriteStage(SocketChannel socketChannel, ByteBuffer byteBuffer, boolean flag) {
        this.socketChannel = socketChannel;
        this.byteBuffer = byteBuffer;
        this.accepted = flag;
    }

    @Override
    public void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages) {
        try {
            if (!socketChannel.isOpen()) return;
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }

            byteBuffer.clear();

            if (accepted) {
                socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                stages.put(socketChannel, new ConnectionReadStage(socketChannel, byteBuffer));
            } else {
                if (socketChannel.isOpen()) {
                    socketChannel.close();
                }
            }
        } catch (IOException iOE) {
            iOE.printStackTrace();
        }
    }
}
