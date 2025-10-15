1.在RELEASE中找到make_words.bat
2.修改路径为你想要的路径，增加你想要的文件类型
3.运行make_words.bat,生成words.txt文件
4.拷贝这文件到../debug下
5.在words.txt中依次修改标识符
6.如 aaa 修改成 aaa bbb中间用一个空格
7.修改几个以后，运行change_words.bat
8.编译自己的模块
9.如果正常，就继续替换，如果有问题，找到编译出错的名字，取消那一行的修改，如 aaa bbb 修改回 aaa
10.依次类推，把所有的都改完。
11.每次运行完change_words.bat正常后，需要REVERT change_words.bat修改过的文件，一般就直接REVERT整个目录就行了，添加好新的单词以后，再运行
12.我早晨忙活了很长时间，也没解决为什么MAKE_WORDS.BAT要在RELEASE下运行，CHANGE_WORDS.BAT要在DEBUG下运行的问题，怕耽搁大家时间，先提交了。谁有能力可以帮我看看。
