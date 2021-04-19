# som

Som is a programming language/computer architecture with bit-level, not byte-level, addressing, meaning all bits can be individually addressed. Each bit is binary, it can have 2 values, 0 in 1 in this document. There is only one memory space for both program AND data. The opcode of each command is just 2 bits long, meaning there are 4 different opcodes available:
opcode|4 letter code| Description
00|READ|Reads a value from memory into accumulator.
01|WRTE|Write a value from accumulator to memory.
10|NAND|Perform a logical NAND operation with the accumulator and the given memory value and write the result to the accumulator.
11|CJMP|Conditional jump. If the accumulator has value 1, perform jump.
The opcode is followed by n bits denoting the memory address (or jump target address). The accumulator lays in the regular address space, and it can be written to/read from like every other address. The accumulator address is 0.
Program execution starts at 0. Each command is n+2 bits long. After a command is executed, execution will advance by n+2 and continue with the next command. If a jump is executed, execution will continue at the given address.