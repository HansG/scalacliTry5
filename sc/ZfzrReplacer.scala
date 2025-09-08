/*
#!/usr/bin/env scala-cli
*/

//> using scala "3.7.1"
//> using toolkit "0.7.0"
//> using dep "com.lihaoyi::os-lib:0.11.5"


package sc


import scala.util.matching.Regex
import scala.util.matching.Regex.Match
import os.*

class ReplacerMult(
                     filterOpt:       Option[Path => Boolean],
                     val replaceSP:   Option[(String, String)],
                     val replacePairs: (Regex, Match => String)*
                  ):

   def replaceA(body: String): String =
      replaceSP.foldLeft(body) { (acc, pair ) =>
         acc.replaceAll(pair._1, pair._2)
      }

   def replaceB(body: String): String =
      replacePairs.foldLeft(body) { (acc, pair) =>
         pair._1.replaceAllIn(acc, pair._2)
      }

   def bearbeite(f: Path): Unit =
      val text   = read(f)
      println(text)

      val n0 = replaceA(text)
      println(n0)

      val n1 = replaceB(n0)
      println(n1)

      write.over(f, "")
      write.over(f, n1)

   def apply(dirr: Path): Unit =
      def accept(fp: Path): Boolean =
         val isFile = os.isFile(fp)
         filterOpt.fold(isFile)(pred => isFile && pred(fp))

      os.walk(dirr).filter(accept).foreach { f =>
         println(s"Bearbeite: $f")
         bearbeite(f)
      }
      println("...fertig")

   def apply(dirr: String): Unit =
      apply(Path(dirr))


object Regex2J extends ReplacerMult(
   None,
   None,
   // 1-digit
   ("&#x?([0-9a-fA-F]{1});".r, m => s"\\u000${m.group(1)}"),
   // 2-digit
   ("&#x?([0-9a-fA-F]{2});".r, m => s"\\u00${m.group(1)}"),
   // 3-digit
   ("&#x?([0-9a-fA-F]{3});".r, m => s"\\u0${m.group(1)}"),
   // 4-digit
   ("&#x?([0-9a-fA-F]{4});".r, m => s"\\u${m.group(1)}")
)

val zfzrJavahead = s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/vers8_0/verkehr/mserver/gen"

@main def setActionReplaceZfzr = SetActionReplace(Path(zfzrJavahead))


class Replacer(
                 val replacee: Regex,
                 val replaceTo: Match => String,
                 filterOpt:    Option[Path => Boolean] = None
              ) extends ReplacerMult(filterOpt, None, (replacee, replaceTo))


object SetActionReplace extends Replacer(
   "(?i)setAction\\(\"([^\"]+)\"\\);".r,
   _ =>
      """setAction("");
        |_operationClient.getOptions().setProperty("disableSoapAction", "true");
        |""".stripMargin,
   Some(path =>
      path.last.endsWith("ServiceStub.java") ||
         path.last.endsWith("GOETEStub.java")
   )
)

def gtueQNameReplacer(path: String, xgtaVers : String) =
   object GtueQNameReplacer extends Replacer(
      """new javax\.xml\.namespace\.QName\(([^\)]+)\)\w?\.\w?equals\(reader\.getName\(\)\)""".r,
      m =>
         s"new de.xgta.$xgtaVers.verkehr.gen.QNameNs(${m.group(1)}).equalsNs(reader.getName())"
   )
   GtueQNameReplacer(path)

def gtueQNameReplacer1(path: String, xgtaVers : String) =
   object GtueQNameReplacer1 extends ReplacerMult(
      None,
      Some(
         "org.apache.axis2.databinding.utils.ConverterUtil",
         "de.xgta.$xgtaVers.verkehr.gen.ConverterUtil"
      ),
      ("""new javax\.xml\.namespace\.QName\(([^\)]+)\)\w?\.\w?equals\(reader\.getName\(\)\)""".r,
         m => s"new de.xgta.$xgtaVers.verkehr.gen.QNameNs(${m.group(1)}).equalsNs(reader.getName())")
   )
   GtueQNameReplacer1(path)

object GtueQNameReplacer2 extends ReplacerMult(
   None,
   Some(
      "org.apache.axis2.databinding.utils.ConverterUtil",
      "de.xgta.$xgtaVers.verkehr.gen.ConverterUtil"
   )
)

def gtueQNameReplacer2(path: String, xgtaVers : String) =
   object MReplacer extends Replacer(
      """\.get\w""".r,
      m => s"new de.xgta.$xgtaVers.verkehr.gen.QNameNs(${m.group(1)}).equalsNs(reader.getName())"
   )
   MReplacer(path)

