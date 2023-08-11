# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

project(mynative-lib)

# 指定动态库输出目录
#set(OUT_DIR ${PROJECT_SOURCE_DIR}/lib)
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib/${CMAKE_ANDROID_ARCH_ABI})
#set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib2/${CMAKE_ANDROID_ARCH_ABI})
#set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib3/${CMAKE_ANDROID_ARCH_ABI})

# 定义变量 LIBS_DIR 指向 libs 目录
set(LIBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)

# message(${CMAKE_ANDROID_ARCH_ABI})
# message(${ANDROID_ABI})

add_library(jpeg SHARED IMPORTED)
set_target_properties(jpeg
        PROPERTIES
        IMPORTED_LOCATION ${LIBS_DIR}/${CMAKE_ANDROID_ARCH_ABI}/libjpeg.so
        )

add_library(jpeg-turbo SHARED IMPORTED)
set_target_properties(jpeg-turbo
        PROPERTIES
        IMPORTED_LOCATION ${LIBS_DIR}/${CMAKE_ANDROID_ARCH_ABI}/libturbojpeg.so
        )

# 配置 FLAGS
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
# -fno-rtti 禁用运行时类型信息
# -fno-exceptions 禁用异常机制
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")

add_library(
        mynative-lib
        SHARED
        native-lib.cpp
        bitmap-compress-lib.cpp
)

# 添加头文件，代码提示
target_include_directories(
        mynative-lib
        PRIVATE
        ${LIBS_DIR}/include
)


find_library(log-lib log)

# 添加链接库；符号表
target_link_libraries(
        mynative-lib # 目标
        jpeg
        jpeg-turbo
        jnigraphics # AndroidBitmap
        ${log-lib} # log库 地址
)
