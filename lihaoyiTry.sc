
// Make sure working directory exists and is empty
val wd = os.pwd/"out/splash"
os.remove.all(wd)
os.makeDir.all(wd)

// Read/write files
os.write(wd/"file.txt", "hello")
os.read(wd/"file.txt")

// Perform filesystem operations
os.copy(wd/"file.txt", wd/"copied.txt")
os.list(wd) //==> Seq(wd/"copied.txt", wd/"file.txt")

// Invoke subprocesses
val invoked = os.proc("cat", wd/"file.txt", wd/"copied.txt").call(cwd = wd)
invoked.out.trim() //==> "hellohello"

// Chain multiple subprocess' stdin/stdout together
val curl = os.proc("curl", "-L" , "https://git.io/fpvpS").spawn(stderr = os.Inherit)
val gzip = os.proc("gzip", "-n").spawn(stdin = curl.stdout)
val sha = os.proc("shasum", "-a", "256").spawn(stdin = gzip.stdout)
sha.stdout.trim() //==> "acc142175fa520a1cb2be5b97cbbe9bea092e8bba3fe2e95afa645615908229e  -"