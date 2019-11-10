pipeline {
  agent any

  environment {
    DOCKER_REGISTRY_NAMESPACE = 'registry.cn-beijing.aliyuncs.com/o-w-o'
  }

  parameters {
    string(name: 'TAGGED_DOCKER_IMG', defaultValue: "${env.DOCKER_REGISTRY_NAMESPACE}/api:latest", description: '已经 build 并 push 成功的项目镜像名')
  }

  stages {
    stage('Prepare') {
      agent {
        docker {
          image "maven:3-jdk-11-slim"
          args "-u root -v /var/mvn/repos:/root/.m2"
        }
      }
      steps {
        echo "1 Prepare Stage"

        echo "1.1 检验 mvn 版本"
        sh "mvn -v"

        echo '1.2 获取 项目 pom.xml 中的应用信息'
        script {
          IMG_BUILD_TAG = sh(returnStdout: true, script: "mvn -q -N -Dexec.executable='echo' -Dexec.args='\${projects.version}' exec:exec").trim().toLowerCase()
          JAR_FILENAME = sh(returnStdout: true, script: "mvn -q -N -Dexec.executable='echo' -Dexec.args='\${project.build.finalName}' exec:exec").trim()
          JAR_PATH = "target/${JAR_FILENAME}.jar"

          println "【 IMG_BUILD_TAG 】-> ${IMG_BUILD_TAG}"
          println "【 JAR_FILENAME 】-> ${JAR_FILENAME}"
          println "【 JAR_PATH 】-> ${JAR_PATH}"
        }

        input('构建版本号为【' + IMG_BUILD_TAG + '】, 确定吗？')

        script {
          params.TAGGED_DOCKER_IMG = "${env.DOCKER_REGISTRY_NAMESPACE}/api:${IMG_BUILD_TAG}"
        }
      }
    }
    stage('Build') {
      agent {
        docker {
          image "maven:3-jdk-11-slim"
          args "-u root -v /var/mvn/repos:/root/.m2"
        }
      }
      steps {
        echo "2 Build Docker Image Stage"

        echo '2.1 使用 mvn 打包'
        sh "mvn clean package -Dmaven.test.skip=true"

        echo '2.2 保存打包后的 jar 文件到制品库'
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      }
    }
    stage('Test') {
      steps {
        echo '3.Test Stage'
      }
    }
    stage('Push') {
      steps {
        echo "4.Push Docker Image Stage"

        withCredentials([usernamePassword(credentialsId: 'aliDockerRegistry', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
          echo "4.1 登录 Docker"
          sh "docker login -u ${dockerHubUser} -p ${dockerHubPassword} registry.cn-beijing.aliyuncs.com"

          echo "4.2 构建 Image"
          sh "docker build --build-arg JAR_PATH=${JAR_PATH} --tag ${params.TAGGED_DOCKER_IMG} ."

          echo "4.3 发布 Image"
          sh "docker push ${params.TAGGED_DOCKER_IMG}"
        }
      }
    }
    stage('Deploy') {
      steps {
        echo "5. Deploy Stage"
        input "确认要部署线上环境吗？"

        withCredentials([usernamePassword(credentialsId: 'sshKey', passwordVariable: 'username', usernameVariable: 'password')]) {

          script {
            def remote = [:]
            remote.name = "o-w-o"
            remote.host = "o-w-o.ink"
            remote.allowAnyHosts = true
            remote.user = username
            remote.password = password


            try {
              sshCommand remote: remote, command: "docker stop api"
              sshCommand remote: remote, command: "docker rm api"
            } catch (e) {
              echo "部署预处理异常 -> ${e.message}"
            }

            sshCommand remote: remote, command: "docker run -it --rm --net=host --name=api -e JAVA_OPTS='-Xms128m -Xmx256m' ${params.TAGGED_DOCKER_IMG}"
          }
        }
      }
    }
  }
}
