/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 *  io.netty.bootstrap.Bootstrap
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelException
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelOption
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.SimpleChannelInboundHandler
 *  io.netty.channel.socket.nio.NioSocketChannel
 *  io.netty.util.concurrent.GenericFutureListener
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OldServerPinger {
    static final Splitter PING_RESPONSE_SPLITTER = Splitter.on((char)'\u0000').limit(6);
    static final Logger logger = LogManager.getLogger();
    private final List<NetworkManager> pingDestinations = Collections.synchronizedList(Lists.newArrayList());

    public void ping(final ServerData server) throws UnknownHostException {
        ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
        final NetworkManager networkmanager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort(), false);
        this.pingDestinations.add(networkmanager);
        server.serverMOTD = "Pinging...";
        server.pingToServer = -1L;
        server.playerList = null;
        networkmanager.setNetHandler(new INetHandlerStatusClient(){
            private boolean field_147403_d = false;
            private boolean field_183009_e = false;
            private long field_175092_e = 0L;

            @Override
            public void handleServerInfo(S00PacketServerInfo packetIn) {
                if (this.field_183009_e) {
                    networkmanager.closeChannel(new ChatComponentText("Received unrequested status"));
                } else {
                    this.field_183009_e = true;
                    ServerStatusResponse serverstatusresponse = packetIn.getResponse();
                    server.serverMOTD = serverstatusresponse.getServerDescription() != null ? serverstatusresponse.getServerDescription().getFormattedText() : "";
                    if (serverstatusresponse.getProtocolVersionInfo() != null) {
                        server.gameVersion = serverstatusresponse.getProtocolVersionInfo().getName();
                        server.version = serverstatusresponse.getProtocolVersionInfo().getProtocol();
                    } else {
                        server.gameVersion = "Old";
                        server.version = 0;
                    }
                    if (serverstatusresponse.getPlayerCountData() != null) {
                        server.populationInfo = (Object)((Object)EnumChatFormatting.GRAY) + String.valueOf(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount()) + (Object)((Object)EnumChatFormatting.DARK_GRAY) + "/" + (Object)((Object)EnumChatFormatting.GRAY) + serverstatusresponse.getPlayerCountData().getMaxPlayers();
                        if (ArrayUtils.isNotEmpty((Object[])serverstatusresponse.getPlayerCountData().getPlayers())) {
                            StringBuilder stringbuilder = new StringBuilder();
                            for (GameProfile gameprofile : serverstatusresponse.getPlayerCountData().getPlayers()) {
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append(gameprofile.getName());
                            }
                            if (serverstatusresponse.getPlayerCountData().getPlayers().length < serverstatusresponse.getPlayerCountData().getOnlinePlayerCount()) {
                                if (stringbuilder.length() > 0) {
                                    stringbuilder.append("\n");
                                }
                                stringbuilder.append("... and ").append(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() - serverstatusresponse.getPlayerCountData().getPlayers().length).append(" more ...");
                            }
                            server.playerList = stringbuilder.toString();
                        }
                    } else {
                        server.populationInfo = (Object)((Object)EnumChatFormatting.DARK_GRAY) + "???";
                    }
                    if (serverstatusresponse.getFavicon() != null) {
                        String s = serverstatusresponse.getFavicon();
                        if (s.startsWith("data:image/png;base64,")) {
                            server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length()));
                        } else {
                            logger.error("Invalid server icon (unknown format)");
                        }
                    } else {
                        server.setBase64EncodedIconData(null);
                    }
                    this.field_175092_e = Minecraft.getSystemTime();
                    networkmanager.sendPacket(new C01PacketPing(this.field_175092_e));
                    this.field_147403_d = true;
                }
            }

            @Override
            public void handlePong(S01PacketPong packetIn) {
                long i = this.field_175092_e;
                long j = Minecraft.getSystemTime();
                server.pingToServer = j - i;
                networkmanager.closeChannel(new ChatComponentText("Finished"));
            }

            @Override
            public void onDisconnect(IChatComponent reason) {
                if (!this.field_147403_d) {
                    logger.error("Can't ping " + server.serverIP + ": " + reason.getUnformattedText());
                    server.serverMOTD = (Object)((Object)EnumChatFormatting.DARK_RED) + "Can't connect to server.";
                    server.populationInfo = "";
                    OldServerPinger.this.tryCompatibilityPing(server);
                }
            }
        });
        try {
            networkmanager.sendPacket(new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS));
            networkmanager.sendPacket(new C00PacketServerQuery());
        }
        catch (Throwable throwable) {
            logger.error((Object)throwable);
        }
    }

    void tryCompatibilityPing(final ServerData server) {
        final ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>(){

            protected void initChannel(Channel p_initChannel_1_) {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                }
                catch (ChannelException channelException) {
                    // empty catch block
                }
                p_initChannel_1_.pipeline().addLast(new ChannelHandler[]{new SimpleChannelInboundHandler<ByteBuf>(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
                        super.channelActive(p_channelActive_1_);
                        ByteBuf bytebuf = Unpooled.buffer();
                        try {
                            bytebuf.writeByte(254);
                            bytebuf.writeByte(1);
                            bytebuf.writeByte(250);
                            char[] achar = "MC|PingHost".toCharArray();
                            bytebuf.writeShort(achar.length);
                            for (char c0 : achar) {
                                bytebuf.writeChar((int)c0);
                            }
                            bytebuf.writeShort(7 + 2 * serveraddress.getIP().length());
                            bytebuf.writeByte(127);
                            achar = serveraddress.getIP().toCharArray();
                            bytebuf.writeShort(achar.length);
                            for (char c1 : achar) {
                                bytebuf.writeChar((int)c1);
                            }
                            bytebuf.writeInt(serveraddress.getPort());
                            p_channelActive_1_.channel().writeAndFlush((Object)bytebuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
                        }
                        finally {
                            bytebuf.release();
                        }
                    }

                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_) {
                        String s;
                        String[] astring;
                        short short1 = p_channelRead0_2_.readUnsignedByte();
                        if (short1 == 255 && "\u00a71".equals((astring = (String[])Iterables.toArray((Iterable)PING_RESPONSE_SPLITTER.split((CharSequence)(s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() << 1).array(), Charsets.UTF_16BE))), String.class))[0])) {
                            int i = MathHelper.parseIntWithDefault(astring[1], 0);
                            String s1 = astring[2];
                            String s2 = astring[3];
                            int j = MathHelper.parseIntWithDefault(astring[4], -1);
                            int k = MathHelper.parseIntWithDefault(astring[5], -1);
                            server.version = -1;
                            server.gameVersion = s1;
                            server.serverMOTD = s2;
                            server.populationInfo = (Object)((Object)EnumChatFormatting.GRAY) + String.valueOf(j) + (Object)((Object)EnumChatFormatting.DARK_GRAY) + "/" + (Object)((Object)EnumChatFormatting.GRAY) + k;
                        }
                        p_channelRead0_1_.close();
                    }

                    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
                        p_exceptionCaught_1_.close();
                    }
                }});
            }
        })).channel(NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void pingPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkmanager = iterator.next();
                if (networkmanager.isChannelOpen()) {
                    networkmanager.processReceivedPackets();
                    continue;
                }
                iterator.remove();
                networkmanager.checkDisconnected();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearPendingNetworks() {
        List<NetworkManager> list = this.pingDestinations;
        synchronized (list) {
            Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
            while (iterator.hasNext()) {
                NetworkManager networkmanager = iterator.next();
                if (!networkmanager.isChannelOpen()) continue;
                iterator.remove();
                networkmanager.closeChannel(new ChatComponentText("Cancelled"));
            }
        }
    }
}

