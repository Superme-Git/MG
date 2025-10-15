
local cwd = debug.getinfo(1,'S').source:sub(2,-24)

dofile(cwd .. '../res/script/utils/bit.lua')


--define path
local defPath = cwd .. '../../FireClient/Application/protocols/'
local listFile = cwd .. 'protocolxmlfiles.txt'
os.execute('dir \"'..defPath..'\" /b> '..listFile)

local gnetPath = cwd .. '../../../common/cauthc/gnet.xml'

local outFile = cwd .. 'protocol_types.txt'

--start
print('start ...')
local typeNames = {}
typeNames[0] = 'AuthError'

local allIDs = {}
table.insert(allIDs, 0)

local function calculateType(t)
	local n = bit.blshift(12, 16)
	return bit.bor(n, t)
end

local function parseFile(f, convert)
	local fp = io.open(f, 'r')
	local commentBegin = false
	for line in fp:lines() do
		local p = string.find(line, '<!%-%-')
		if p then
			commentBegin = true
		end
		
		if not commentBegin then
			local name,t,tolua = string.match(line, '.*<protocol.*name=\"([a-zA-Z0-9]+)\".*type=\"(%d+)\".*tolua=\"(%d+)\".*')
			if not name then
				name,t = string.match(line, '.*<protocol.*name=\"([a-zA-Z0-9]+)\".*type=\"(%d+)\".*')
			end
			
			if name then
				t = tonumber(t)
				if convert then
					t = calculateType(t)
				end
				
				if typeNames[t] then
					print('[ERROR] protocol type repeat: ', t, ' ', typeNames[t], ' ', name)
				else
					if tolua then
						name = name .. ' (tolua=' .. tolua .. ')'
					end
					typeNames[t] = name
					table.insert(allIDs, t)
				end
			end
		end
		
		if commentBegin then
			local m,n = string.find(line, '%-%->')
			if m and m==n then
				print(line,m)
			end
			p = string.find(line, '%-%->')
			if p then
				commentBegin = false
			end
		end
	end
	fp:close()
end

local fp = io.open(listFile, 'r')
for f in fp:lines() do
	if string.find(f, '.*%.xml') then
		parseFile(defPath..f, true)
	end
end
fp:close()

parseFile(gnetPath, false)

for i=1, #allIDs-1 do
	for j=i+1, #allIDs do
		if allIDs[i] > allIDs[j] then
			local tmp = allIDs[i]
			allIDs[i] = allIDs[j]
			allIDs[j] = tmp
		end
	end
end

fp = io.open(outFile, 'w')
for k,v in pairs(allIDs) do
	fp:write(v .. '\t' .. typeNames[v] .. '\n')
end
fp:close()

print('--- all end ---')
