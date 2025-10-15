#ifndef __FIRENET_BOO_H__
#define __FIRENET_BOO_H__

template<bool x> struct STATIC_ASSERTION_FAILURE;
template<> struct STATIC_ASSERTION_FAILURE<true>{};
template<int x> struct static_assert_test{};
#define STATIC_ASSERT(B) typedef static_assert_test<sizeof(STATIC_ASSERTION_FAILURE<(bool)(B)>)> static_assert_typedef_ ## __LINE__

#endif // __FIRENET_BOO_H__

