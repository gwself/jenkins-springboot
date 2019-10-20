# jenkins-springboot
spring-boot 项目 jenkins+ansible 自动化部署

1. jenkins只通过shell来启动jar包 shell 脚本如下：
> 

#!/bin/bash

#环境参数
env=$1
#端口号
serverPort=$2
#项目名称
projectName=$3 


#复制项目包的目标路径
destAbsPath=/home/bigdata/${projectName}/${env}

#$WORKSPACE 是jenkins 的一个变量
sourFile=${WORKSPACE}/target/${projectName}*.jar
destFile=${destAbsPath}/${projectName}.jar
properties=--spring.profiles.active=${env}

#定义常量
logFile=${projectName}.log
dstLogFile=${destAbsPath}/${logFile}
whatToFind="Started "
msgLogFileCreated="$logFile created"
msgBuffer="Buffering: "
msgAppStarted="Application Started... exiting buffer!"

function stopServer(){
    echo " "
    echo "Stoping process on port: $serverPort"
    # 需要安装 yum install psmisc  来使用fusre命令
    fuser -n tcp -k ${serverPort} > redirection &
    echo " "
}

function deleteFiles(){
    echo "Deleting $destFile"
    rm -rf ${destFile}

    echo "Deleting $dstLogFile"
    rm -rf ${dstLogFile}
    
    echo " "
}

function copyFiles(){
    echo "Copying files from $sourFile"
    cp ${sourFile} ${destFile}

    echo " "
}

function run(){

   nohup nice java -jar ${destFile} --server.port=${serverPort} ${properties} $ > ${dstLogFile} 2>&1 &

   echo "COMMAND: nohup nice java -jar $destFile --server.port=$serverPort $properties $> $dstLogFile 2>&1 &"

   echo " "
}
function changeFilePermission(){

    echo "Changing File Permission: chmod 777 $destFile"

    chmod 777 ${destFile}

    echo " "
}   

function watch(){
 
    tail -f ${dstLogFile} |

        while IFS= read line
            do
                echo "$msgBuffer" "$line"

                if [[ "$line" == *"$whatToFind"* ]]; then
                    echo ${msgAppStarted}
                    pkill tail
                    exit
                fi
        done
}


#BUILD_ID=dontKillMe /path/to/this/file/api-deploy.sh dev 8080 jenkins-spring-boot

#1 - 停止服务
stopServer

#2 - 删除文件夹
deleteFiles

#3 - 复制文件
copyFiles

changeFilePermission
#4 - 改变权限
run

#5 - 加载完成
watch
