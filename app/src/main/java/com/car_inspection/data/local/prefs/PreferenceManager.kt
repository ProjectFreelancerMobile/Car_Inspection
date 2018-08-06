package com.car_inspection.data.local.prefs

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@SuppressLint("CommitPrefEdits")
@Suppress("unused","UNCHECKED_CAST","IMPLICIT_CAST_TO_ANY")
object PreferenceManager{
    private lateinit var sharedPreferences: SharedPreferences
    private var initialized = false
    private var mGson: Gson = Gson()
    /**
     * @param application Global context in order use everywhere
     *      without the need for context every time
     * @param sharedPreferencesName custom name for SharedPreferences
     * @return instance of the GlobalSharedPreferences
     */
    fun initialize(application: Application, sharedPreferencesName: String = "DefaultSharedPreferences"): PreferenceManager {
        sharedPreferences = application.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        initialized = true
        return this
    }


    //region editor
    private val edit: SharedPreferences.Editor by lazy { requiredOrThrow(sharedPreferences.edit()) }
    //endregion editor

    //region get
    fun getAll(): Map<String, *> = requiredOrThrow(sharedPreferences.all)

    fun getInt(key: String, defaultValue: Int = 0) =
            requiredOrThrow(sharedPreferences.getInt(key, defaultValue))

    fun getLong(key: String, defaultValue: Long = 0L) =
            requiredOrThrow(sharedPreferences.getLong(key, defaultValue))

    fun getFloat(key: String, defaultValue: Float= 0F) =
            requiredOrThrow(sharedPreferences.getFloat(key, defaultValue))

    fun getBoolean(key: String, defaultValue: Boolean = false) =
            requiredOrThrow(sharedPreferences.getBoolean(key, defaultValue))

    fun getString(key: String, defaultValue: String = ""): String =
            requiredOrThrow(sharedPreferences.getString(key, defaultValue))

    fun getStringSet(key: String, defaultValue: Set<String>): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(key, defaultValue))

    infix fun String.forStringSet(defaultValue: Set<String>): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(this, defaultValue))

    infix fun String.forInt(defaultValue: Int) =
            requiredOrThrow(sharedPreferences.getInt(this, defaultValue))

    infix fun String.forLong(defaultValue: Long) =
            requiredOrThrow(sharedPreferences.getLong(this, defaultValue))

    infix fun String.forFloat(defaultValue: Float) =
            requiredOrThrow(sharedPreferences.getFloat(this, defaultValue))

    infix fun String.forBoolean(defaultValue: Boolean) =
            requiredOrThrow(sharedPreferences.getBoolean(this, defaultValue))

    infix fun String.forString(defaultValue: String): String =
            requiredOrThrow(sharedPreferences.getString(this, defaultValue))

    fun <T : Any> get(key: String, defaultValue: T) =
            try {
                when (defaultValue) {
                    is String ->
                        getString(key, defaultValue)
                    is Int ->
                        getInt(key, defaultValue)
                    is Long ->
                        getLong(key, defaultValue)
                    is Boolean ->
                        getBoolean(key, defaultValue)
                    is Float ->
                        getFloat(key, defaultValue)
                    is Set<*> -> {
                        getStringSet(key, defaultValue as Set<String>)
                    }
                    else -> {
                        throw ClassCastException("GlobalSharedPreferences Only support SharedPreferences types\n type ${defaultValue::class}")
                    }
                } as T
            } catch (ex: ClassCastException) {
                throw Throwable("""
                    Exception while performing get from shared preferences
                    key: $key
                    default value: $defaultValue
                    exception: ${ex.message}
                    ${ex.stackTrace}
                """.trimIndent())
            }


    //endregion get

    //region contains
    operator fun contains(key: String) = requiredOrThrow(sharedPreferences.contains(key))
    //endregion contains

    //region put
    fun put(key: String, value: String) {
        edit.putString(key, value).commit()
    }

    fun put(key: String, value: Int) = also { edit.putInt(key, value).commit() }

    fun put(key: String, value: Long) = also { edit.putLong(key, value).commit() }

    fun put(key: String, value: Boolean) = also { edit.putBoolean(key, value).commit() }

    fun put(key: String, value: Float) = also { edit.putFloat(key, value).commit() }

    fun put(key: String, value: Set<String>) = also { edit.putStringSet(key, value).commit() }

    fun put(key: String, value: Any) =
            also {
                when (value) {
                    is String ->
                        put(key, value)
                    is Int ->
                        put(key, value)
                    is Long ->
                        put(key, value)
                    is Boolean ->
                        put(key, value)
                    is Float ->
                        put(key, value)
                    is Set<*> ->
                        put(key, value.map { it.toString() }.toSet())
                }
            }

    private fun putUnit(key: String, value: Any) {
        put(key, value)
    }

    operator fun String.plusAssign(value: Any) = putUnit(this, value)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) =
            putUnit(keyValuePair.first, keyValuePair.second)

    operator fun plus(keyValuePair: Pair<String, Any>) =
            put(keyValuePair.first, keyValuePair.second)

    //endregion put

    //region remove
    fun remove(key: String) = also { edit.remove(key).commit() }

    fun clear()= also { edit.clear().apply() }

    operator fun minus(key: String) = remove(key)
    //endregion remove

    //region commit/apply
    fun commit() = edit.commit()

    fun apply() = edit.apply()
    //endregion commit/apply

    /**
     * @param returnIfInitialized object to be returned if class is initialized
     * @throws InitializeException
     */
    @Throws(UninitializedPropertyAccessException::class)
    private fun <T> requiredOrThrow(returnIfInitialized: T) = if (initialized) {
        returnIfInitialized
    } else {
        throw UninitializedPropertyAccessException("GlobalSharedPreferences")
    }

    @Synchronized
    fun put(key: String, value: List<*>) = also { edit.putString(key, mGson.toJson(value)).commit() }

    @Synchronized
    fun <T> getEntity(key: String, classOfT: Class<T>): T? {
        var entity: T? = null
        try {
            entity = mGson.fromJson(getString(key, ""), classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return entity
    }

    @Synchronized
    fun <T : Any> getList(key: String, classOfT: Class<T>?): MutableList<T> {
        val list = ArrayList<T>()
        try {
            mGson.fromJson<ArrayList<JsonElement>>(getString(key, ""), object : TypeToken<ArrayList<JsonElement>>() {}.type).mapNotNullTo(list) { mGson.fromJson(it, classOfT) }
        } catch (e: Exception) {
            e.printStackTrace()
            return list
        }
        return list
    }
}

inline fun pref(sharedPreferences: PreferenceManager.() -> Unit) = with(PreferenceManager) {
    also {
        sharedPreferences()
        apply()
    }
}