--------------------------------------------------------------
--Variables
local _imagesets = {}
local _missImages = {}
local _relatedFiles = {}

local _missfiles = {}
local _layoutCodeFiles = {}
local _missImageCount = 0
local _relatedFileCount = 0


local pwd = debug.getinfo(1, 'S').source
pwd = string.match(pwd, "@(.*)\check_imageset.lua")
pwd = string.gsub(pwd, "\\", "/")
print('current path: ' .. pwd)


--------------------------------------------------------------
--all imageset list
print('read all imageset list')
local imagesetPath = pwd .. "../res/ui/imagesets/"
local outputFile = pwd .. "check_imageset_output.txt"
os.execute('dir \"' .. imagesetPath .. '\" /b> ' .. outputFile)
local fp = io.open(outputFile, 'r')
for line in fp:lines() do
	local imageset = string.match(line, "(.*).imageset")
	if imageset then
		imageset = string.lower(imageset)
		_imagesets[imageset] = 1
	end
end
fp:close()

--------------------------------------------------------------
--search cpp/lua/layout/looknfeel/scheme files
local listfile = pwd .. "check_imageset_files.txt"


local fp = io.open(listfile, 'r')
--read file list
for filepath in fp:lines() do
	local p, file = string.match(filepath, "(.*)/(.*)")
	p = p .. '/'
	if file ~= "LuaFireClient.cpp" and file ~= "LuaFireClientWin32.cpp" then
		print(file)
		--read file
		local fp1 = io.open(p .. file, 'r')
		if fp1 then
			file = string.lower(file)
			--read line
			local isScheme = string.find(file, ".*%.scheme")
			local isCpp = string.find(file, ".*%.cpp")
			local isLua = string.find(file, ".*%.lua")
			
			for line in fp1:lines() do
				local imageset = nil
				if isScheme then
					imageset = string.match(line, "([%w_]+).imageset.*")
					
				else
					imageset = string.match(line, ".*\"set:([%w_]+).*")
				end
				
				if imageset then
					imageset = string.lower(imageset)
				end
				
				if imageset and not _imagesets[imageset] then
					if not _missImages[imageset] then
						_missImages[imageset] = true
						_missImageCount = _missImageCount + 1
					end
					if not _relatedFiles[p .. file] then
						_relatedFiles[p .. file] = { images={}, file=file }
						_relatedFileCount = _relatedFileCount + 1
					end
					if not _relatedFiles[p .. file].images[imageset] then
						_relatedFiles[p .. file].images[imageset] = true
					end
					
				end
				
				--find layout
				if isCpp or isLua then
					local layout = string.match(line, ".*\"([%w_]+).layout.*")
					if layout then
						layout = string.lower(layout)
						layout = layout .. ".layout"
						if not _layoutCodeFiles[layout] then
							_layoutCodeFiles[layout] = file
						else
							_layoutCodeFiles[layout] = _layoutCodeFiles[layout] .. '/' .. file
						end
					end
				end
			end
			fp1:close()
		end
	end
end
fp:close()


--------------------------------------------------------------
--output miss files
print('output miss files')
local fp = io.open(outputFile, 'w')
fp:write('missing imageset count: ' .. _missImageCount .. '\n')
fp:write('related  files   count: ' .. _relatedFileCount .. '\n\n\n')

fp:write("------------------ MISSING IMAGESETS ------------------\n")
for image in pairs(_missImages) do
	fp:write(image .. '.imageset\n')
end

fp:write("\n\n------------------ RELATED FILES ------------------\n")
for k,v in pairs(_relatedFiles) do
	fp:write(v.file .. '\n')
	fp:write('\t\timage:')
	for k in pairs(v.images) do
		fp:write(' ' .. k)
	end
	fp:write('\n')
	if _layoutCodeFiles[v.file] then
		fp:write('\t\tcode: ' .. _layoutCodeFiles[v.file] .. '\n\n')
	else
		fp:write('\n')
	end
end
fp:write('\n')

fp:close()

print('All End')
