package net.garethrogers.str8ur.servers

import io.netty.channel.ChannelInitializer
import io.netty.channel.Channel
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpContentCompressor
import net.garethrogers.str8ur.Str8urApp

class HttpPipelineInitializer(val app: Str8urApp) extends ChannelInitializer[Channel]:
  override def initChannel(ch: Channel): Unit =
    val pipeline = ch.pipeline
    pipeline.addLast("codec", HttpServerCodec())
    pipeline.addLast("aggregator", HttpObjectAggregator(512 * 1024))
    pipeline.addLast("compressor", HttpContentCompressor())
    pipeline.addLast("myhandler", HttpRequestHandler(app))
