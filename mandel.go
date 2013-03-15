/*
Mandelbrot set in Go
*/

package main

import (
	"fmt"
)

type Rectangle struct {
	x, y float32
	width, height float32
}

func alpha(x float32, min float32, max float32) float32 {
	return (x - min) / (max - min)
}

func lerp(alpha float32, min float32, max float32) float32 {
	return alpha * (max - min) + min
}

func main() {
	var r Rectangle = Rectangle{x:-2.5, y:-2.5, width:5, height:5}
	var display_grid Rectangle = Rectangle{x:0, y:0, width:120, height:50}
	for y := display_grid.y; y < display_grid.height; y++ {
		var b = lerp(alpha(y, display_grid.y, display_grid.y + display_grid.height),
			r.x, r.x + r.width)
		for x := display_grid.x; x < display_grid.width; x++ {
			var a = lerp(alpha(x, display_grid.x, display_grid.x + display_grid.width),
				r.y, r.y + r.height)
			var z = complex(a, b)
			var c = z
			var i = 0
			for (i < 9) && (imag(z) * imag(z) + real(z) * real(z) < 16) {
				z = z * z + c
				i += 1
			}
			fmt.Printf("\x1B[%#vmM", i + 30)
		}
		fmt.Printf("\n")
	}
}
