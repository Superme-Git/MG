del ..\MT_G2\client\bin\* /Q/S/F/A:-H
xcopy .\client\resource\bin\*.*  ..\MT_G2\client\bin\ /Y /S /R
del ..\MT_G2\client\res\script\*.lua /Q/S/F
xcopy .\client\resource\res\script\*.lua  ..\MT_G2\client\res\script\ /Y /S /R
xcopy .\client\resource\res\*.*  ..\MT_G2\client\res\ /Y /R
del ..\MT_G2\client\res\table\bintable\*.bin /Q/S/F
xcopy .\client\resource\res\table\bintable\*.bin  ..\MT_G2\client\res\table\bintable\ /Y /S /R
del ..\MT_G2\client\res\table\xmltable\*.xml /Q/S/F
xcopy .\client\resource\res\table\xmltable\*.xml  ..\MT_G2\client\res\table\xmltable\ /Y /S /R

del ..\MT_G2\server\gs\gamedata\xml\*.xml /Q/S/F
xcopy .\server\serverbin\gs\gamedata\xml\*.xml  ..\MT_G2\server\gs\gamedata\xml\ /Y /S /R
xcopy .\server\serverbin\gs\*.*  ..\MT_G2\server\gs\ /Y /R
