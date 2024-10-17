package com.example.foodup.Helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import com.example.foodup.Domain.Foods
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class TinyDB(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var defaultAppImageDataDirectory: String? = null
    private var lastImagePath: String = ""

    fun getImage(path: String?): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSavedImagePath(): String {
        return lastImagePath
    }

    fun putImage(theFolder: String?, theImageName: String?, theBitmap: Bitmap?): String? {
        if (theFolder == null || theImageName == null || theBitmap == null) return null

        defaultAppImageDataDirectory = theFolder
        val fullPath = setupFullPath(theImageName)

        if (fullPath.isNotEmpty()) {
            lastImagePath = fullPath
            saveBitmap(fullPath, theBitmap)
        }

        return fullPath
    }

    fun putImageWithFullPath(fullPath: String?, theBitmap: Bitmap?): Boolean {
        return fullPath != null && theBitmap != null && saveBitmap(fullPath, theBitmap)
    }

    private fun setupFullPath(imageName: String): String {
        val folder = File(Environment.getExternalStorageDirectory(), defaultAppImageDataDirectory)

        if (isExternalStorageReadable() && isExternalStorageWritable() && !folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder")
                return ""
            }
        }

        return "${folder.path}/$imageName"
    }

    private fun saveBitmap(fullPath: String?, bitmap: Bitmap?): Boolean {
        if (fullPath == null || bitmap == null) return false

        val imageFile = File(fullPath)
        if (imageFile.exists() && !imageFile.delete()) return false

        var fileCreated = false
        var bitmapCompressed = false
        var streamClosed = false

        try {
            fileCreated = imageFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(imageFile)
            bitmapCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
            bitmapCompressed = false
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                    streamClosed = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    streamClosed = false
                }
            }
        }

        return fileCreated && bitmapCompressed && streamClosed
    }

    // Getters

    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    fun getListInt(key: String): ArrayList<Int> {
        val myList = TextUtils.split(preferences.getString(key, "") ?: "", "‚‗‚")
        return ArrayList(myList.map { it.toInt() })
    }

    fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    fun getFloat(key: String): Float {
        return preferences.getFloat(key, 0f)
    }

    fun getDouble(key: String): Double {
        val number = getString(key)
        return try {
            number.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun getListDouble(key: String): ArrayList<Double> {
        val myList = TextUtils.split(preferences.getString(key, "") ?: "", "‚‗‚")
        return ArrayList(myList.map { it.toDouble() })
    }

    fun getListLong(key: String): ArrayList<Long> {
        val myList = TextUtils.split(preferences.getString(key, "") ?: "", "‚‗‚")
        return ArrayList(myList.map { it.toLong() })
    }

    fun getString(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun getListString(key: String): ArrayList<String> {
        return ArrayList(TextUtils.split(preferences.getString(key, "") ?: "", "‚‗‚").toList())
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun getListBoolean(key: String): ArrayList<Boolean> {
        val myList = getListString(key)
        return ArrayList(myList.map { it == "true" })
    }

    fun getListObject(key: String): ArrayList<Foods> {
        val gson = Gson()
        val objStrings = getListString(key)
        return ArrayList(objStrings.map { gson.fromJson(it, Foods::class.java) })
    }

    fun <T> getObject(key: String, classOfT: Class<T>): T {
        val json = getString(key)
        return Gson().fromJson(json, classOfT) ?: throw NullPointerException()
    }

    // Put methods

    fun putInt(key: String, value: Int) {
        checkForNullKey(key)
        preferences.edit().putInt(key, value).apply()
    }

    fun putListInt(key: String, intList: ArrayList<Int>) {
        checkForNullKey(key)
        preferences.edit().putString(key, TextUtils.join("‚‗‚", intList)).apply()
    }

    fun putLong(key: String, value: Long) {
        checkForNullKey(key)
        preferences.edit().putLong(key, value).apply()
    }

    fun putListLong(key: String, longList: ArrayList<Long>) {
        checkForNullKey(key)
        preferences.edit().putString(key, TextUtils.join("‚‗‚", longList)).apply()
    }

    fun putFloat(key: String, value: Float) {
        checkForNullKey(key)
        preferences.edit().putFloat(key, value).apply()
    }

    fun putDouble(key: String, value: Double) {
        checkForNullKey(key)
        putString(key, value.toString())
    }

    fun putListDouble(key: String, doubleList: ArrayList<Double>) {
        checkForNullKey(key)
        preferences.edit().putString(key, TextUtils.join("‚‗‚", doubleList)).apply()
    }

    fun putString(key: String, value: String) {
        checkForNullKey(key)
        checkForNullValue(value)
        preferences.edit().putString(key, value).apply()
    }

    fun putListString(key: String, stringList: ArrayList<String>) {
        checkForNullKey(key)
        preferences.edit().putString(key, TextUtils.join("‚‗‚", stringList)).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        checkForNullKey(key)
        preferences.edit().putBoolean(key, value).apply()
    }

    fun putListBoolean(key: String, boolList: ArrayList<Boolean>) {
        checkForNullKey(key)
        val newList = ArrayList(boolList.map { if (it) "true" else "false" })
        putListString(key, newList)
    }

    fun putObject(key: String, obj: Any) {
        checkForNullKey(key)
        val gson = Gson()
        putString(key, gson.toJson(obj))
    }

    fun putListObject(key: String, playerList: ArrayList<Foods>) {
        checkForNullKey(key)
        val gson = Gson()
        val objStrings = ArrayList(playerList.map { gson.toJson(it) })
        putListString(key, objStrings)
    }

    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun deleteImage(path: String): Boolean {
        return File(path).delete()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    fun getAll(): Map<String, *> {
        return preferences.all
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        fun isExternalStorageWritable(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        }

        fun isExternalStorageReadable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }
    }

    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }

    private fun checkForNullValue(value: String?) {
        if (value == null) {
            throw NullPointerException()
        }
    }
}
