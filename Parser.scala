package gold

object GoldParser {
  def parse(code: String): Program = {
    var stmts: List[Statement] = List()
    for (line <- code.split("\n").map(_.trim).filter(_.nonEmpty)) {
      if (line.startsWith("printline(")) {
        val s = line.indexOf("\"")
        val e = line.lastIndexOf("\"")
        if (s != -1 && e > s) stmts = stmts :+ PrintLine(line.substring(s+1, e))
      } else if (line == "end") {
        stmts = stmts :+ End()
      }
    }
    Program(stmts)
  }
}
