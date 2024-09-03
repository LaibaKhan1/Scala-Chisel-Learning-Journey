package Pipelining
import chisel3._
import chisel3.util
import org.scalatest.FreeSpec
import chiseltest._


class LRSCTest extends FreeSpec with ChiselScalatestTester {
   "lrscTest" in {
        test(new LrSc()) { c =>
        c.io.inputAddr.poke(10.U) 
        c.io.inputData.poke(5.U)
        c.clock.step(1)
        c.io.inputAddr.poke(10.U) 
        c.clock.step(1)
    }
  }
}
