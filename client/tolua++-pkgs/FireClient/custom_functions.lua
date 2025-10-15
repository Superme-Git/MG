_basic['std::wstring'] = 'cppwstring'
_basic_ctype.cppwstring = 'const wchar_t*'


_is_functions = _is_functions or {}
_to_functions = _to_functions or {}
_push_functions = _push_functions or {}

-- register LUA_FUNCTION type
_to_functions["LUA_FUNCTION"] = "tolua_ref_function"
_is_functions["LUA_FUNCTION"] = "tolua_isfunction"

_to_functions["LUAOBJ_REF"] = "tolua_ref_luaobj"
_is_functions["LUAOBJ_REF"] = "tolua_isluaobj"
