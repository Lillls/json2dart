package com.xiaou.plugin.json2dart.aticon

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.xiaou.plugin.json2dart.DartFileGenerator
import com.xiaou.plugin.json2dart.inputJson2Map
import com.xiaou.plugin.json2dart.map2DartClassDefinition
import com.xiaou.plugin.json2dart.ui.JsonInputDialog
import com.xiaou.plugin.json2dart.utils.DartClassUtils


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
            //去除文件后缀名
            val fileName = it.inputClassName.split(".")[0]
            //TODO 转为符合Dart命名规范的文件名字
            if (containsFile(directory, fileName)) {
                Messages.showInfoMessage(project, "The $fileName.dart already exists", "Info")
                return@JsonInputDialog
            }
            val map = inputJson2Map(it.inputJson)
            val dartClassDefinition = map2DartClassDefinition(fileName, map)
            val generator = DartFileGenerator(project, directory)
            val originalClassCodeContent = generator.classDefinition2ClassCode(dartClassDefinition)
            val codeContent = DartClassUtils.insertClassHead(fileName, originalClassCodeContent)
            generator.generateDarFile(fileName, codeContent)

        }.show()
    }


    private fun containsFile(directory: PsiDirectory, fileName: String): Boolean {
        return directory.files.filter { it.name.endsWith(".dart") }
            .firstOrNull { fileName.equals(it.name, true) } != null
    }
}