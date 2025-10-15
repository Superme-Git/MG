
#pragma once


namespace gnet { 

class GRoleInventory : public GNET::Rpc::Data
{
public:
	int id;
	int pos;
	int count;
	int max_count;
	char container_id;
	GNET::Octets data;
	int guid1;
	int guid2;
	int mask;
	int proctype;
	int reserved;

	GRoleInventory() {
		id = 0;
		pos = -1;
		count = 0;
		max_count = 0;
		container_id = 0;
		guid1 = 0;
		guid2 = 0;
		mask = 0;
		proctype = 0;
		reserved = 0;
	}

	GRoleInventory(int _id_, int _pos_, int _count_, int _max_count_, char _container_id_, const GNET::Octets & _data_, int _guid1_, int _guid2_, int _mask_, int _proctype_, int _reserved_)
		: id(_id_), pos(_pos_), count(_count_), max_count(_max_count_), container_id(_container_id_), data(_data_), guid1(_guid1_), guid2(_guid2_), mask(_mask_), proctype(_proctype_), reserved(_reserved_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << id;
		_os_ << pos;
		_os_ << count;
		_os_ << max_count;
		_os_ << container_id;
		_os_ << data;
		_os_ << guid1;
		_os_ << guid2;
		_os_ << mask;
		_os_ << proctype;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		_os_ >> pos;
		_os_ >> count;
		_os_ >> max_count;
		_os_ >> container_id;
		_os_ >> data;
		_os_ >> guid1;
		_os_ >> guid2;
		_os_ >> mask;
		_os_ >> proctype;
		_os_ >> reserved;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GRoleInventory(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GRoleInventory *o = dynamic_cast<const GRoleInventory*>(&d)) return *this = *o;
		return *this;
	}

	GRoleInventory& operator=(const GRoleInventory & d)
	{
		if (&d == this) return *this;
		id = d.id;
		pos = d.pos;
		count = d.count;
		max_count = d.max_count;
		container_id = d.container_id;
		data = d.data;
		guid1 = d.guid1;
		guid2 = d.guid2;
		mask = d.mask;
		proctype = d.proctype;
		reserved = d.reserved;
		return *this;
	}

};

} 
