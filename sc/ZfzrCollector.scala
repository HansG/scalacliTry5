/*
#!/usr/bin/env scala-cli
*/

//> using scala "3.7.1"
//> using dep "com.lihaoyi::os-lib:0.11.5"


package sc


import os._

import scala.util.matching.Regex
import scala.collection.mutable.Set as MSet

val srcbase = Path(s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/vers8_0")


def extractXmldsig(p : String) = {
   val rgx = "if \\(\"http://www.w3.org/20([^\\}]+)\\}".r
   rgx.findAllIn(read(Path(p))).foreach( println(_) )
}



object CollectNamespaces :   //extends App

   // Regex für die Namespace-Zeilen
   val genns: Regex =
      """if\s*\(namespace\.equals\(\"([^\"]+)\"\)\)\s*\{\s*return\s*\"([^\"]+)\";\s*\}""".r

   type NP = (String, String)

   def getNSet(src: Path): MSet[NP] =
      val set = MSet[NP]()
//      os.walk(src).filter(_.ext == "java").map(read).map { text =>
//         println(text)
//         genns.findFirstMatchIn(text).map { m =>
//            (m.group(1), m.group(2)) }.foreach(set += _) }

      //os.walk(src, _.ext != "java") funktioniert nicht, aber:
      os.walk(src).filter(_.ext == "java")
         .map(read)
         .foreach { text =>
            genns.findFirstMatchIn(text)
               .foreach { m => set += ((m.group(1), m.group(2))) }
         }
      set

   def eval(set: MSet[NP]): Map[String, MSet[String]] =
      set
         .groupBy(_._2)
         .filter((_, pairs) => pairs.size > 1)
         .map((abbr, pairs) => abbr -> pairs.map(_._1))

   def apply(src: Path): Unit =
      val nset       = getNSet(src)
      val doppelSet  = eval(nset)

      println("\nDoppelte\n")
      println(doppelSet.mkString("\n"))
      println("\n\n")

      val nsetr = nset.toSeq
      println("\nnach namespace\n")
      println(nsetr.sortBy(_._1).mkString("\n"))
      println("\n\n")

      //nach namespace-kürzel
      val doppelKuerzel = doppelSet.keySet
      println("\nnach namespace-kürzel\n")
      println(
         nset
            .filter((_, abbr) => doppelKuerzel.contains(abbr))
            .toSeq
            .map((ns, abbr) => (abbr, ns))
            .sortBy(_._1)
            .mkString("\n")
      )

//   apply(srcbase)


object CollectNamespaces1 {
   //      val genns = """if\(namespace\.equals\(\"([^\"]+)\"\)\)\{\s*return\s*\"([^\"]+)\";\s*\}""".r
   //      für formattierten Code:
   val genns = """if\s*\(namespace\.equals\(\"([^\"]+)\"\)\)\s*\{\s*return\s*\"([^\"]+)\";\s*\}""".r
   //> genns  : scala.util.matching.Regex = if\(namespace\.equals\(\"([^\"]+)\"\)\)
   //| \{\s*return\s*\"([^\"]+)\";\s*\}

   type NP = (String, String)

   import scala.collection.mutable.{Set => MSet}

   def getNSet(src: Path) = {
      val set = MSet[NP]()
      os.walk(src, _.ext != "java").map(read).map { text => genns.findFirstMatchIn(text).map { m => (m.group(1), m.group(2)) }.foreach(set += _) }
      set
   }

   def eval(set: MSet[NP]) = set.groupBy(p => p._2).filter(nsp => nsp._2.size > 1).map(nsp => (nsp._1, nsp._2.map(_._1)))

   def apply(src: Path) = {
      val nset = getNSet(src)
      //nicht eindeutige
      val doppelSet = eval(nset)
      println("/nDoppelte\n")
      println(doppelSet.mkString("\n"))
      println("\n\n")

      //Listen
      val nsetr = nset.toSeq
      //nach namespace
      println("/nnach namespace\n")
      println(nsetr.sortBy(np => np._1) mkString ("\n"))
      println("\n\n")

      //nach namespace-kürzel
      val doppelKuerzel = doppelSet.keySet
      println("/nnach namespace-kürzel\n")
      println(nset.filter(nsp => doppelKuerzel.contains(nsp._2)).toSeq.map(np => (np._2, np._1)).sortBy(np => np._1) mkString ("\n"))
   }

}
