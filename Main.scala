import os._

@main def run() =
  println("Listing current directory:")
  val wd = os.pwd  // / "my-test-folder"
// Find and concatenate all .txt files directly in the working directory
  os.write(
    wd / "all.txt",
    os.list(wd).filter(_.ext == "scala").map(os.read)
  )

  println(os.read(wd / "all.txt"))

     """I am cowI am cow
       |Hear me moo
       |I weigh twice as much as you
       |And I look good on the barbecue""".stripMargin

  // Find and concatenate all .txt files directly in the working directory using `cat`
//  os.proc("cat", os.list(wd).filter(_.ext == "txt")).call(stdout = wd / "all.txt")

//  println(os.read(wd / "all.txt")  )
