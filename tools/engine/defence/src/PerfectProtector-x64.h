#pragma  once

bool Is64BitSystem();
DWORD _InsertCurrentPid_x64();
DWORD _DelCurrentPid_x64();

DWORD DoIntCall_x64(DWORD dwCmd);
DWORD DoIntCallValue_x64(DWORD dwCmd,DWORD dwValue1);
DWORD DoIntCall3Value_x64(DWORD dwCmd,DWORD dwValue1,DWORD dwValue2,DWORD dwValue3);
DWORD _CheckAlive_Client_x64(DWORD dwServerKey,DWORD dwRoleId,unsigned char *lpOutBuf);
DWORD GACD_Service_x64(DWORD dwSid,DWORD *lpBuf,DWORD dwSize,DWORD dwKey);