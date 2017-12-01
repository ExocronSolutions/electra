package io.electra.client;

import io.electra.common.server.ElectraChannelInitializer;
import io.electra.common.server.ElectraThreadFactory;
import io.electra.common.server.PipelineUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.codec.Charsets;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class DefaultElectraClient implements ElectraClient {

    private String host;

    private int port;

    private Channel channel;

    private ElectraBinaryHandler electraBinaryHandler;

    public DefaultElectraClient(String host, int port) {
        this.host = host;
        this.port = port;

        connect();
    }

    private void connect() {
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(PipelineUtils.newEventLoopGroup(2, new ElectraThreadFactory("Electra Client Thread")))
                    .channel(NioSocketChannel.class)
                    .handler(new ElectraChannelInitializer(electraBinaryHandler = new ElectraBinaryHandler()));

            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void get(String key, Consumer<String> consumer) {
        int keyHash = Arrays.hashCode(key.getBytes(Charsets.UTF_8));

        int callbackId = ElectraBinaryHandler.callbackId.incrementAndGet();

        ByteBuf byteBuf = Unpooled.buffer().writeByte(0).writeInt(callbackId).writeInt(keyHash);

        electraBinaryHandler.send(byteBuf, bytes -> consumer.accept(new String(bytes, Charsets.UTF_8)), callbackId);
    }

    @Override
    public void get(byte[] keyBytes, Consumer<byte[]> consumer) {
        int keyHash = Arrays.hashCode(keyBytes);

        int callbackId = ElectraBinaryHandler.callbackId.incrementAndGet();

        ByteBuf byteBuf = Unpooled.buffer().writeByte(0).writeInt(callbackId).writeInt(keyHash);

        electraBinaryHandler.send(byteBuf, consumer, callbackId);
    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public void put(byte[] key, byte[] value) {

    }

    @Override
    public void disconnect() {
        channel.close();
    }
}