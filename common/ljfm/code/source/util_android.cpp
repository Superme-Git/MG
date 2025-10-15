#ifdef _OS_ANDROID
#include "util_android.h"


int
wcsncasecmp(wchar_t const *s1, wchar_t const *s2, size_t n)
{
	wchar_t c1, c2;

	if (n == 0)
		return (0);
	for (; *s1; s1++, s2++) {
		c1 = towlower(*s1);
		c2 = towlower(*s2);
		if (c1 != c2)
			return ((int)c1 - c2);
		if (--n == 0)
			return (0);
		}
	return (-*s2);
}

int
wcscasecmp(wchar_t const *s1, wchar_t const *s2)
{
	wchar_t c1, c2;

	for (; *s1; s1++, s2++) {
		c1 = towlower(*s1);
		c2 = towlower(*s2);
		if (c1 != c2)
			return ((int)c1 - c2);
	}
	return (-*s2);
}
#endif
