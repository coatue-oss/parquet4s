package com.github.mjakubowski84.parquet4s

import java.io.{ByteArrayOutputStream, FileOutputStream}

import org.scalatest.{BeforeAndAfter, FreeSpec, Matchers}

case class Test(
                 t: String,
                 f: Option[Int]
               )
class ParquetStreamWriter extends FreeSpec
with Matchers
with BeforeAndAfter with SparkHelper {
  import sparkSession.implicits._


  "write parquet to outputstream" - {
    val data = Seq(Test("a", None), Test("b", Some(1)))
    val outputStream = new ByteArrayOutputStream()
    val outfile = new StreamOutputFile(outputStream)
    import ParquetWriter._
    ParquetWriter.writeS(outfile,data)
    outputStream.close()
    println(outputStream)
    outputStream.size() > 1 should equal(true)
  }

  "spark read data written to file from outputstream" - {
    val data = Seq(Test("a", None), Test("b", Some(1)))
    val outputStream = new ByteArrayOutputStream()
    val outfile = new StreamOutputFile(outputStream)
    ParquetWriter.writeS(outfile,data)
    outputStream.close()
    val f = new FileOutputStream(tempPathString)
    f.write(outputStream.toByteArray)
    f.close()
    sparkSession.read.parquet(tempPathString).as[Test].collect().toSeq should equal(data)
  }

}
