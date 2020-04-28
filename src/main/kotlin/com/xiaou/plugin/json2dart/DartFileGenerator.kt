package com.xiaou.plugin.json2dart

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.jetbrains.lang.dart.DartFileType
import com.xiaou.plugin.json2dart.utils.DartClassUtils

class DartFileGenerator(private val project: Project, private val directory: PsiDirectory) {

    private fun generateDarFile(fileName: String, classCodeContent: String) {
        val psiFileFactory = PsiFileFactory.getInstance(project)

        CommandProcessor.getInstance().executeCommand(directory.project, {
            ApplicationManager.getApplication().runWriteAction {
                val file =
                    psiFileFactory.createFileFromText("$fileName.dart", DartFileType.INSTANCE, classCodeContent)
                directory.add(file)
            }
        }, "FlutterJsonBeanFactory", "FlutterJsonBeanFactory")
    }

    fun dartClass2File(dartClass: DartClassDefinition) {
        val sb = StringBuilder()
        sb.append(DartClassUtils.IMPORT_CONSTANT)
        sb.append("\n")
        dartClass.childClassDefinition.forEach {
            sb.append(DartClassUtils.importChildClassStr(it))
            sb.append("\n")
        }
        sb.append("\n")
        sb.append(DartClassUtils.partStr(dartClass.fileName))
        sb.append("\n")
        sb.append("\n")
        sb.append(DartClassUtils.dartClassStartStr(dartClass.className))
        sb.append("\n")
        dartClass.fields.forEach {
            sb.append(DartClassUtils.fieldsStr(it))
            sb.append("\n")
        }
        dartClass.childClassDefinition.forEach {
            sb.append(DartClassUtils.childClassDefinitionStr(it))
            dartClass2File(it)
            sb.append("\n")
        }
        sb.append("\n")
        sb.append(DartClassUtils.constructorStr(dartClass))
        sb.append("\n")
        sb.append("\n")
        sb.append(DartClassUtils.factoryConstructorStr(dartClass.className))
        sb.append("\n")
        sb.append("\n")
        sb.append(DartClassUtils.toJsonStr(dartClass.className))
        sb.append("\n")
        sb.append("\n")
        sb.append(DartClassUtils.dartClassEndStr())
        generateDarFile(dartClass.fileName, sb.toString())
    }

}