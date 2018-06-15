package Stages;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;


public interface IStage {
    void proceed(SelectionKey selectionKey, Map<SocketChannel, IStage> stages);
}
