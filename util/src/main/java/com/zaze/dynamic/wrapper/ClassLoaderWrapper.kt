package com.zaze.dynamic.wrapper

open class ClassLoaderWrapper(private val mBase: ClassLoader) {
    companion object {
        private val findLibraryMethod by lazy {
            ClassLoader::class.java.getDeclaredMethod("findLibrary", String::class.java)
        }
    }

    fun loadClass(name: String): Class<*> {
        return mBase.loadClass(name)
    }

    fun findLibrary(libName: String): String? {
        return findLibraryMethod.invoke(this, libName) as? String
    }
}

fun ClassLoader.wrapper() = ClassLoaderWrapper(this)