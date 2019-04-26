package com.github.mjakubowski84.parquet4s

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.github.mjakubowski84.parquet4s.Case.CaseDef
import com.github.mjakubowski84.parquet4s.CompatibilityParty._
import org.scalatest.{BeforeAndAfter, FreeSpec, Matchers}

class ParquetWriterAndParquetReaderCompatibilityItSpec extends
  FreeSpec
    with Matchers
    with BeforeAndAfter
    with SparkHelper {

  before {
    clearTemp()
  }

  case class Test(
                 t: String,
                 f: Option[Int]
                 )

  private def runTestCase(testCase: CaseDef): Unit = {
    testCase.description in {
      ParquetWriter.write(tempPathString, testCase.data)(testCase.writer)
      val parquetIterable = ParquetReader.read(tempPathString)(testCase.reader)
      try {
        parquetIterable should contain theSameElementsAs testCase.data
      } finally {
        parquetIterable.close()
      }
    }
  }

  private def runStreamTestCase(testCase: CaseDef): Unit = {
    testCase.description in {
      val outputStream = new ByteArrayOutputStream()
      val outfile = new StreamOutputFile(outputStream)
      ParquetWriter.writeS(outfile, testCase.data)(testCase.writer)
      outputStream.close()
      outputStream.size() > 1 should equal(true)
    }
  }

  "Spark should be able to read file saved by ParquetWriter if the file contains" - {
    CompatibilityTestCases.cases(Writer, Reader).foreach(runTestCase)
  }

  "Stream Writer should be able to write all types" - {
    CompatibilityTestCases.cases(Writer, Reader).foreach(runStreamTestCase)
  }
}
