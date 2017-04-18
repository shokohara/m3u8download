package com.github.shokohara.googlepagination

import shapeless.Poly1
import shapeless.poly.identity
import shapeless.syntax.std.tuple._

case class GooglePagination(previous: Boolean, pages: List[Int], next: Boolean, maxPageNumber: Int)

object GooglePagination {

  object wrapIntInOption extends Poly1 {
    implicit def caseBool = at[Boolean](identity)
    implicit def caseInt = at[Int](Some.apply)
  }

  type Pagination = (Boolean, Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Boolean)

  def sequenceTuple(s: Int): (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) =
    (s + 0, s + 1, s + 2, s + 3, s + 4, s + 5, s + 6, s + 7, s + 8, s + 9)
  def toPagination(v: (Boolean, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Boolean)): Pagination =
    v.map(wrapIntInOption)
  def toPagination(v: (Boolean, (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int), Boolean)): Pagination =
    toPagination(v._1 +: v._2 :+ v._3)

  def maxPageNumber(max: Int, per: Int) = scala.math.ceil(max.toDouble / per.toDouble).toInt
  def firstPaginationNumber(page: Int, max: Int, per: Int, width: Int): Int = {
    val maxPage = this.maxPageNumber(max, per)
    if (page < 1 + width / 2) 1
    else if (maxPage - width / 2 < page) maxPage - width + 1
    else page - width / 2
  }
  def pagination(page: Int, max: Int, per: Int, width: Int): Pagination = {
    val maxPage = this.maxPageNumber(max, per)
    val start = firstPaginationNumber(page, max, per, width)
    object limit extends Poly1 {
      implicit def caseInt = at[Int](n => if (start + n <= maxPage) Some(start + n) else None)
    }
    (1 < page) +: sequenceTuple(0).map(limit) :+ (page < maxPage)
  }
  def paginate(page: Int, max: Int, per: Int, width: Int): GooglePagination = {
    val p = pagination(page, max, per, width)
    val ps: List[Int] = p.tail.init.toList[Option[Int]].flatten
    GooglePagination(p.head, ps, p.last, maxPageNumber(max, per))
  }
}
