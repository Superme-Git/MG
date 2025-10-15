# Install script for directory: /Users/magicstudio/Public/3rd/pcre-8.31

# Set the install prefix
IF(NOT DEFINED CMAKE_INSTALL_PREFIX)
  SET(CMAKE_INSTALL_PREFIX "/usr/local")
ENDIF(NOT DEFINED CMAKE_INSTALL_PREFIX)
STRING(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
IF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  IF(BUILD_TYPE)
    STRING(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  ELSE(BUILD_TYPE)
    SET(CMAKE_INSTALL_CONFIG_NAME "Release")
  ENDIF(BUILD_TYPE)
  MESSAGE(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
ENDIF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)

# Set the component getting installed.
IF(NOT CMAKE_INSTALL_COMPONENT)
  IF(COMPONENT)
    MESSAGE(STATUS "Install component: \"${COMPONENT}\"")
    SET(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  ELSE(COMPONENT)
    SET(CMAKE_INSTALL_COMPONENT)
  ENDIF(COMPONENT)
ENDIF(NOT CMAKE_INSTALL_COMPONENT)

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  IF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcre.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcre.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Mm][Ii][Nn][Ss][Ii][Zz][Ee][Rr][Ee][Ll])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcre.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ww][Ii][Tt][Hh][Dd][Ee][Bb][Ii][Nn][Ff][Oo])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcre.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcre.a")
    ENDIF()
  ENDIF()
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  IF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcreposix.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcreposix.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Mm][Ii][Nn][Ss][Ii][Zz][Ee][Rr][Ee][Ll])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcreposix.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ww][Ii][Tt][Hh][Dd][Ee][Bb][Ii][Nn][Ff][Oo])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcreposix.a")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
      EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/lib/libpcreposix.a")
    ENDIF()
  ENDIF()
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  IF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcregrep")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcregrep")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Mm][Ii][Nn][Ss][Ii][Zz][Ee][Rr][Ee][Ll])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcregrep")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ww][Ii][Tt][Hh][Dd][Ee][Bb][Ii][Nn][Ff][Oo])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcregrep")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcregrep")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ENDIF()
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  IF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcretest")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcretest")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Mm][Ii][Nn][Ss][Ii][Zz][Ee][Rr][Ee][Ll])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcretest")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ELSEIF("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ww][Ii][Tt][Hh][Dd][Ee][Bb][Ii][Nn][Ff][Oo])$")
    FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/bin" TYPE EXECUTABLE FILES "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcretest")
    IF(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest" AND
       NOT IS_SYMLINK "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      IF(CMAKE_INSTALL_DO_STRIP)
        EXECUTE_PROCESS(COMMAND "/usr/bin/strip" "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/bin/pcretest")
      ENDIF(CMAKE_INSTALL_DO_STRIP)
    ENDIF()
  ENDIF()
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include" TYPE FILE FILES
    "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/pcre.h"
    "/Users/magicstudio/Public/3rd/pcre-8.31/pcreposix.h"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/man/man1" TYPE FILE FILES
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre-config.1"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcregrep.1"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcretest.1"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/man/man3" TYPE FILE FILES
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre16.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_assign_jit_stack.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_compile.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_compile2.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_config.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_copy_named_substring.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_copy_substring.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_dfa_exec.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_exec.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_free_study.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_free_substring.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_free_substring_list.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_fullinfo.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_get_named_substring.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_get_stringnumber.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_get_stringtable_entries.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_get_substring.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_get_substring_list.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_jit_stack_alloc.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_jit_stack_free.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_maketables.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_pattern_to_host_byte_order.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_refcount.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_study.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_utf16_to_host_byte_order.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcre_version.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreapi.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrebuild.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrecallout.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrecompat.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrecpp.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrejit.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrelimits.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrematching.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrepartial.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrepattern.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreperform.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreposix.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreprecompile.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcresample.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcrestack.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcresyntax.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreunicode.3"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/pcreunicode.3"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  FILE(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/doc/pcre/html" TYPE FILE FILES
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/index.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre-config.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre16.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_assign_jit_stack.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_compile.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_compile2.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_config.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_copy_named_substring.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_copy_substring.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_dfa_exec.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_exec.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_free_study.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_free_substring.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_free_substring_list.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_fullinfo.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_get_named_substring.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_get_stringnumber.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_get_stringtable_entries.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_get_substring.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_get_substring_list.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_jit_stack_alloc.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_jit_stack_free.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_maketables.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_pattern_to_host_byte_order.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_refcount.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_study.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_utf16_to_host_byte_order.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcre_version.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcreapi.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrebuild.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrecallout.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrecompat.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrecpp.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcredemo.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcregrep.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrejit.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrelimits.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrematching.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrepartial.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrepattern.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcreperform.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcreposix.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcreprecompile.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcresample.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcrestack.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcresyntax.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcretest.html"
    "/Users/magicstudio/Public/3rd/pcre-8.31/doc/html/pcreunicode.html"
    )
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

IF(CMAKE_INSTALL_COMPONENT)
  SET(CMAKE_INSTALL_MANIFEST "install_manifest_${CMAKE_INSTALL_COMPONENT}.txt")
ELSE(CMAKE_INSTALL_COMPONENT)
  SET(CMAKE_INSTALL_MANIFEST "install_manifest.txt")
ENDIF(CMAKE_INSTALL_COMPONENT)

FILE(WRITE "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/${CMAKE_INSTALL_MANIFEST}" "")
FOREACH(file ${CMAKE_INSTALL_MANIFEST_FILES})
  FILE(APPEND "/Users/magicstudio/Public/3rd/pcre-8.31/prj2/${CMAKE_INSTALL_MANIFEST}" "${file}\n")
ENDFOREACH(file)
