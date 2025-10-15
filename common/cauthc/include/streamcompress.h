#ifndef __FIRENET_STREAMCOMPRESS_H__
#define __FIRENET_STREAMCOMPRESS_H__

namespace FireNet
{
	class Compress
	{
		enum { CTRL_OFF_EOB = 0, MPPC_HIST_LEN = 8192 };
		unsigned char history[MPPC_HIST_LEN];
		unsigned char *hash[256];
		unsigned char *histptr;
		unsigned int  legacy_in;

		void putbits(unsigned char *&buf, unsigned int val, unsigned int n, unsigned int& l);
		void putlit(unsigned char *&buf, unsigned int c, unsigned int& l);
		void putoff(unsigned char *&buf, unsigned int off, unsigned int& l);
		unsigned char* compress_block( unsigned char *obuf, size_t isize );

	public:
		Compress();
		Compress(const Compress& rhs);
		Octets& UpdateSelf(Octets &in);
		Octets& Final(Octets &in);
	};

	class Decompress
	{
		enum { CTRL_OFF_EOB = 0, MPPC_HIST_LEN = 8192 };
		unsigned char history[MPPC_HIST_LEN];
		unsigned char *histptr;
		unsigned int l, adjust_l;
		unsigned int blen, blen_total;
		unsigned char *rptr, *adjust_rptr;
		Octets   legacy_in;

		bool passbits(const unsigned int n);
		unsigned int fetch();
		static void lamecopy(unsigned char *dst, unsigned char *src, unsigned int len);
	public:
		Decompress();
		Decompress(const Decompress& rhs);

		Octets& UpdateSelf(Octets &in);
	};

}// namespace FireNet {

#endif
