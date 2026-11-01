// carve/Bytecode.scala — Carve Bytecode Format (.gldc)
package carve

sealed trait CarveOp
case class OP_PRINT(msg: String) extends CarveOp
case object OP_HALT extends CarveOp

case class CarveBytecode(ops: List[CarveOp])
