#!/bin/sh
# 更改历史log的author

git filter-branch --env-filter '

an="$GIT_AUTHOR_NAME"
am="$GIT_AUTHOR_EMAIL"
cn="$GIT_COMMITTER_NAME"
cm="$GIT_COMMITTER_EMAIL"

if [ "$GIT_COMMITTER_EMAIL" = "wf@ascend-tech.com.cn>" ]
then
    cn="wangfengye"
    cm="104044135@qq.com"
fi
if [ "$GIT_AUTHOR_EMAIL" = "wf@ascend-tech.com.cn>" ]
then
    an="wangfengye"
    am="104044135@qq.com"
fi

export GIT_AUTHOR_NAME="$an"
export GIT_AUTHOR_EMAIL="$am"
export GIT_COMMITTER_NAME="$cn"
export GIT_COMMITTER_EMAIL="$cm"
'