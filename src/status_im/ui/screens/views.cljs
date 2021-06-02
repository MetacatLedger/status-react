(ns status-im.ui.screens.views
  (:require [status-im.ui.components.react :as react]
            [reagent.core :as reagent]
            [status-im.reloader :as reloader]
            [status-im.ui.screens.screens :as screens]
            [status-im.ui.screens.routing.core :as routing]
            [status-im.ui.screens.popover.views :as popover]
            [status-im.ui.screens.bottom-sheets.views :as bottom-sheets]))

(defn get-screens []
  (reduce
   (fn [acc screen]
     (assoc acc (:name screen) screen))
   {}
   screens/screens))

;;TODO find why hot reload doesn't work
(def screens (get-screens))

(def components
  (reduce
   (fn [acc {:keys [name component]}]
     (assoc acc name component))
   {}
   (concat screens/components)))

(defn screen [key]
  (reagent.core/reactify-component
   (fn []
     ^{:key (str "root" key @reloader/cnt)}
     [react/safe-area-provider
      [react/safe-area-consumer
       (fn [insets]
         (reagent/as-element
          [react/view {;;TODO check how it works
                       :style (routing/wrapped-screen-style
                               {:insets (get-in screens [(keyword key) :insets])}
                               insets)}
           [(get-in (if js/goog.DEBUG (get-screens) screens) [(keyword key) :component])]]))]
      (when js/goog.DEBUG
        [reloader/reload-view])])))

(defn component [comp]
  (reagent/reactify-component
   (fn []
     [react/view {:width 500 :height 44}
      [comp]])))

(def popover-comp
  (reagent/reactify-component
   (fn []
     ^{:key (str "popover" @reloader/cnt)}
     [react/safe-area-provider
      [popover/popover]
      (when js/goog.DEBUG
        [reloader/reload-view])])))

(def sheet-comp
  (reagent/reactify-component
   (fn []
     ^{:key (str "seet" @reloader/cnt)}
     [react/safe-area-provider
      [bottom-sheets/bottom-sheet]
      (when js/goog.DEBUG
        [reloader/reload-view])])))