#pragma once

class CGlobalError
{
	std::wstring	m_errorfile;
	int				m_error;

public:

	static CGlobalError& GetInstance()
	{
		static CGlobalError s_instance;
		return s_instance;
	}

	void SetError( int error, const std::wstring& errorfile)
	{
		m_error = error;
		m_errorfile = errorfile;
	}

	int GetError()
	{
		return m_error;
	}

	const std::wstring& GetErrorFile()
	{
		return m_errorfile;
	}
};