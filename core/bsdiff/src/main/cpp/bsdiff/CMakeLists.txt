cmake_minimum_required(VERSION 3.22.1)
project(libbsdiff C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -Wall")

# 指定动态库输出目录
#set(STATIC_LIBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../build/static)
#set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${STATIC_LIBS_DIR}/${CMAKE_ANDROID_ARCH_ABI})

# -----
set(bsdiff_source_dir ${CMAKE_CURRENT_SOURCE_DIR})
set(
        bsdiff_source
        ${bsdiff_source_dir}/bsdiff.c
        ${bsdiff_source_dir}/bspatch.c
#        ${bsdiff_source_dir}/err.c

        ${bsdiff_source_dir}/bzip2/blocksort.c
        ${bsdiff_source_dir}/bzip2/bzip2.c
        ${bsdiff_source_dir}/bzip2/bzlib.c
        ${bsdiff_source_dir}/bzip2/compress.c
        ${bsdiff_source_dir}/bzip2/crctable.c
        ${bsdiff_source_dir}/bzip2/decompress.c
        ${bsdiff_source_dir}/bzip2/huffman.c
        ${bsdiff_source_dir}/bzip2/randtable.c
        #        ${bzip2_source_dir}/bzip2/bzip2recover.c
)

add_library(bsdiff STATIC ${bsdiff_source})

find_library(log-lib log)

target_include_directories(
        bsdiff
        PUBLIC ${bsdiff_source_dir}
        PUBLIC ${bsdiff_source_dir}/bzip2
)

target_link_libraries(bsdiff
        PUBLIC ${log-lib}
        )

#aux_source_directory(. SOURCE)
#add_executable(bspatch  bspatch.c ${SOURCE})