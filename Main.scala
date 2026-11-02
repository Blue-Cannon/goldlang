// ./Main.scala — Gold Engine Core | Carve VM
// Syntax: printline("Hello World!") / end
package gold

import java.io.File
import scala.io.Source

object Main {
  def runGoldFile(filePath: String): Unit = {
    val code = Source.fromFile(new File(filePath)).mkString
    val ast = GoldParser.parse(code)
    GoldInterpreter.interpret(ast)
    GoldCompiler.compile(ast, filePath)
  }

  def main(args: Array[String]): Unit = {
    if (args.nonEmpty) runGoldFile(args.last)
  }
}

case class Program(statements: List[Statement])
sealed trait Statement
case class PrintLine(msg: String) extends Statement
case class End() extends Statement
case class VarDecl(name: String, expr: String) extends Statement
case class Ask(name: String) extends Statement
case class IfEnter(name: String) extends Statement
case class Print(expr: String) extends Statement
case class EndIf() extends Statement

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

object GoldCompiler {
  def compile(program: Program, filePath: String): Unit = {
    // Carve VM: .gld -> .gldc in v0.5
  }
}
