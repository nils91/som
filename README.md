# som

Som is a programming language/(simulated) computer architecture with bit-level, not byte-level, addressing, meaning all bits can be individually addressed. Each bit is binary, it can have 2 values, denoted as 0 in 1 in this document. Each command is made up of the opcode and the address. The only operation possible is the NAND (not and) between two bits.

| A | B | NAND(A, B) |
|:-:|:-:|----:|
| 0 | 0 |  1  |
| 0 | 1 |  1  |
| 1 | 0 |  1  |
| 1 | 1 |  0  |

One of the bits is fixed (its always at position 0 and called the accumulator), the other one is given by the address and the opcode specifies which one the result is written to. The opcode of each command is 1 bit long, meaning there are 2 different opcodes available:

 opcode | letter code |Description |
 --- | --- |--- |
0|`NAR`|Perform a logical NAND operation with the accumulator and the given memory addresses´ value and write the result to the accumulator.|
1|`NAW`|Perform a logical NAND operation with the accumulator and the given memory addresses´ value and write the result to the given memory address.|

## som bitcode

The opcode is followed by n bits denoting the memory address to make one command. Once the execution of one command is complete, execution will resume at the next command.
The accumulator lays in the regular address space, and it can be written to/read from like every other address. The accumulator address is always 0.
Bits 1-7 contain n as an unsigned int. The next bit is the `ADR_EVAL` bit, followed by n address bits. If the `ADR_EVAL` bit is set, the address bits will be evaluated and execution will continue at that address.
The next 4 bits are the write hook bits. The first one is the global write hook trigger bit (`WH_TRG`), 2nd is the write hook direction bit `WH_DIR`, 3rd is the global write hook communication bit (`WH_COM`), 4th is the write hook selection bit (`WH_SEL`). 
Each command is n+1 bits long. The first bit of each command is the opcode bit (see above), the remaining n bits are a memory address. The program will terminate, when execution reaches the end of the file with exactly 0 bits left. So, to exit at any point, jump to n^2-(1+n). If the accumulator bit is 1 at the time the program exits normally, a return code of 0 will be returned, otherwise 1.
In order to perform a jump to a memory address, write the address to the address bits and set `ADR_EVAL` to 1. Don´t forget to clear `ADR_EVAL` after the jump.

### basic memory layout

bit | name | code |
--- | --- | --- |
0|accumulator|`ACC`|
1|address evaluation bit|`ADR_EVAL`|
2|writehook enable bit|`WH_EN`|
3-7|N (address size)|`N[[0-4]]`|
8|writehook communication bit|`WH_COM`|
9|writehook direction|`WH_DIR`|
10|writehook select|`WH_SEL`|
11+|next jump target address|`ADR[[0-(N-1)]]`

### write hooks

- General behaviour

Write hooks are how SOM interacts with external ressources (`stdout` etc.). They are small programs provided by the runtime. Only 1 is loaded at any given time, but is it possible to change which write hook is loaded.
If `WH_EN` is set, all write hook bits will be evaluated. If `WH_DIR` is 1, the write hook will be triggered in write mode and the `WH_COM` bit  is sent to the write hook. If `WH_DIR` is 0, the write hook will be triggered in read mode. If the write hook has data available, the next available bit is written to `WH_COM` and the `WH_DIR` remains 0. If there´s no new data available, the `WH_DIR` will be 1.
If `WH_SEL` is one, when the write hook is triggered, the currently loaded write hook will be triggered. If `WH_SEL` is 0 that means write hook selection mode. If `WH_DIR` is 1 while `WH_SEL` is 0 it will switch the currently selected write hook. If `WH_COM` is 0, the previous write hook will be selected, while the next one will be selected if its 1. If `WH_DIR` is 0 while `WH_SEL` is 0, `WH_COM` will be set according to the success state of the last write hook switch. However that does not nescesarily mean, that a valid write hook is selected, if `WH_COM` signals a successful switch, it just means, that the internal write hook select number haS a value that is greater than 0 and smaller than the maximum write hook number. If there is no valid write hook, if a read is attempted (see above), `WH_DIR` will be set to 1 to indicate theres no data available. At program start the write hook selected is always 0.

- Notes on implementation
  
### Example (outdated)

Note: For readability each command is written as a new line and commented. Comments are not supported within the bitcode.

```
0			//accumulator
00001		//n=5
01011		//startaddress=11
10 00000	//NAND ACC. This has the effect of inverting the accumulator bit.
11 11001	//CJMP 25.
```

Each bit not explicitly written in the file is 0. The file is 2^n bits in size.

## SOM language (possibly outdated)

SOM language comes in different flavors, .hra, .hrb and .hrc. .hra is just as slightly more human readable version of the bitcode, while .hrb and .hrc are human readable assembly languages. .hrc is a more complex version of .hrb. .hrb programs can also be interpreted as .hrc.

## Formats

SOM bitcode can be used in several formats.

### Ascii binary (.ab)

The ascii binary format is a format where the bits are the characters '0' and '1'. Every other character is ignored. Above example is a valid program in ascii binary (its outdated).  Comments are not really supported, but since all characters other than 0 or 1 are ignored, it is still possible to comment as long the 0 or 1 characters are not used.

### Binary (.bin)

Same thing as ascii binary, but instead of characters the bits are actual bits. The file does not need to be 2^n bits in size, it only needs to contain the bits necessary. If the bits that make up the program don´t come out to be a round number of bytes, the file should be padded with '0'´s to the next byte.

### SOM bitlanguage (.hra)

The bitlanguage replaces the opcode bits with human-readable letter codes. The file only contains the commands, the compiler handles the accumulator bits. N is specified at the top of the file as `N 5`. hra also allows the `ACC` and `EXIT` symbols in place of addresses. The start address can be set with `START [N]`, but doesn´t have to, in which case it is calculated automatically (and is subsequently available as a symbol). Additional symbols are the ones given in above table `basic memory layout`. It is also possible to define own symbols: `SYMBOL [VALUE]`. Symbols can also be accessed as arrays: `SYMBOL[0]` would be the same as `SYMBOL` and point to `[VALUE]`, while `SYMBOL[1]` would point to `[VALUE]+1`. Comments are supported, but only as line comments starting with `'#'`. The following is a valid .hra program: (TODO: outdated, needs update)

```
;n=5
;start=11
NAND ACC
CJMP EXIT
```

That program is equivalent to above bitcode example.

### SOM simplified (.hrb)

### SOM language (.hrc)
