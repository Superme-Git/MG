local cwd = debug.getinfo(1,'S').source:sub(2,-25)

local listFile = cwd .. 'protocol_types.txt'
local logFile = cwd .. 'protocollog.txt'
local outFile = cwd .. 'protocollog_result.txt'

local allTypes = {}
local fp = io.open(listFile, 'r')
for line in fp:lines() do
	local _,_,ID,name = string.find(line, '(%d+)\t(%w+).*')
	allTypes[ID] = name
end
fp:close()

local function getName(ID)
	if allTypes[ID] then
		return allTypes[ID]
	end
	return ID
end

local fpo = io.open(outFile, 'w')
fp = io.open(logFile, 'r')
for line in fp:lines() do
	if string.find(line, '%[Send Protocol%]') then
		local _,_,ID = string.find(line, '(%d+)')
		fpo:write('[send] ', getName(ID)..'\n')
		print('[send] ', getName(ID))
	elseif string.find(line, '%[Lua Send Protocol%]') then
		local _,_,ID = string.find(line, '(%d+)')
		fpo:write('[lua send] ', getName(ID)..'\n')
		print('[lua send] ', getName(ID))
	elseif string.find(line, '%[Dispatch Protocol%]') then
		local _,_,ID = string.find(line, '(%d+)')
		fpo:write('[recv] ', getName(ID)..'\n')
		print('[recv] ', getName(ID))
	elseif string.find(line, '%[Dispatch Lua Protocol%]') then
		local _,_,ID = string.find(line, '(%d+)')
		fpo:write('[lua recv] ', getName(ID)..'\n')
		print('[lua recv] ', getName(ID))
	end
end
fp:close()
fpo:close()

print('end')
