package util

import java.io.File

fun readLines(fileName: String) = File(getFileName(fileName)).readLines()
fun readText(fileName: String) = File(getFileName(fileName)).readText()

private fun getFileName(fileName: String) = "src/main/kotlin/${Thread.currentThread().stackTrace[3].className.split('.')[0]}/$fileName"
