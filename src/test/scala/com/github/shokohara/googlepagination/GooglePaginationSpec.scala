package com.github.shokohara.googlepagination

import com.github.shokohara.googlepagination.GooglePagination._
import org.scalatest.{FreeSpec, Matchers}

class GooglePaginationSpec extends FreeSpec with Matchers {

  def in(i: Int)(f: Int => Any): Unit = s"page $i" in f(i)

  "rangeTuple" - {
    "0" in {
      sequenceTuple(0) should be(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    }
    "1" in {
      sequenceTuple(1) should be(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }
    "2" in {
      sequenceTuple(2) should be(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    }
  }
  "width 10" - {
    val width = 10
    "per 10" - {
      val per = 10
      "max 17" - {
        val max = 17
        (-1 to 1) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(false, Some(1), Some(2), None, None, None, None, None, None, None, None, true)
        })
        (2 to 3) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(true, Some(1), Some(2), None, None, None, None, None, None, None, None, false)
        })
      }
      "max 12346" - {
        val max = 12346
        (-1 to 1) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(false, sequenceTuple(1), true))
        })
        (2 to 6) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(1), true))
        })
        (7 to 14) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(page - 5), true))
        })
        (1225 to 1230) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(page - 5), true))
        })
        (1231 to 1234) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, true))
        })
        (1235 to 1236) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, false))
        })
      }
    }
    "per 20" - {
      val per = 20
      "max 17" - {
        val max = 17
        in(1) { page =>
          pagination(page, max, per, width) should be(false, Some(1), None, None, None, None, None, None, None, None, None, false)
        }
        // TODO
        //        (-1 to 2) foreach (in(_) { page =>
        //          pagination(page, max, per, width) should be(false, Some(1), None, None, None, None, None, None, None, None, None, false)
        //        })
      }
      "max 12346" - {
        val max = 12346
        (-1 to 1) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(false, sequenceTuple(1), true))
        })
        (2 to 6) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(1), true))
        })
        (7 to 14) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(page - 5), true))
        })
        (608 to 613) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, sequenceTuple(page - 5), true))
        })
        (614 to 617) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, true))
        })
        (618 to 620) foreach (in(_) { page =>
          pagination(page, max, per, width) should be(toPagination(true, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, false))
        })
      }
    }
  }
}
