# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)
project(libbsdiff_android)

# 编译 bsdiff
add_subdirectory(${CMAKE_CURRENT_SOURCE_DIR}/bsdiff)

# 指定动态库输出目录
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib/${CMAKE_ANDROID_ARCH_ABI})

#
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

add_library(
        bsdiff-android
        SHARED
        bsdiff-android.cpp
)

find_library( # Sets the name of the path variable.
        log-lib
        log)

target_include_directories(
        bsdiff-android
        PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/bsdiff
)

target_link_libraries( # Specifies the target library.
        bsdiff-android
        bsdiff
        ${log-lib})