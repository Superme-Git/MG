#ifndef __FIRENET_SECURITY_H__
#define __FIRENET_SECURITY_H__

#include "octets.h"
#include "streamcompress.h"

namespace FireNet
{
	enum { enumSECURITY_RANDOM = 0, enumSECURITY_NULL = 1, enumSECURITY_ARCFOUR = 2, MD5HASH = 3, enumSECURITY_HMAC_MD5HASH = 4, enumSECURITY_COMPRESSARCFOUR = 5, enumSECURITY_DECOMPRESSARCFOURSE = 6, enumSECURITY_SHA1HASH = 7 };

	class Security
	{
	public:
		typedef unsigned int Type;
	private:
		Type type;
		typedef std::map<Type, const Security *> Map;
		static Map& GetMap();
	protected:
		Security();
		virtual ~Security();
		Security(Type t);

	public:
		Security(const Security &rhs);
		virtual void SetParameter(const Octets &);
		virtual void GetParameter(Octets &);
		virtual Octets& UpdateSelf(Octets &) = 0;
		virtual Octets& Final(Octets &o);
		virtual Security *Clone() const = 0;
		virtual void Destroy();
		static Security* Create(Type type);
	};

	class Random : public Security
	{
		static int fd;
		static int Init();

		Security *Clone() const;
	public:
		Random();
		Random(Type type);
		Random(const Random &rhs);
		Octets& UpdateSelf(Octets &o);
	};

	class NullSecurity : public Security
	{
		Security *Clone() const { return new NullSecurity(*this); }
	public:
		NullSecurity() { }
		NullSecurity(Type type) : Security(type) { }
		NullSecurity(const NullSecurity &rhs) : Security(rhs) { }
		Octets& UpdateSelf(Octets &o) { return o; }
	};

	class ARCFourSecurity : public Security
	{
		unsigned char index1;
		unsigned char index2;
		unsigned char perm[256];
		Security *Clone() const;
	public:
		ARCFourSecurity();
		ARCFourSecurity(Type type);
		ARCFourSecurity(const ARCFourSecurity &rhs);

		void SetParameter(const Octets &mParam);
		Octets& UpdateSelf(Octets &o);
	};

	class MD5Hash : public Security
	{
		unsigned int state[4];
		unsigned int low_count, high_count, remain;
		unsigned char buffer[64];

		Security *Clone() const;
		void transform (const unsigned char block[64]);
		void update(const unsigned char *input, unsigned int inputlen);
		void init();
	public:
		MD5Hash();
		MD5Hash(Type type);
		MD5Hash(const MD5Hash &rhs);
		Octets& UpdateSelf(Octets &o);
		Octets& Final(Octets &digest);

		static Octets Digest(const Octets &o);
	};

	class HMAC_MD5Hash : public Security
	{
		Octets k_opad;
		MD5Hash md5hash;
		Security *Clone() const;
	public:
		HMAC_MD5Hash();
		HMAC_MD5Hash(Type type);
		HMAC_MD5Hash(const HMAC_MD5Hash &rhs);
		void SetParameter(const Octets &mParam);
		Octets& UpdateSelf(Octets &o);
		Octets& Final(Octets &digest);
	};

	class CompressARCFourSecurity : public Security
	{
		ARCFourSecurity arc4;
		Compress compress;
		Security *Clone() const { return new CompressARCFourSecurity(*this); }
	public:
		CompressARCFourSecurity() { }
		CompressARCFourSecurity(Type type) : Security(type) { }
		void SetParameter(const Octets &mParam) { arc4.SetParameter(mParam); }
		Octets& UpdateSelf(Octets &o)
		{
			arc4.UpdateSelf(compress.Final(o));
			return o;
		}
	};

	class DecompressARCFourSecurity : public Security
	{
		ARCFourSecurity arc4;
		Decompress decompress;
		Security *Clone() const { return new DecompressARCFourSecurity(*this); }
	public:
		DecompressARCFourSecurity() { }
		DecompressARCFourSecurity(Type type) : Security(type) { }
		void SetParameter(const Octets &mParam) { arc4.SetParameter(mParam); }
		Octets& UpdateSelf(Octets &o) { return decompress.UpdateSelf(arc4.UpdateSelf(o)); }
	};

	class SHA1Hash : public Security
	{
		unsigned int state[5];
		unsigned int low_count, high_count, remain;
		unsigned char buffer[64];

		static inline unsigned int rol1(unsigned int x)  { return (x << 1) | (x >> 31); }
		static inline unsigned int rol5(unsigned int x)  { return (x << 5) | (x >> 27); }
		static inline unsigned int rol30(unsigned int x) { return (x << 30) | (x >> 2); }

