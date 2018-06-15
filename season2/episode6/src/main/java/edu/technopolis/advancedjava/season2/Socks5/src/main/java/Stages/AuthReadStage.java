package Stages;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static Stages.Constants.AUTH_METHOD;
import static Stages.Constants.AUTH_REJECT;
import static Stages.Constants.SOCKS_VERSION;


public class AuthReadStage implements IStage {

    private ByteBuffer byteBuffer;
    private SocketChannel socketChannel;

    public AuthReadStage(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.socketChannel = socketChannel;
    }

    @Override
    public void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages) {

    }

    private void accept(Map<SocketChannel, IStage> stages) {
        byteBuffer.put(SOCKS_VERSION)
                .put(AUTH_METHOD);
        byteBuffer.flip();
        stages.put(socketChannel, new AuthWriteStage(socketChannel, byteBuffer, true));
    }

    private void reject(Map<SocketChannel, IStage> stages) {
        byteBuffer.put(SOCKS_VERSION)
                .put(AUTH_REJECT);
        byteBuffer.flip();
        stages.put(socketChannel, new AuthWriteStage(socketChannel, byteBuffer, false));
    }

    private boolean isAcceptableAuthMethod(int methodsNumber, ByteBuffer bb) {
        if (methodsNumber < 1) return false;
        for(int i = 0; i < methodsNumber; i++) {
            if (bb.get() == AUTH_METHOD) {
                return true;
            }
        }
        return false;
    }
}
