package gold

object GoldParser {
  def parse(code: String): Program = {
    var stmts: List[Statement] = List()
    for (line <- code.split("\n").map(_.trim).filter(_.nonEmpty)) {
      if (line.isEmpty) {
      }
        else if (line.startsWith("printline(")) {
        val s = line.indexOf("\"")
        val e = line.lastIndexOf("\"")
        if (s != -1 && e > s) stmts = stmts :+ PrintLine(line.substring(s+1, e))
      } else if (line.contains("=") && !line.startsWith("if")) {
          val parts = line.split("=", 2)
          val varName = parts(0).trim
          val expr = parts(1).trim
          if (varName.matches("""[a-zA-Z_][a-zA-Z0-9_]*""")) {
            stmts = stmts :+ VarDecl(varName, expr)
          }
      } else if (line.startsWith("ask")) {
          val s = line.indexOf("(")
          val e = line.indexOf(")")
          if (s != -1 && e > s)
          stmts = stmts :+ Ask(line.substring(s+1, e).trim) 
      } else if (line.startsWith("if") && line.contains("enter()")) {
          val varName = line.replace("if", "").replace("enter()", "").(":", "").trim
          stmts = stmts :+ IfEnter(varName)
      } else if (line == "end.if") {
        stmts = stmts :+ EndIf()
      } else if (line == "end") {
        stmts = stmts :+ End()
      }
    }
    Program(stmts)
  }
}
