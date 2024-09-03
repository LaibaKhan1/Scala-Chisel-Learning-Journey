package Pipelining



import chisel3._
import chisel3.util._   

class LrSc extends Module {
  val io = IO(new Bundle {
    val inputAddr = Input(UInt(32.W))   
    val isValid = Output(Bool())         
  })

  val addressVector = RegInit(VecInit(Seq.fill(1024)(0.U(32.W)))) 
  val addressCount = RegInit(0.U(3.W)) 
  val dataVector = RegInit(VecInit(Seq.fill(1024)(0.U(32.W)))) 
  val emptyIndex = RegInit(10.U(32.W))

  io.isValid := false.B

  when(addressCount === 0.U) {
    
    addressVector(0) := io.inputAddr
    addressCount := 1.U
    io.isValid := true.B
  }.otherwise {
    val found = Wire(Bool())
    found := false.B
    for (i <- 0 until 1024) { 
      when(io.inputAddr === addressVector(i)) {
        found := true.B
      }
    }

    when(found) {
      io.isValid := false.B
    }.otherwise {
      when(addressCount < 1024.U) { 
        addressVector(addressCount) := io.inputAddr
        addressCount := addressCount + 1.U
        io.isValid := true.B
      }
    }
  }
}
// import chisel3._
// import chisel3.util._

// class LrSc extends Module {
//   val io = IO(new Bundle {
//     val inputAddr = Input(UInt(32.W))   // Assume 32-bit addresses
//     val inputData = Input(UInt(32.W))   // Assume 32-bit data to write
//     val writeEnable = Input(Bool())     // Control signal to enable writing
//     val isValid = Output(Bool())
//     val outputData = Output(UInt(32.W))
//   })

//   val addressVector = RegInit(VecInit(Seq.fill(10)(0.U(32.W)))) // Storage for 10 addresses
//   val dataVector = RegInit(VecInit(Seq.fill(10)(0.U(32.W))))    // Storage for data corresponding to addresses
//   val vectorSize = 10
//   val emptyIndex = RegInit(10.U(32.W))

//   io.isValid := false.B
//   io.outputData := 0.U

//   when(io.writeEnable) {
//     var found = false.B
//     var emptyIndex = 10.U // Initialize to an invalid index that is out of bounds

//     // Initialize isValid at the start of each write cycle
//     io.isValid := false.B 

//     // Check for address in vector or find an empty spot
//     for (i <- 0 until vectorSize) {
//       when(addressVector(i) === io.inputAddr) {
//         found := true.B
//         io.isValid := false.B  // This is redundant and could be causing issues if not handled properly elsewhere
//       }.elsewhen(addressVector(i) === 0.U && emptyIndex === 10.U) {
//         emptyIndex := i.U
//       }
//     }

//     // If not found and there's an empty spot, add the address
//     when(!found && emptyIndex =/= 10.U) {
//       addressVector(emptyIndex) := io.inputAddr
//       dataVector(emptyIndex) := io.inputData
//       io.isValid := true.B
//     }
// }

// // Another section to explicitly handle writing based on validity
// when(io.isValid && io.writeEnable) {
//     dataVector(emptyIndex) := io.inputData
//     io.outputData := io.inputData
//     // Reset the valid flag after the operation
//     io.isValid := false.B
// }

// }

