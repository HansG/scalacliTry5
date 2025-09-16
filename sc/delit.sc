import os.Path

val wd = os.pwd / "idea"
val wd1 = Path("C:/se/prj/scalacliTry")
os.walk.attrs(wd1).mkString("\n")