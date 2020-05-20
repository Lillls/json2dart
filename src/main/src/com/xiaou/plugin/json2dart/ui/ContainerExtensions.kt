package com.xiaou.plugin.json2dart.ui
import java.awt.Component
import java.awt.Container
import javax.swing.Box
import javax.swing.BoxLayout


fun Container.addComponentIntoVerticalBoxAlignmentLeft(component: Component) {
    if (layout is BoxLayout) {
        val hBox = Box.createHorizontalBox()
        hBox.add(component)
        hBox.add(Box.createHorizontalGlue())
        add(hBox)
    }

}

