package com.github.shokohara.googlepagination

import java.io.File

import scala.io.Source
import sys.process._

object Main {
  def main(args: Array[String]): Unit = {
    toCommand(toM3u8("http://aaaaa/detail/JFiM7T0Xmu")).foreach(_.!)
  }

  def toM3u8(url: String): List[(String, String)] = {
    val name = url.diff("http://aaaaa/detail/")
    s"""http://.*/$name/master.m3u8""".r.findFirstIn(Source.fromURL(url).mkString).toList.distinct.map((s"$name.mp4", _))
  }
  def toCommand(details: List[(String, String)]) = details.filterNot(new File("/Users/sho.kohara/m/").listFiles().map(_.getName).contains)
    .map(v => s"ffmpeg -i ${v._2} -c copy -bsf:a aac_adtstoasc -vcodec copy -c copy ${v._1}")

  def pages(url: String): List[String] = {
    val urls =
      """/category/[\w\-\.\/\?\,\#\:\u3000-\u30FE\u4E00-\u9FA0\uFF01-\uFFE3]*\?page=[0-9]*""".r.findAllIn(Source.fromURL(url).mkString).toList.distinct ++
        """/search\?word=[\%\w]*\&page=[0-9]*""".r.findAllIn(Source.fromURL(url).mkString).toList.distinct
    val pages = urls.flatMap("""page=[0-9]*""".r.findAllIn).distinct.map(_.diff("page=")).map(_.toInt)
    urls.headOption.map(_.replaceFirst("=[0-9]*", "=")).toList.flatMap { base =>
      (pages.min to pages.max).map(base + _)
    }
  }

  def words = {
    """search\?[\w\%=]*""".r.findAllIn(Source.fromURL("http://aaaaa/taglist").mkString).toList.distinct.map(_.diff("search?")).map { word =>
      // word=%E9%A8%8E%E4%B9%97%E4%BD%8D
      word
    }
  }

  def dynamicDetails(url: String): List[(String, String)] =
    """/detail/\w+""".r.findAllIn(Source.fromURL(url).mkString).toList.distinct.map(_.diff("/detail/")).flatMap { name =>
      println(name)
      s"""http://.*/$name/master.m3u8""".r.findAllIn(Source.fromURL(s"http://aaaaa/detail/$name").mkString).toList.distinct.map((s"$name.mp4", _))
  }
}
