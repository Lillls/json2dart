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
import com.xiaou.plugin.json2dart.parseInputJson
import com.xiaou.plugin.json2dart.map2CustomClassDefinition
import com.xiaou.plugin.json2dart.ui.JsonInputDialog
import com.xiaou.plugin.json2dart.utils.DartClassUtils
import com.xiaou.plugin.json2dart.utils.hump2Underline
import com.xiaou.plugin.json2dart.utils.toLowerCaseFirstOne


class Json2DartAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val dataContext = event.dataContext
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return
        //Get the folder selected by the right mouse button
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
            //Remove the file suffix
            var fileName = it.inputClassName.split(".")[0]
            fileName = fileName.hump2Underline().toLowerCaseFirstOne()
            if (containsFile(directory, fileName)) {
                Messages.showInfoMessage(project, "The $fileName.dart already exists", "Info")
                return@JsonInputDialog
            }
            val map = parseInputJson(it.inputJson)
            val dartClassDefinition = map2CustomClassDefinition(fileName, map)
            val generator = DartFileGenerator(project, directory)
            val originalClassCodeContent = generator.class2Code(dartClassDefinition)
            val codeContent = DartClassUtils.insertClassHead(fileName, originalClassCodeContent)
            generator.generateDarFile(fileName, codeContent)

        }.show()
    }


    private fun containsFile(directory: PsiDirectory, fileName: String): Boolean {
        return directory.files.filter { it.name.endsWith(".dart") }
            .firstOrNull { fileName.equals(it.name.split(".dart")[0], true) } != null
    }
}