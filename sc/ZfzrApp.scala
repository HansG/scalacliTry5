/*
#!/usr/bin/env scala-cli
*/

//> using file ZfzrExtensionMapper.scala
//> using file ZfzrReplacer.scala
//> using file ZfzrCollector.scala
//> using scala "3.7.1"
//> using toolkit "0.7.0"
//> using dep "com.lihaoyi::os-lib:0.11.5"

import os.*
import sc.{CollectNamespaces, SetActionReplace}

// Version (aktualisieren falls nötig)
val vers = "vers8_0"

// Pfadkonstanten
val zfzrJavahead = s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/$vers/verkehr/mserver/gen"
val zevisWsgen = s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/ws-gen/src/de/zevis/$vers/mserver/gen"
val zevisJavahead = s"C:/se/intj/Projekte/obj743vkhilf/VkZevisKonnektor-Gen/src/main/javahead/de/zevis/$vers/mserver/gen"
val bqrJavahead = s"C:/se/intj/Projekte/obj743vkhilf/vkbqrk-gen/src/main/javahead/de/bqr/vers2_28_1/verkehr/gen"
val tuevrhJavagen = s"D:/se/projekte/obj743vkhilf/VkTuevRhLandKonnektor-Gen/trunk/src/main/java/de/akdb/ok/verkehr/tuevrhlnd/common/stub"
val gendir = zevisWsgen

// Weitere Beispiele
def srcbase = Path(s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/$vers")
val xgtaVers = "vers5_4"
val gtueGensrc = s"C:/se/intj/Projekte/obj743vkhilf/VkGtueKonnektor-Gen/trunk/src/main/javahead"
val gtueQNameEx = s"C:/se/intj/Projekte/obj743vkhilf/VkGtueKonnektor-Gen/trunk/src/main/javahead/de/gtue/vers4_1/schemata/xkfz/_4_1/TypeQuittungskopf.java"
val gtueGensrc1 = s"C:/se/intj/Projekte/obj743vkhilf/VkGtueKonnektor-Gen/trunk/src/main/javahead/de/gtue/vers4_1/code/_1_0"
val fswtm = s"D:/temp/del/dela1.txt"
val touchPath = s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/vers4_6/schemata/xkfz/_5"


//@main def mergeExtensionMapperB(): Unit =
def tryit = MergeExtensionMapper(
   Path(s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/$vers/zfzr/update/ExtensionMapperMerged.java"),
   Path(s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/$vers/zfzr/update/ExtensionMapper.java"),
   Path(s"C:/se/intj/Projekte/obj743vkhilf/ZfzrKonnektor/src/main/javahead/de/kba/$vers/zfzr/update/ExtensionMapperB.java")
                  )

//@main 
def setActionReplaceZfzr = SetActionReplace(Path(zfzrJavahead))

@main def doCollectNamespaces = CollectNamespaces(srcbase)
