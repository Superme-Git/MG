
#pragma once

namespace gnet{

	enum
	{
		ALGORITHM_MASK_HIGH  	 =  0x000F0000,
		ALGORITHM_MASK_OPT   	 =  0x0000FF00,
		ALGORITHM_OPT_NOCACHE	 =  0x00000100,
		
		ALGORITHM_OPT_TICKETS	 =  0x00001000,
		ALGORITHM_OPT_EASY_PAY 	 =  0x00003000,//support easy pay
		
		ALGORITHM_NONE       	 =  0x0,
		ALGORITHM_CARD     	 =  0x00010000,
		ALGORITHM_HANDSET   	 =  0x00020000,
		ALGORITHM_USBKEY    	 =  0x00030000,
		ALGORITHM_PHONE       	 =  0x00040100,
		ALGORITHM_USBKEY2   	 =  0x00050000,
		ALGORITHM_PHONETOKEN     =  0x00060000,

		ALGORITHM_PASSWD_OBSOLETE	=  0x00000200,//remind change password
		ALGORITHM_CARD_OBSOLETE		=  0x00000200,//remind change matrixcard
		ALGORITHM_GM_ACCOUNT  		=  0x00000800,//is GM
		
	};

	enum
	{
		FBD_FORBID_LOGIN	= 100,	//forbid login
		FBD_FORBID_TALK		= 101,	//
		FBD_FORBID_TRADE	= 102,	//
		FBD_FORBID_SELL		= 103,	//
		FBD_FORBID_SELLPOINT	= 104,	//
	};

	enum
	{
	   	SHENDIAO_AID		= 26,
	   	PUKE_AID		= 30,
	};
	
	enum
	{
		NORMAL_LOGIN		= 0,
		ELEC_LOGIN		= 1,
		CROSS_LOGIN		= 2,
		AUANY_LOGIN		= 3,
		SSO_LOGIN		= 4,
	};
/*
	const static int	NORMAL_LOGIN = 0;
	const static int	ELEC_LOGIN = 1;
	const static int	CROSS_LOGIN = 2;
*/
}
