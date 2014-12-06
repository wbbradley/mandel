(ns mandel.core
  (:require [clojure.math.numeric-tower :as math])
  (:gen-class))

(defn make-complex [real imag]
  {:real real :imag imag})

(defn multiply-complex [c1 c2]
  (let [x (:real c1)
        y (:imag c1)
        u (:real c2)
        v (:imag c2)]
    {:real (- (* x u) (* y v))
     :imag (+ (* x v) (* y u))}))

