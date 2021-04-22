# som

Som is a programming language/(simulated) computer architecture with bit-level, not byte-level, addressing, meaning all bits can be individually addressed. Each bit is binary, it can have 2 values, denoted as 0 in 1 in this document. There is only one memory space for both program AND data. The opcode of each command is just 2 bits long, meaning there are 4 different opcodes available:
opcode|letter code| Description
00|READ|Reads a value from memory into accumulator.
01|WRITE|Write a value from accumulator to memory.
10|NAND|Perform a logical NAND operation with the accumulator and the given memory value and write the result to the accumulator.
11|CJMP|Conditional jump. If the accumulator has value 1, perform jump.

## som bitcode

The opcode is followed by n bits denoting the memory address (or jump target address) to make one command. The accumulator lays in the regular address space, and it can be written to/read from like every other address. The accumulator address is 0.
Bits 1-5 contain n as an unsigned int, but offset by 4, 00000 means 4, 00001 means 5 and so on. 11111 means 35.
Program execution starts at 6. Each command is n+2 bits long. After a command is executed, execution will advance by n+2 and continue with the next command. If a jump is executed, execution will continue at the given address. The program will terminate, when execution reaches the end of the file with exactly 0 bits left. So, to exit at any point, jump to n^2-(2+n) and make sure that the last 2+n bits are set to 0.

### Example

Note: For readability each command is written as a new line and commented. Comments are not supported within the bitcode.

0			//accumulator
00001		//n=5
10 00000	//NAND ACC. This has the effect of inverting the accumulator bit.
11 11001	//CJMP 25.

Each bit not explicitly written in the file is 0. The file is 2^n bits in size.

## SOM language

SOM language comes in different flavors, .hra, .hrb and .hrc. .hra is just as slightly more human readable version of the bitcode, while .hrb and .hrc are human readable assembly languages. .hrc is a more complex version of .hrb. .hrb programs can also be interpreted as .hrc.

## Formats

SOM bitcode can be used in several formats.

### Ascii binary (.ab)

The ascii binary format is a format is a format where the bits are the characters '0' and '1'. Every other character is ignored. Above example, minus the comments, is a valid program in ascii binary.

### Binary (.bin)

Same thing as ascii binary, but instead of characters the bits are actual bits. The file does not need to be 2^n bits in size, it only needs to contain the bits nescessary. If the bits that make up the program don´t come out to be a round number of bytes, the file should be padded with '0'´s to the next byte.

### SOM bitlanguage (.hra)

The bitlanguage replaces the opcode bits with human-readable letter codes. The file only contains the commands, the compiler handles the accumulator bits. N is specified as a directive at the top of the file, like `;n=5`. hra also allows the ACC and EXIT symbols in place of addresses. Comments are also supported, but only as line comments starting with `'#'`. The following is a valid .hra program:

```
;n=5
NAND ACC
CJMP EXIT
```

That program is equivalent to above bitcode example.h

### SOM simplified (.hrb))

### SOM language (.hrc))
