#pragma once

namespace PP
{
	void WINAPI Check(int64_t roleid, int pptype, int seq, int key);
	void WINAPI DumpResult( GNET::Marshal::OctetsStream & os );
	void WINAPI PPService(int srvid, void *buf, int bufsz, int key);

	//void WINAPI DumpResult();

	/*
	//����ֵΪ0����ɹ����������������
	//10 : ��������  11 : �ڴ治��
	int WINAPI ReadProMemory(int pid, void* readaddr, void* buffer, int size, int* realreadsize);
	int WINAPI WriteProMemory(int pid, void* writeaddr, void* buffer, int size, int* realwritesize);
	int WINAPI QuerySystemModule(void* buffer, int size);
	int WINAPI QueryAllProcessInfo(void* buffer, int size);
	int WINAPI QuerySingleProcessInfo(int pid, void* buffer, int size);

	int WINAPI GetServiceAddr(const char* service_name, int* outaddr);
	int WINAPI QueryProcessModule(int pid, void* outbuffer, int bufsize);
	*/




}