		void transform (const unsigned char block[64]);
		void update(const unsigned char *input, unsigned int inputlen);
		void init();
		Security *Clone() const;
	public:
		SHA1Hash();
		SHA1Hash(Type type);
		SHA1Hash(const SHA1Hash &rhs);
		Octets& UpdateSelf(Octets &o);
		Octets& Final(Octets &digest);
		static Octets Digest(const Octets &o);	};

}// namespace FireNet {

namespace SECURITY_WIN32
{
	class MD5Hash
	{
		unsigned int state[4];
		unsigned int low_count, high_count, remain;
		unsigned char buffer[64];

		void transform(const unsigned char block[64])
		{
#define f(x, y, z) (z ^ (x & (y ^ z)))
#define g(x, y, z) (y ^ (z & (x ^ y)))
#define h(x, y, z) ((x) ^ (y) ^ (z))
#define i(x, y, z) ((y) ^ ((x) | (~z)))
#define rotate_left(x, n) (((x) << (n)) | ((x) >> (32-(n))))

			unsigned int a = state[0], b = state[1], c = state[2], d = state[3];
			unsigned int *x = (unsigned int *)block;

			/* round 1 */
#define ff(a, b, c, d, x, s, mAc) { \
				(a) += f ((b), (c), (d)) + (x) + (unsigned int)(mAc); \
				(a) = rotate_left ((a), (s)); \
				(a) += (b); \
									}
			ff(a, b, c, d, x[0], 7, 0xd76aa478); /* 1 */
			ff(d, a, b, c, x[1], 12, 0xe8c7b756); /* 2 */
			ff(c, d, a, b, x[2], 17, 0x242070db); /* 3 */
			ff(b, c, d, a, x[3], 22, 0xc1bdceee); /* 4 */
			ff(a, b, c, d, x[4], 7, 0xf57c0faf); /* 5 */
			ff(d, a, b, c, x[5], 12, 0x4787c62a); /* 6 */
			ff(c, d, a, b, x[6], 17, 0xa8304613); /* 7 */
			ff(b, c, d, a, x[7], 22, 0xfd469501); /* 8 */
			ff(a, b, c, d, x[8], 7, 0x698098d8); /* 9 */
			ff(d, a, b, c, x[9], 12, 0x8b44f7af); /* 10 */
			ff(c, d, a, b, x[10], 17, 0xffff5bb1); /* 11 */
			ff(b, c, d, a, x[11], 22, 0x895cd7be); /* 12 */
			ff(a, b, c, d, x[12], 7, 0x6b901122); /* 13 */
			ff(d, a, b, c, x[13], 12, 0xfd987193); /* 14 */
			ff(c, d, a, b, x[14], 17, 0xa679438e); /* 15 */
			ff(b, c, d, a, x[15], 22, 0x49b40821); /* 16 */
#undef ff

			/* round 2 */
#define gg(a, b, c, d, x, s, mAc) { \
				(a) += g ((b), (c), (d)) + (x) + (unsigned int)(mAc); \
				(a) = rotate_left ((a), (s)); \
				(a) += (b); \
									}
			gg(a, b, c, d, x[1], 5, 0xf61e2562); /* 17 */
			gg(d, a, b, c, x[6], 9, 0xc040b340); /* 18 */
			gg(c, d, a, b, x[11], 14, 0x265e5a51); /* 19 */
			gg(b, c, d, a, x[0], 20, 0xe9b6c7aa); /* 20 */
			gg(a, b, c, d, x[5], 5, 0xd62f105d); /* 21 */
			gg(d, a, b, c, x[10], 9, 0x2441453); /* 22 */
			gg(c, d, a, b, x[15], 14, 0xd8a1e681); /* 23 */
			gg(b, c, d, a, x[4], 20, 0xe7d3fbc8); /* 24 */
			gg(a, b, c, d, x[9], 5, 0x21e1cde6); /* 25 */
			gg(d, a, b, c, x[14], 9, 0xc33707d6); /* 26 */
			gg(c, d, a, b, x[3], 14, 0xf4d50d87); /* 27 */
			gg(b, c, d, a, x[8], 20, 0x455a14ed); /* 28 */
			gg(a, b, c, d, x[13], 5, 0xa9e3e905); /* 29 */
			gg(d, a, b, c, x[2], 9, 0xfcefa3f8); /* 30 */
			gg(c, d, a, b, x[7], 14, 0x676f02d9); /* 31 */
			gg(b, c, d, a, x[12], 20, 0x8d2a4c8a); /* 32 */
#undef gg 

