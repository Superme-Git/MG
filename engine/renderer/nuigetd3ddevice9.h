namespace Nuclear
{
	class IGetD3DDevice9
	{
	public:
		virtual void* GetDevice() const = 0;
	};
}