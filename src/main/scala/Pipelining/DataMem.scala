package Pipelining

import chisel3._
import chisel3.util._

class DataMemory extends Module {
    val io = IO(new Bundle {
        val Addr = Input(UInt(32.W))
        val mem_read = Input(Bool())
        val mem_write = Input(Bool())
        val DataIn = Input(SInt(32.W))
        val DataOut = Output(SInt(32.W))
        val signal = Input(Bool()) 
    })

    val mem = Mem(1024, SInt(32.W))
    io.DataOut := 0.S

    val lrsc = Module(new LrSc)
    lrsc.io.inputAddr := io.Addr

    when (!io.signal) {
        when(io.mem_write) {
            mem.write(io.Addr, io.DataIn)
        }
        when(io.mem_read) {
            io.DataOut := mem.read(io.Addr)
        }
    }.otherwise {
        when(lrsc.io.isValid) {
            io.DataOut := mem.read(io.Addr) 
        }
    }
}