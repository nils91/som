# som

Som is a programming language/(simulated) computer architecture with bit-level, not byte-level, addressing, meaning all bits can be individually addressed. Each bit is binary, it can have 2 values, denoted as 0 in 1 in this document. There is only one memory space for both program AND data. The opcode of each command is just 2 bits long, meaning there are 4 different opcodes available:
opcode|letter code| Description
00|READ|Reads a value from memory into accumulator.
01|WRITE|Write a value from accumulator to memory.
10|NAND|Perform a logical NAND operation with the accumulator and the given memory value and write the result to the accumulator.
11|CJMP|Conditional jump. If the accumulator has value 1, perform jump.
The opcode is followed by n bits denoting the memory address (or jump target address) to make one command. The accumulator lays in the regular address space, and it can be written to/read from like every other address. The accumulator address is 0.
Bits 1-5 contain n as an unsigned int, but offset by 4, 00000 means 4, 00001 means 5 and so on. 11111 means 35.
Program execution starts at 6. Each command is n+2 bits long. After a command is executed, execution will advance by n+2 and continue with the next command. If a jump is executed, execution will continue at the given address. The program will terminate, when execution reaches the end of the file with exactly 0 bits left. So, to exit at any point, jump to n^2-(2+n) and make sure that the last 2+n bits are set to 0.