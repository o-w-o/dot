pipeline {
  agent any

  stages {
    stage('Prepare') {
      agent {
        docker {
          image 'maven:3-jdk-11-slim'
          args '-u root -v /var/mvn/repos:/root/.m2'
        }
      }
      steps {
        echo "1 Prepare Stage"

        echo '1.0 检验 mvn 版本'
        sh 'mvn -v'

        echo '1.1 获取 项目 pom.xml 中的版本号'
        script {
          API_BUILD_TAG = sh(returnStdout: true, script:"mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | sed -n -e '/^\\[.*\\]/ !{ /^[0-9]/ { p; q } }'").trim()
          println API_BUILD_TAG
        }

        input('构建版本号为【' + API_BUILD_TAG + '】, 确定吗？')
      }
    }
    stage('Test') {
      steps {
        echo "2.Test Stage"
      }
    }
    stage('Build') {
      agent {
        docker {
          image 'maven:3-jdk-11-slim'
          args '-u root -v /var/mvn/repos:/root/.m2'
        }
      }
      steps {
        echo "3 Build Docker Image Stage"

        echo '3.0 使用 mvn 打包'
        sh 'mvn clean package -Dmaven.test.skip=true'

        echo '3.1 使用 mvn 构建 docker 镜像'
        sh 'mvn dockerfile:build -Dmaven.test.skip=true'
      }
    }
    stage('Push') {
      steps {
        echo "4.Push Docker Image Stage"
        withCredentials([usernamePassword(credentialsId: 'aliDockerRegistry', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            sh "docker login -u ${dockerHubUser} -p ${dockerHubPassword}"
            sh "docker push registry.cn-beijing.aliyuncs.com/o-w-o/api:${buildTag}"
        }
      }
    }
    stage('Deploy') {
      steps {
        echo "5. Deploy Stage"
        input "确认要部署线上环境吗？"
      }
    }
  }
}
