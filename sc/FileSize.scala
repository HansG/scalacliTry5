//> using scala "3.7.1"
//> using dep com.lihaoyi::os-lib::0.11.5

import os.Path

@main
def sizeHigherThan(dir: String, minSizeB: Int) =
   val wd = Path(dir)
   val files = os.walk.attrs(wd).collect{
      case (p, attrs) if attrs.size > minSizeB   => p -> attrs.size
   }
   files.foreach((p,s) => println(s"""$s\t\t$p"""))