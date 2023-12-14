package util

fun String.removePrefixTrim(prefix: String) = this.trim().removePrefix(prefix).trim()