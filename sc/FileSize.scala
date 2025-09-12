//> using scala "3.7.1"
//> using dep com.lihaoyi::os-lib::0.11.5

// using platform scala-native version 0.5.8  funktioniert nicht: default 0.5.5 existiert nicht, version Angabe: unrecognised argument
// oder scala-cli --native --scala-native-version 0.5.8 FileSize.scala  --scala 3.7.1
//scala-cli run FileSize.scala  --platform scala-native  --platform-version 0.5.8

//  using nativeMode release-full

import os.{Path, RelPath}


//https://scala-cli.virtuslab.org/docs/cookbooks/introduction/instant-startup-scala-scripts/
@main
def sizeHigherThan(dir: String, minSizeB: Int) =
   val wd = os.pwd / RelPath(dir)
   val files = os.walk.attrs(wd).collect{
      case (p, attrs) if attrs.size > minSizeB   => p -> attrs.size
   }
   files.sortBy((p,s) => s).foreach((p,s) => println(s"""$s\t\t$p"""))