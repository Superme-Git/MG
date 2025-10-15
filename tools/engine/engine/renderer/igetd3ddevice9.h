namespace Nuclear
{
	class IGetD3DDevice9
	{
	public:
		virtual IDirect3DDevice9* GetDevice() const = 0;
	};
}