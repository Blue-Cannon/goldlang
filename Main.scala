// ./Main.scala — Gold Engine Core (Engine of Gold, not terminal)
// Gold Programming Language | .gld files | Carve VM | Written in Scala

package gold

import java.io.File
import scala.io.Source

object Main {

  // Engine entrance: Gold code comes here
  def runGoldFile(filePath: String): Unit = {
    val file = new File(filePath)
    
    if (!file.exists()) {
      throw new RuntimeException(s"[Gold Engine] File not found: $filePath")
    }

    val code = Source.fromFile(file).mkString

    // 1. Parser it (inside engine)
    val ast = GoldParser.parse(code)

    // 2. Interpreter it (inside engine)
    GoldInterpreter.interpret(ast)

    // 3. Compile it (inside engine → Carve)
    GoldCompiler.compile(ast, filePath)
  }

  // For engine internal testing
  def main(args: Array[String]): Unit = {
    if (args.nonEmpty) {
      runGoldFile(args.last)
    }
  }
}

// --- Inside Engine ---

case class Program(statements: List[Statement])
sealed trait Statement
case class UsePackage(name: String) extends Statement
case class ComputerStart() extends Statement
case class ComputerSay(message: String) extends Statement
case class ComputerEnd() extends Statement
case class ProgramEnd() extends Statement

object GoldParser {
  def parse(code: String): Program = {
    var stmts: List[Statement] = List()
    for (line <- code.split("\n").map(_.trim).filter(_.nonEmpty)) {
      if (line.startsWith("use prepackage/")) stmts = stmts :+ UsePackage(line.replace("use prepackage/", ""))
      else if (line == "if.computer == start:") stmts = stmts :+ ComputerStart()
      else if (line.startsWith("computer.say")) {
        val s = line.indexOf("\""); val e = line.lastIndexOf("\"")
        if (s != -1 && e > s) stmts = stmts :+ ComputerSay(line.substring(s + 1, e))
      }
      else if (line == "end.computer") stmts = stmts :+ ComputerEnd()
      else if (line == "end") stmts = stmts :+ ProgramEnd()
    }
    Program(stmts)
  }
}

object GoldInterpreter {
  def interpret(program: Program): Unit = {
    var started = false
    for (stmt <- program.statements) {
      stmt match {
        case ComputerStart() => started = true
        case ComputerSay(msg) => if (started) println(msg)
        case ComputerEnd() => started = false
        case _ =>
      }
    }
  }
}

object GoldCompiler {
  def compile(program: Program, filePath: String): Unit = {
    }
}
