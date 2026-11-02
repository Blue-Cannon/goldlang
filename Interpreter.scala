package gold

object GoldInterpreter {
  def interpret(program: Program): Unit = {
    for (stmt <- program.statements) {
      stmt match {
        case PrintLine(msg) => println(msg)
        case VarDecl(name, expr) => vars += (name -> evalExpr(expr, vars))
        case End() => return
        case _ =>
      }
    }
  }
}
