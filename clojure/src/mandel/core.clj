(ns mandel.core
  (:require [clojure.math.numeric-tower :as math])
  (:gen-class))

(defn make-point [x y]
  {:x x :y y})

(defn make-rect [x y w h]
  {:x x :y y :w w :h h})

(defn complex-make [real imag]
  {:real real :imag imag})

(defn complex-add [c1 c2]
  {:real (+ (:real c1) (:real c2))
   :imag (+ (:imag c1) (:imag c2))})

(defn complex-multiply [c1 c2]
  (let [x (:real c1)
        y (:imag c1)
        u (:real c2)
        v (:imag c2)]
    {:real (- (* x u) (* y v))
     :imag (+ (* x v) (* y u))}))

(defn complex-magnitude [c]
  (math/sqrt (+
              (* (:real c) (:real c)) 
              (* (:imag c) (:imag c)))))

(defn mandelbrot-depth [z c depth]
  (if (> depth 100)
    depth
    (if
      (< (complex-magnitude z) 2)
      (let [z-squared (complex-multiply z z)]
        (mandelbrot-depth (complex-add z-squared c) c (+ depth 1)))
      depth)))

(defn mandelbrot [c]
  (mandelbrot-depth c c 0))

(defn generate-coords [w h]
  (vector (for [y (range (+ h))]
    (vector (for [x (range (+ w))]
      (make-point x y))))))

(defn linear-interpolate [min-from max-from min-to max-to value-from]
  (let [diff-from (- max-from min-from)
        ratio (/ (- value-from min-from) diff-from)]
    (+ min-from (* ratio diff-from))))