			/* round 3 */
#define hh(a, b, c, d, x, s, mAc) { \
				(a) += h ((b), (c), (d)) + (x) + (unsigned int)(mAc); \
				(a) = rotate_left ((a), (s)); \
				(a) += (b); \
									}
			hh(a, b, c, d, x[5], 4, 0xfffa3942); /* 33 */
			hh(d, a, b, c, x[8], 11, 0x8771f681); /* 34 */
			hh(c, d, a, b, x[11], 16, 0x6d9d6122); /* 35 */
			hh(b, c, d, a, x[14], 23, 0xfde5380c); /* 36 */
			hh(a, b, c, d, x[1], 4, 0xa4beea44); /* 37 */
			hh(d, a, b, c, x[4], 11, 0x4bdecfa9); /* 38 */
			hh(c, d, a, b, x[7], 16, 0xf6bb4b60); /* 39 */
			hh(b, c, d, a, x[10], 23, 0xbebfbc70); /* 40 */
			hh(a, b, c, d, x[13], 4, 0x289b7ec6); /* 41 */
			hh(d, a, b, c, x[0], 11, 0xeaa127fa); /* 42 */
			hh(c, d, a, b, x[3], 16, 0xd4ef3085); /* 43 */
			hh(b, c, d, a, x[6], 23, 0x4881d05); /* 44 */
			hh(a, b, c, d, x[9], 4, 0xd9d4d039); /* 45 */
			hh(d, a, b, c, x[12], 11, 0xe6db99e5); /* 46 */
			hh(c, d, a, b, x[15], 16, 0x1fa27cf8); /* 47 */
			hh(b, c, d, a, x[2], 23, 0xc4ac5665); /* 48 */
#undef hh 

			/* round 4 */
#define ii(a, b, c, d, x, s, mAc) { \
				(a) += i ((b), (c), (d)) + (x) + (unsigned int)(mAc); \
				(a) = rotate_left ((a), (s)); \
				(a) += (b); \
									}
			ii(a, b, c, d, x[0], 6, 0xf4292244); /* 49 */
			ii(d, a, b, c, x[7], 10, 0x432aff97); /* 50 */
			ii(c, d, a, b, x[14], 15, 0xab9423a7); /* 51 */
			ii(b, c, d, a, x[5], 21, 0xfc93a039); /* 52 */
			ii(a, b, c, d, x[12], 6, 0x655b59c3); /* 53 */
			ii(d, a, b, c, x[3], 10, 0x8f0ccc92); /* 54 */
			ii(c, d, a, b, x[10], 15, 0xffeff47d); /* 55 */
			ii(b, c, d, a, x[1], 21, 0x85845dd1); /* 56 */
			ii(a, b, c, d, x[8], 6, 0x6fa87e4f); /* 57 */
			ii(d, a, b, c, x[15], 10, 0xfe2ce6e0); /* 58 */
			ii(c, d, a, b, x[6], 15, 0xa3014314); /* 59 */
			ii(b, c, d, a, x[13], 21, 0x4e0811a1); /* 60 */
			ii(a, b, c, d, x[4], 6, 0xf7537e82); /* 61 */
			ii(d, a, b, c, x[11], 10, 0xbd3af235); /* 62 */
			ii(c, d, a, b, x[2], 15, 0x2ad7d2bb); /* 63 */
			ii(b, c, d, a, x[9], 21, 0xeb86d391); /* 64 */
