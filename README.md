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

Each command is made from the opcode followed by the address. The opcode is one bit and the address is n bits in size. Once the execution of one command is complete, execution will resume at the next command, except if the `ADR_EVAL` bit is set (at position 1), in which case execution will resume at the address set by the n bits at position 11 and following. For the positions of this and other special bits refer to the table `basic memory layout` below.
The accumulator bit at position 0 is a special bit used by the `NAW` and `NAR` commands, which read respectively write to it.
Each command is n+1 bits long. The first bit of each command is the opcode bit (see above), the remaining n bits are a memory address. The program will terminate, when execution reaches the end of the file with exactly 0 bits left. If the accumulator bit is 1 at the time the program exits normally, a return code of 0 will be returned, otherwise 1.
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
11+|next jump target address (n bits)|`ADR[[0-(N-1)]]`

### write hooks

- General behaviour

Write hooks are how SOM interacts with external ressources (`stdout` etc.). They are small programs provided by the runtime. Only 1 is loaded at any given time, but is it possible to change which write hook is loaded.
If `WH_EN` is set, all write hook bits will be evaluated. If `WH_DIR` is 1, the write hook will be triggered in write mode and the `WH_COM` bit  is sent to the write hook. If `WH_DIR` is 0, the write hook will be triggered in read mode. If the write hook has data available, the next available bit is written to `WH_COM` and the `WH_DIR` remains 0. If there´s no new data available, the `WH_DIR` will be 1.
If `WH_SEL` is one, when the write hook is triggered, the currently loaded write hook will be triggered. If `WH_SEL` is 0 that means write hook selection mode. If `WH_DIR` is 1 while `WH_SEL` is 0 it will switch the currently selected write hook. If `WH_COM` is 0, the previous write hook will be selected, while the next one will be selected if its 1. If `WH_DIR` is 0 while `WH_SEL` is 0, `WH_COM` will be set according to the success state of the last write hook switch. However that does not nescesarily mean, that a valid write hook is selected, if `WH_COM` signals a successful switch, it just means, that the internal write hook select number haS a value that is greater than 0 and smaller than the maximum write hook number. If there is no valid write hook, if a read is attempted (see above), `WH_DIR` will be set to 1 to indicate theres no data available. At program start the write hook selected is always 0.

- Notes on implementation
  
### Example

Note: For readability each command is written as a new line and commented. Comments are not really supported within the bitcode.

```
1 //ACC is set
1 //ADR_EVAL is set
0 //WH_EN is set
00001 //N is 5 (offset of 4)
000 //WH bits (WH_COM, WH_SEL and WH_DIR) will not be used
11010 //(Start-)Address is 26 and will be continued at because ADR_EVAL is set
0000000000 //unused bits
1 00001 //Address 26 starts here. Command is NAW one (Address one is ADR_EVAL, this needs to be cleared to prevent the program from being stuck on address 26)
```

Each bit not explicitly written in the file is 0. The file is 2^n bits in size.

## SOM language

SOM language comes in different flavors to make the bitcode more accessible.

## Formats

SOM bitcode can be used in several formats.

### Ascii binary (.ab)

The ascii binary format is a format where the bits are the characters '0' and '1'. Every other character is ignored. Above example is a valid program in ascii binary.  Comments are not really supported, but since all characters other than 0 or 1 are ignored, it is still possible to comment as long the 0 or 1 characters are not used.

### Binary (.bin)

Same thing as ascii binary, but instead of characters the bits are actual bits. The file does not need to be 2^n bits in size, it only needs to contain the bits necessary. If the bits that make up the program don´t come out to be a round number of bytes, the file should be padded with '0'´s to the next byte.

### Base64 Binary (.b64)

Base64 encoded version of the binary.

### Compressed binary (.cbin)

Binary in compressed form. The compression used is ZIP, the .cbin file is a zip file with a single entry called BIN which contains the binary data.

### Image (.png)

Encodes the binary as the RGB pixel values in an image.

### SOM bitlanguage (.hra)

The bitlanguage replaces the opcode bits with human-readable letter codes. There are 2 versions of .hra: .hras and .hrac. In hras memory addresses still need to be written, while in hrac the compiller allocates the addresses used. The file only contains the commands, the compiler handles the accumulator bits. N is specified at the top of the file as `;n=5`. hra also allows  symbols in place of addresses. Builtin are the ones outlined in the table basic memory layout, more can be definded by the user. The start address can be set with `;start=26`. Comments are supported, but only as line comments starting with `'#'`. The following is a valid .hras program:
```
;n=5
;start=26
NAW ADR_EVAL
```

That program is equivalent to above bitcode example. The same in hrac would only need to contain the commands, n and the start address are calculated automatically.

### Language features hras

- Directives:
 Directives start with a semicolon (`;`) and are hints for the compiler. hras supports the following directives:
	- `;n=<value>`
   Value for N. (Only once per file)
	- `;start=<value>`
Gives the start address. (Only once per file)
	- `;continue=<value>`
   Continue writing at a given address. Does not change program execution.
- Symbols:
Symbols are placeholders for memory addresses. They can be used within commands. They can also be used for the `start` and `continue` directives.
	- Symbols can be defined by writing the symbol name followed by an address:
	```A 42```
	- Symbols  can be defined using other symbols:
	```B A```
	- Symbols can be used within commands:
	```NAR A```
	- When using symbols, a address offset can be used:
	`NAR A[42]`
	`B A[42]`
	- There are built-in symbols. For these see the table 'basic memory layout'.
- Commands
	- Available commands are `NAW` and `NAR`. Both can be used with symbols or memory addresses.
	- First command must be `NAW ADR_EVAL` to clear the ADR_EVAL bit.
	
The accumulator will be set at program start.
	
	
### Language features hrac

- Directives:
 Directives start with a semicolon (`;`) and are hints for the compiler. hras supports the following directives:
	- `;n=<value>`
   Minimum value for N. N will be calculated automatically. (Optional) 
	- `;heap=<value>`
Minimum number of additional unused bytes to be included.
- Symbols:
Symbols are placeholders for memory addresses. They can be used within commands. The memory address for each symbol will be calculated automatically.
	- Symbols can be defined by writing just the symbol name:
	```A```
	- Arrays of symbols can be defined:
	```A[10]```
	- Symbols  can be defined using other symbols:
	```B A```
	- Symbols can be used within commands:
	```NAR A```
	- When using symbols, a address offset can be used:
	`NAR A[4]`
	`B A[4]`
	- There are built-in symbols. For these see the table 'basic memory layout'.
- Commands
	- Available commands are `NAW` and `NAR`. Both can only be used with symbols, using memory addresses directly is not supported with hrac.
	- The first command `NAW ADR_EVAL` to clear the ADR_EVAL bit is added automatically.
	
The accumulator will be set at program start.
	
### SOM simplified (.hrb)

### SOM language (.hrc)

## How to build

Needs Maven and Java (>=11).

`mvn clean install`
or
`mvn clean install -DskipTests` to build without tests (might be needed when running maven through eclipse).

The finished exececutable jar file is in the ```target``` subfolder (som-java-\<VERSION\>-shaded.jar). Theres also another jar file without the ```-shaded```, but that one is not executable.
