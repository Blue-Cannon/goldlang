// carve/Carve.scala — Carve VM
// Carve is the Gold Virtual Machine
// Gold is soft, so you carve it. Diamond is hard, so you cut it.

package carve

import gold.Program
import gold.PrintLine
import gold.End

object Carve {
  def run(program: Program): Unit = {
    println("Starting...")
    for (stmt <- program.statements) {
      stmt match {
        case PrintLine(msg) => println(msg)
        case End() => 
          println("Halted.")
          return
        case _ =>
      }
    }
  }

  def runBytecode(filePath: String): Unit = {
    // v0.5: will read .gldc file
    println(s"Loading bytecode: $filePath")
  }
}