#undef ii 
#undef rotate_left 
#undef i
#undef h
#undef g
#undef f
			state[0] += a;
			state[1] += b;
			state[2] += c;
			state[3] += d;
		}
		void NetLoginModule_OnUpdate(const unsigned char *input, unsigned int inputlen)
		{
			if (remain)
			{
				unsigned int copy_len = 64 - remain;
				if (inputlen < copy_len)
				{
					memcpy(buffer + remain, input, inputlen);
					remain += inputlen;
					return;
				}
				memcpy(buffer + remain, input, copy_len);
				transform(buffer);
				inputlen -= copy_len;
				input += copy_len;
				if ((low_count += 512) == 0) high_count++;
			}
			for (; inputlen >= 64; inputlen -= 64, input += 64)
			{
				transform(input);
				if ((low_count += 512) == 0) high_count++;
			}
			if ((remain = inputlen) > 0) memcpy(buffer, input, remain);
		}
		void init()
		{
			state[0] = 0x67452301;
			state[1] = 0xefcdab89;
			state[2] = 0x98badcfe;
			state[3] = 0x10325476;
			low_count = high_count = remain = 0;
		}
	public:
		MD5Hash() { init(); }
		void UpdateSelf(const unsigned char *input, unsigned int inputlen)
		{
			NetLoginModule_OnUpdate(input, inputlen);
		}
		const unsigned char * Final()
		{
			static unsigned char padding[64] = {
				0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
			};
			unsigned int l = low_count, h = high_count;
			if ((l += remain * 8) < l) h++;
			unsigned int index = remain & 0x3f;
			unsigned int padlen = (index < 56) ? (56 - index) : (120 - index);
			NetLoginModule_OnUpdate(padding, padlen);
			NetLoginModule_OnUpdate((const unsigned char *)&l, sizeof(l));
			NetLoginModule_OnUpdate((const unsigned char *)&h, sizeof(h));

			return (const unsigned char *)state;
		}

		static void Digest(const unsigned char *input, unsigned int inputlen, unsigned char* strOut)
		{
			MD5Hash ctx;
			ctx.NetLoginModule_OnUpdate(input, inputlen);
			memcpy(strOut, ctx.Final(), 16);
		}

		const unsigned char* GetState() const
		{
			return (const unsigned char *)state;
		}
	};

	class HMAC_MD5Hash
	{
		unsigned char k_opad[64];
		unsigned char Output[16];
		MD5Hash md5hash;
	public:
		HMAC_MD5Hash(){ }
		void SetParameter(const unsigned char* strKey, size_t nSize)
		{
			unsigned char k_ipad[64];
			unsigned char *ipad = k_ipad;
			unsigned char *opad = k_opad;

			if (nSize > 64)
			{
				unsigned char	Output[16];

				MD5Hash::Digest(strKey, nSize, Output);

				memcpy(ipad, Output, sizeof(Output));
				memcpy(opad, Output, sizeof(Output));
				nSize = sizeof(Output);
			}
			else
			{
				memcpy(ipad, strKey, nSize);
				memcpy(opad, strKey, nSize);
			}

			for (size_t i = 0; i < nSize; i++)
			{
				ipad[i] ^= 0x36; opad[i] ^= 0x5c;
			}

			memset(ipad + nSize, 0x36, 64 - nSize);
			memset(opad + nSize, 0x5c, 64 - nSize);

			md5hash.UpdateSelf(k_ipad, 64);
		}
		void UpdateSelf(const unsigned char *input, unsigned int inputlen)
		{
			md5hash.UpdateSelf(input, inputlen);
		}
		const unsigned char * Final()
		{
			MD5Hash ctx;
			ctx.UpdateSelf(k_opad, 64);
			ctx.UpdateSelf(md5hash.Final(), 16);
			memcpy(Output, ctx.Final(), 16);
			return Output;
		}
		const unsigned char * GetResult()
		{
			return Output;
		}
		static void Partial_HMAC_Md5_init_key(const unsigned char* pKey, size_t nSize, unsigned char* ipadkey, unsigned char* opadkey)
		{
			unsigned char ipad[64];
			unsigned char opad[64];

			if (nSize > 64)
			{
				unsigned char	Output[16];

				MD5Hash::Digest(pKey, nSize, Output);

				memcpy(ipad, Output, sizeof(Output));
				memcpy(opad, Output, sizeof(Output));
				nSize = sizeof(Output);
			}
			else
			{
				memcpy(ipad, pKey, nSize);
				memcpy(opad, pKey, nSize);
			}

			for (size_t i = 0; i < nSize; i++)
			{
				ipad[i] ^= 0x36; opad[i] ^= 0x5c;
			}

			memset(ipad + nSize, 0x36, 64 - nSize);
			memset(opad + nSize, 0x5c, 64 - nSize);

			{
				MD5Hash ctx;

				ctx.UpdateSelf(ipad, sizeof(ipad));
				memcpy(ipadkey, ctx.GetState(), 16);
			}

			{
				MD5Hash ctx;

				ctx.UpdateSelf(opad, sizeof(opad));
				memcpy(opadkey, ctx.GetState(), 16);
			}
		}
	};

}// SECURITY_WIN32

#ifndef _WIN32
namespace SECURITY_LINUX
{
	class MD5Hash
	{
		FireNet::MD5Hash	hash;
		FireNet::Octets	result;
	public:
		MD5Hash() {}
		~MD5Hash() {}
	public:
		void UpdateSelf(const unsigned char* data, size_t size)
		{
			FireNet::Octets	odata(data, size);
			hash.UpdateSelf(odata);
		}
		const unsigned char * Final()
		{
			return (const unsigned char*)hash.Final(result).begin();
		}
	};

	class HMAC_MD5Hash
	{
		FireNet::HMAC_MD5Hash		hash;
		FireNet::Octets				result;
	public:
		HMAC_MD5Hash() {}
		~HMAC_MD5Hash() {}
	public:
		void SetParameter(const unsigned char* data, size_t size)
		{
			hash.SetParameter(FireNet::Octets(data, size));
		}
		void UpdateSelf(const unsigned char* data, size_t size)
		{
			FireNet::Octets	odata(data, size);
			hash.UpdateSelf(odata);
		}

		const unsigned char* Final()
		{
			return (const unsigned char*)hash.Final(result).begin();
		}
	};
}
#endif

#endif
