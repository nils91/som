package main

import (
	"fmt"
	"os"
	"strconv"
)

func main() {
	args := os.Args[1:]

	n, err := strconv.Atoi(args[0])
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	memsize := upow(2, uint(n))
	filename := args[1]

	fmt.Println("Address size: " + fmt.Sprint(n) + " bits")
	fmt.Println("Memory space: " + fmt.Sprint(memsize) + " bits")
	fmt.Println("Input file: " + filename)
}

func upow(n uint, i uint) uint {
	if i == 0 {
		return 1
	}
	return upow(n, i-1) * n
}
