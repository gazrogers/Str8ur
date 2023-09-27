package net.garethrogers.str8ur.servers

import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpUtil
import io.netty.handler.codec.http.HttpVersion
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.QueryStringDecoder
import io.netty.buffer.Unpooled
import io.netty.util.CharsetUtil
import scala.jdk.CollectionConverters.*
import scala.collection.mutable.HashMap
import scala.collection.mutable.{Map => MutableMap}
import scala.collection.immutable.Queue
import net.garethrogers.str8ur.Str8urApp
import net.garethrogers.str8ur.HttpRequest
import java.util.Calendar

class HttpRequestHandler(app: Str8urApp) extends SimpleChannelInboundHandler[FullHttpRequest]:
  override def channelReadComplete(ctx: ChannelHandlerContext): Unit =
    ctx.flush

  override def channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest): Unit =
    if HttpUtil.is100ContinueExpected(request) then
      ctx.write(DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE))
    val urlDecoder = QueryStringDecoder(request.uri)
    val str8urRequest = HttpRequest(
      urlDecoder.path(),
      request.uri,
      getQueryParams(urlDecoder.parameters().asScala),
      request.method.name,
      getHeaders(request),
      if request.content.readableBytes > 0 then request.content.toString(CharsetUtil.UTF_8) else ""
    )
    println(str8urRequest)
    val myResponse = app.handleRequest(str8urRequest)
    val content = Unpooled.copiedBuffer(myResponse.body, CharsetUtil.UTF_8)
    println("[" + Calendar.getInstance.getTime + "]: " + urlDecoder.path() + ", " + HttpResponseStatus.valueOf(myResponse.code))
    val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(myResponse.code), content)
    ctx.writeAndFlush(response)
    ctx.close

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit =
    cause.printStackTrace
    ctx.close

  private def getHeaders(request: FullHttpRequest): HashMap[String, Queue[String]] =
    request.headers.asScala.foldLeft(HashMap[String, Queue[String]]()) {(map, header) =>
      map.clone.addOne(header.getKey(), map.getOrElse(header.getKey(), Queue()).enqueue(header.getValue))
    }

  private def getQueryParams(params: MutableMap[String, java.util.List[String]]): Map[String, List[String]] =
    params.map((key, value) => key -> value.asScala.toList).toMap

