(ns mandel.core
  (:require [clojure.math.numeric-tower :as math])
  (:gen-class))

(def MAX-DEPTH 100)

; From http://paulbourke.net/dataformats/asciiart/
(def ASCII-SHADE-SCALE " .'`^\",:;Il!i><~+_-?][}{1)(\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$")

(defn get-shade-for-depth [depth]
  (nth ASCII-SHADE-SCALE (mod depth (count ASCII-SHADE-SCALE))))

(defn make-point [x y]
  {:x x :y y})

(defn make-rect [x y w h]
  {:x x :y y :w w :h h})

(defn complex-make [real imag]
  {:real real :imag imag})

(defn complex-add [c1 c2]
  {:real (+ (:real c1) (:real c2))
   :imag (+ (:imag c1) (:imag c2))})

(defn complex-to-point [c]
  {:x (c :real) :y (c :imag)})

(defn point-to-complex [p]
  {:real (p :x) :imag (p :y)})

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

(defn mandelbrot [c]
  (loop [z c depth 0]
    (if (or (>= depth MAX-DEPTH)
            (>= (complex-magnitude z) 2))
      depth
      ; z <- z^2 + c
      (recur (complex-add (complex-multiply z z) c) (+ depth 1)))))

(defn generate-coords [w h]
  (for [y (range (+ h))]
    (for [x (range (+ w))]
      (make-point x y))))

(defn map-coords 
  "Apply f to each item in the 2-D coords structure"
  [f coords]
  (map #(map f %) coords))

(defn linear-interpolate
  [min-from max-from min-to max-to value-from]
  (let [diff-from (- max-from min-from)
        diff-to (- max-to min-to)
        ratio (/ (- value-from min-from) diff-from)]
    (+ min-to (* ratio diff-to))))

(defn translate-point
  "Interpolates a point in rect1 to a similar point within rect2"
  [rect1 rect2 point]
  {:x (linear-interpolate
        (rect1 :x) (+ (rect1 :x) (rect1 :w))
        (rect2 :x) (+ (rect2 :x) (rect2 :w))
        (point :x))
   :y (linear-interpolate
        (rect1 :y) (+ (rect1 :y) (rect1 :w))
        (rect2 :y) (+ (rect2 :y) (rect2 :w))
        (point :y))})
