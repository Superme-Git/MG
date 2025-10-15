#!/bin/sh

# snail/test project. see autorun.bat
cd ../test; ant xgen test; cd ../testdir

# snail/test/src/btest/TestCheckpoint sample
cd ../test; sh tc 5000 "-p 4000000 -b 50"; cd ../testdir


# test xtransform
cd xtransform && call ant -f build-transform.xml test && cd ..