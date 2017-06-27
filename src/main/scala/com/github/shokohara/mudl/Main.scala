package com.github.shokohara.mudl

import java.io.File
import java.net.URL

import org.http4s.client.blaze._

import scala.sys.process._

object Main {
  val httpClient = PooledHttp1Client()

  def main(args: Array[String]): Unit = {
    (toCommand _ compose toM3u8)(args.head).foreach(_.!)
  }

  def toM3u8(url: String): List[(String, String)] = {
    val name: String = new URL(url).getPath.reverse.takeWhile(_ != '/').reverse
    s"""http://.*/$name/master.m3u8""".r.findFirstIn(httpClient.expect[String](url).unsafeValue.get).toList.distinct.map((s"$name.mp4", _))
  }

  def toCommand(details: List[(String, String)]): List[String] = details.filterNot(new File(new File(".").getAbsoluteFile.getParent).listFiles().map(_.getName).contains)
    .map(v => s"ffmpeg -i ${v._2} -c copy -bsf:a aac_adtstoasc -vcodec copy -c copy ${v._1}")

  def pages(url: String): List[String] = {
    val urls =
      """/category/[\w\-\.\/\?\,\#\:\u3000-\u30FE\u4E00-\u9FA0\uFF01-\uFFE3]*\?page=[0-9]*""".r.findAllIn(httpClient.expect[String](url).unsafeValue.get).toList.distinct ++
        """/search\?word=[\%\w]*\&page=[0-9]*""".r.findAllIn(httpClient.expect[String](url).unsafeValue.get).toList.distinct
    val pages = urls.flatMap("""page=[0-9]*""".r.findAllIn).distinct.map(_.diff("page=")).map(_.toInt)
    urls.headOption.map(_.replaceFirst("=[0-9]*", "=")).toList.flatMap { base =>
      (pages.min to pages.max).map(base + _)
    }
  }

  def words(protocolDomain:String) = {
    """search\?[\w\%=]*""".r.findAllIn(httpClient.expect[String](s"$protocolDomain/taglist").unsafeValue.get).toList.distinct.map(_.diff("search?")).map { word =>
      word
    }
  }

  def dynamicDetails(protocolDomain: String)(url: String): List[(String, String)] =
    """/detail/\w+""".r.findAllIn(httpClient.expect[String](url).unsafeValue.get).toList.distinct.map(_.diff("/detail/")).flatMap { name =>
      s"""http://.*/$name/master.m3u8""".r.findAllIn(httpClient.expect[String](s"$protocolDomain/detail/$name").unsafeValue.get).toList.distinct.map((s"$name.mp4", _))
  }
}
