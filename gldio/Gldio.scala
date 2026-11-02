// goldio/Gldio.scala — Gold.io now imports to goldio_package/
package goldio

import java.io.File
import scala.io.Source

object Gldio {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) { printHelp(); return }
    val op = args(0)
    val pkg = if (args.length > 1) args(1) else ""
    op match {
      case "init" => init()
      case "install" => install(pkg)
      case "build" => build()
      case "publish" => publish()
      case "list" => list()
      case "uninstall" => uninstall(pkg)
      case _ => println(s"[Gold.io] Unknown: $op")
    }
  }

  def printHelp(): Unit = {
    println("Gold.io — gold.io.toml -> goldio_package/")
    println("  gldio init")
    println("  gldio install <pkg>  -> goldio_package/<pkg>/")
    println("  gldio build")
    println("  gldio list")
  }

  def init(): Unit = {
    val file = new File("gold.io.toml")
    if (file.exists()) { println("[Gold.io] gold.io.toml exists"); return }
    val content = """[package]
name = "my-gold-project"
version = "0.1.0"
main = "main.gld"

[dependencies]

[registry]
url = "https://github.com/Blue-Cannon/goldlang/tree/main/goldio_package"
local_path = "goldio_package/"
"""
    val w = new java.io.PrintWriter(file); w.write(content); w.close()
    println("[Gold.io] Created gold.io.toml")
  }

  def install(pkg: String): Unit = {
    if (pkg.isEmpty) { println("Usage: gldio install <package>"); return }
    println(s"[Gold.io] Fetching $pkg from registry https://gold.io...")
    
    // Local registry path: goldio_package/
    val registryDir = new File("goldio_package")
    if (!registryDir.exists()) registryDir.mkdir()

    val pkgDir = new File(s"goldio_package/$pkg")
    if (!pkgDir.exists()) pkgDir.mkdirs()

    // Simulate import: create package files
    val pkgFile = new File(s"goldio_package/$pkg/main.gld")
    val pw = new java.io.PrintWriter(pkgFile)
    pw.write(s"// $pkg from gold.io registry\n// Imported to goldio_package/$pkg/\n")
    pw.close()

    println(s"[Gold.io] Imported $pkg -> goldio_package/$pkg/")
    updateToml(pkg, "add")
  }

  def build(): Unit = {
    println("[Gold.io] Reading gold.io.toml...")
    val cfg = new File("gold.io.toml")
    if (!cfg.exists()) { println("gold.io.toml not found"); return }
    val text = Source.fromFile(cfg).mkString
    val name = """name\s*=\s*"([^"]+)"""".r.findFirstMatchIn(text).map(_.group(1)).getOrElse("gold-package")
    println(s"[Gold.io] Building $name...")
    val dist = new File("dist"); if (!dist.exists()) dist.mkdir()
    println(s"[Gold.io] Using packages from goldio_package/")
    println(s"[Gold.io] Built dist/$name.goldpkg")
  }

  def list(): Unit = {
    val dir = new File("goldio_package")
    if (!dir.exists() || dir.list().isEmpty) {
      println("[Gold.io] No packages in goldio_package/")
      return
    }
    println("[Gold.io] Packages in goldio_package/:")
    dir.listFiles().foreach(f => println(s"  - ${f.getName}"))
  }

  def uninstall(pkg: String): Unit = {
    val dir = new File(s"goldio_package/$pkg")
    if (dir.exists()) {
      deleteRecursively(dir)
      println(s"[Gold.io] Removed goldio_package/$pkg")
      updateToml(pkg, "remove")
    }
  }

  def publish(): Unit = println("[Gold.io] Publishing from gold.io.toml to https://gold.io")

  def updateToml(pkg: String, action: String): Unit = {
    // TODO: Update gold.io.toml [dependencies]
  }

  def deleteRecursively(f: File): Unit = {
    if (f.isDirectory) f.listFiles().foreach(deleteRecursively)
    f.delete()
  }
  }
