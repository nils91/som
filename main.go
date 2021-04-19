package main

import (
	"fmt"
	"os"
)

func main() {
	var args []string
	args = os.Args[1:]

	fmt.Println(args)
}
