Each file in either binary or ascii binary has header bits. Their meaning is constants and they exist in every program. The first 12 bits (0-11) are fixed in position, the n bits after that are the address bits, which are utilized to set the start address and jump. The program counter itsself is not part of the memspace, the address bits, if read, do not contain the current position in code.

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

N as given has an offset of 4.