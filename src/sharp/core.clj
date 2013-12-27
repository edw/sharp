(ns sharp.core)

(defn- translate-vertices
  [vertices]
  (loop [ms {:indices {} :names []}
         i 0
         vs (vec (set vertices))]
    (if (seq vs)
      (let [v (first vs)]
        (-> (update-in ms [:indices] assoc v i)
            (update-in [:names] conj v)
            (recur (inc i) (rest vs))))
       (assoc ms :vertex-count (count (:names ms))))))

(defn- adjacency-matrix
  [n-vertices & edges]
  (let [bits (java.util.BitSet. (* n-vertices n-vertices))]
    (doseq [[out in] edges]
      (.set bits (+ (* out n-vertices) in)))
    bits))

(defn graph
  [& edges]
  (let [vertex-translations (translate-vertices (apply concat edges))
        indices (get vertex-translations :indices)
        translated-edges (for [[from to] edges] [(get indices from)
                                                 (get indices to)])]
    (assoc vertex-translations :rows (apply adjacency-matrix (count indices)
                                            translated-edges))))

(defmacro edge-visitor
  [[i vertex-count offset] index-form]
  `(fn [g# vertex#]
     (let [rows# (:rows g#)
           ~vertex-count (:vertex-count g#)
           names# (:names g#)
           ~offset (get-in g# [:indices vertex#])]
       (->
        (reduce (fn [set# ~i]
                  (if (.get rows# ~index-form)
                    (conj! set# (get names# ~i))
                    set#))
                (transient #{})
                (range 0 ~vertex-count))
        persistent!))))

(def in-edges
  (edge-visitor [i vertex-count offset]
                (+ (* i vertex-count) offset)))

(def out-edges
  (edge-visitor [i vertex-count offset]
                (+ (* offset vertex-count) i)))
