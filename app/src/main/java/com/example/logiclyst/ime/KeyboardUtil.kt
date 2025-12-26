package com.example.logiclyst.ime

import android.content.Context
import android.provider.Settings
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun isKeyboardEnabled(context: Context): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledMethods = imm.enabledInputMethodList
        return enabledMethods.any { it.packageName == context.packageName }
    }

    // Mengecek apakah keyboard sedang "DIPAKAI" saat ini sebagai default
    fun isKeyboardSelected(context: Context): Boolean {
        val currentId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        )
        return currentId != null && currentId.contains(context.packageName)
    }
}