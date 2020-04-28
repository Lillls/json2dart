package com.xiaou.plugin.json2dart.aticon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.xiaou.plugin.json2dart.DartFileGenerator
import com.xiaou.plugin.json2dart.inputJson2Map
import com.xiaou.plugin.json2dart.map2DartClassDefinition
import com.xiaou.plugin.json2dart.ui.JsonInputDialog


class Json2DartAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val dataContext = event.dataContext
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return
        //获取右键所在的文件夹
        val directory = when (val navigationTable = LangDataKeys.NAVIGATABLE.getData(dataContext)) {
            is PsiDirectory -> navigationTable
            is PsiFile -> navigationTable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots
                    .asSequence()
                    .mapNotNull {
                        PsiManager.getInstance(project).findDirectory(it)
                    }.firstOrNull()
            }
        } ?: return

        JsonInputDialog(project) {
            val map = inputJson2Map(it.inputJson)
            val dartClassDefinition = map2DartClassDefinition(it.inputClassName, map)

            val generator = DartFileGenerator(project, directory)
            generator.dartClass2File(dartClassDefinition)
        }.show()
    }


}
