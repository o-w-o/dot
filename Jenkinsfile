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

        echo '1.1 检验 mvn 版本'
        sh 'mvn -v'

        echo '1.2 获取 项目 pom.xml 中的应用信息'
        script {
          IMG_BUILD_TAG = sh(returnStdout: true, script:"mvn -q -N -Dexec.executable='echo' -Dexec.args='\${projects.version}' exec:exec").trim().toLowerCase()
          JAR_FILENAME  = sh(returnStdout: true, script:"mvn -q -N -Dexec.executable='echo' -Dexec.args='\${project.build.finalName}' exec:exec").trim()
          JAR_PATH      = "target/${JAR_FILENAME}.jar"

          println IMG_BUILD_TAG
          println JAR_FILENAME
          println JAR_PATH
        }

        input('构建版本号为【' + IMG_BUILD_TAG + '】, 确定吗？')
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

        echo '3.1 使用 mvn 打包'
        sh 'mvn clean package -Dmaven.test.skip=true'
      }
    }
    stage('Push') {
      steps {
        echo "4.Push Docker Image Stage"

        withCredentials([usernamePassword(credentialsId: 'aliDockerRegistry', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            echo "4.1 登录 Docker"
            sh "docker login -u ${dockerHubUser} -p ${dockerHubPassword} registry.cn-beijing.aliyuncs.com"

            echo "4.2 构建 Image"
            sh "docker build --build-arg JAR_PATH=${JAR_PATH} --tag registry.cn-beijing.aliyuncs.com/o-w-o/api:${IMG_BUILD_TAG} ."

            echo "4.3 发布 Image"
            sh "docker push registry.cn-beijing.aliyuncs.com/o-w-o/api:${IMG_BUILD_TAG}"
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
