import os.{Path, read, write}

import scala.collection.mutable
import scala.collection.mutable.Map as MMap

type SS = (String, String)
type MM = MMap[SS, String]

object MergeExtensionMapper:
   val apairs = """\s*if\s*\(\s*\"([^\"]+)\"[^\"]+\"([^\"]+)\"[^r]+return\s*([^;]+);""".r //


   def cmap(file: Path): MM = cmap(read(file))

   def cmap(text: String): MM = apairs.findAllMatchIn(text).foldLeft(mutable.Map[SS, String]())((map, m) => {
      map += (m.group(1), m.group(2)) -> m.group(3)
   })


   // Verschmelze mehrere Maps, erstes Vorkommen gewinnt
   def mergeMaps(maps: Seq[MM]): MM =
      val allKeys = maps.foldLeft[Set[SS]](Set.empty[SS])((s0, mm) => s0 ++ mm.keySet)
      allKeys.iterator.foldLeft[MM](mutable.Map[SS, String]()) { (acc, key) =>
         val value = maps.collectFirst { case m if m.contains(key) => m(key) }
            .getOrElse(s"FEHLER: kein Wert für $key")
         acc += key -> value
      }

   // Erzeuge Scala-Code-Snippet
   def codeLine(kv: ((String, String), String)): String =
      s"""      if ("${kv._1._1}".equals(namespaceURI) && "${kv._1._2}".equals(typeName)) {
         |        return ${kv._2};
         |      }
         |""".stripMargin

   def toCode(map: MM): String =
      val buf = new StringBuilder
      map.foreach(kv => buf.append(codeLine(kv)))
      buf.toString()

   // Lese, merge und schreibe
   def apply(target: Path, gens: Path*): Unit =
      val maps = gens.map(cmap)
      maps foreach { m => println("\n\n\n/nMap:" + m.toString() + "\n\n\n\n") }
      val mergedCode = toCode(mergeMaps(maps))
      println(s"\n--- Generated Code ---\n$mergedCode")
      write(target, mergedCode)

end MergeExtensionMapper



