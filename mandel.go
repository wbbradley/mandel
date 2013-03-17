/*
Mandelbrot set in Go
*/

package main

import (
	"image"
	"image/png"
	"log"
	"os"
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

func SetPixel(img *image.NRGBA, x, y, r, g, b, a float32) {
	offset := img.PixOffset(int(x), int(y))
	img.Pix[offset] = uint8(r * 255);
	img.Pix[offset + 1] = uint8(g * 255);
	img.Pix[offset + 2] = uint8(b * 255);
	img.Pix[offset + 3] = uint8(a * 255);
}

func main() {
	var maxDepth int = 20
	var r Rectangle = Rectangle{x:-3.14159, y:-2.5, width:5, height:5}
	var display_grid Rectangle = Rectangle{x:0, y:0, width:240, height:240}
	img := image.NewNRGBA(image.Rect(0, 0,
							int(display_grid.width), int(display_grid.height)))

	for y := display_grid.y; y < display_grid.height; y++ {
		var b = lerp(alpha(y, display_grid.y, display_grid.y + display_grid.height),
			r.x, r.x + r.width)
		for x := display_grid.x; x < display_grid.width; x++ {
			var a = lerp(alpha(x, display_grid.x, display_grid.x + display_grid.width),
				r.y, r.y + r.height)
			var z = complex(b, a)
			var c = z
			var i = 0
			for (i < maxDepth) && (imag(z) * imag(z) + real(z) * real(z) < 16) {
				z = z * z + c
				i += 1
			}
			SetPixel(img, x, y,
				float32(i) / float32(maxDepth),
				float32(i) / float32(maxDepth),
				float32(i) / float32(maxDepth), 1);
		}
	}
	f, err := os.OpenFile("mandel.png", os.O_CREATE | os.O_WRONLY, 0666)
	if err != nil {
		log.Fatal(err)
	}
	if err = png.Encode(f, img); err != nil {
		log.Fatal(err)
	}
	err = f.Close()
	if err != nil {
		log.Fatal(err)
	}
}
