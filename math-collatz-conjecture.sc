// summary : Collatz conjecture / conjecture de syracuse.
// run-with : scala-cli $file

/*
Collatz conjecture wikipedia : https://en.wikipedia.org/wiki/Collatz_conjecture
*/


def pathLengthToOne(from:Long):Int = {
  @annotation.tailrec
  def loop(current:Long, length:Int=0):Int = {
    if (current == 1) length else {
      if (current % 2 == 0) loop(current / 2, length + 1)
      else loop(3 * current +1, length + 1)
    }
  }
  loop(from)
}


def pathToOne(from:Long):List[Long] = {
  @annotation.tailrec
  def loop(current:Long, currentPath:List[Long]=Nil):List[Long] = {
    if (current == 1L) current::currentPath else {
      if (current % 2 == 0) loop(current / 2, current::currentPath)
      else loop(3 * current +1, current::currentPath)
    }
  }
  loop(from)
}


def highestPathLength(start:Long, end:Long):Int = {
  start.to(end).map(pathLengthToOne).max
}

println(pathToOne(12))

println(pathToOne(177))

println(pathToOne(18))


/*
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

object CollatzConjectureTest extends AnyFlatSpec with should.Matchers {
  "collatz conjecture path" `should` "start with 1" in {
    pathToOne(128).head shouldBe 1L
  }

  it `should` "last with its start value" in {
    pathToOne(17).last shouldBe 17L
  }

  it `should` "be possible to get the path up to 1" in {
    pathToOne(7) shouldBe List(1, 2, 4, 8, 16, 5, 10, 20, 40, 13, 26, 52, 17, 34, 11, 22, 7)
  }
}

CollatzConjectureTest.execute()
*/
