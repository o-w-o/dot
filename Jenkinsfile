pipeline {
  agent any

  environment {
    BUILD_TARGET = 'target'
    DOCKER_REGISTRY_NAMESPACE = 'registry.cn-beijing.aliyuncs.com/o-w-o'
    TAGGED_DOCKER_IMG = "${env.DOCKER_REGISTRY_NAMESPACE}/api:latest"
  }

  parameters {
    booleanParam(name: 'ENABLE_DEBUG', defaultValue: true, description: '开启 DEBUG 模式')
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
          JAR_FILE = "${env.JENKINS_HOME}/jobs/${env.JOB_NAME}/branches/${env.BRANCH_NAME}/builds/${env.BUILD_NO}/${env.BUILD_TARGET}/${JAR_FILENAME}.jar"

          TAGGED_DOCKER_IMG = "${env.DOCKER_REGISTRY_NAMESPACE}/api:${IMG_BUILD_TAG}"
        }
      }
    }
    stage('Prepare:Result') {
      steps {
        println "【 IMG_BUILD_TAG 】-> ${IMG_BUILD_TAG}"
        println "【 JAR_FILENAME 】-> ${JAR_FILENAME}"
        println "【 JAR_FILE 】-> ${JAR_FILE}"
        println "【 TAGGED_DOCKER_IMG 】-> ${TAGGED_DOCKER_IMG}"

        println "env.JENKINS_HOME -> ${env.JENKINS_HOME}"
        println "env.WORKSPACE -> ${env.WORKSPACE}"
        println "env.JOB_NAME -> ${env.JOB_NAME}"
        println "env.JOB_BASE_NAME -> ${env.JOB_BASE_NAME}"
        println "env.BRANCH_NAME -> ${env.BRANCH_NAME}"
        println "env.BUILD_NUMBER -> ${env.BUILD_NUMBER}"
        println "env.BUILD_ID -> ${env.BUILD_ID}"
        println "env.EXECUTOR_NUMBER -> ${env.EXECUTOR_NUMBER}"
        println "env.NODE_NAME -> ${env.NODE_NAME}"
        println "env.NODE_LABELS -> ${env.NODE_LABELS}"
        println "pipeline -> ${pipeline}"
        println "currentBuild.number -> ${currentBuild.number}"
        println "currentBuild.fullProjectName -> ${currentBuild.fullProjectName}"
        println "currentBuild.absoluteUrl -> ${currentBuild.absoluteUrl}"
        println "currentBuild.description -> ${currentBuild.description}"
        println "currentBuild.displayName -> ${currentBuild.displayName}"
        println "currentBuild.result -> ${currentBuild.result}"
      }
    }
    stage('Prepare:Debug') {
      when {
        expression { return params.ENABLE_DEBUG }
      }
      steps {
        input("构建版本号为【 ${IMG_BUILD_TAG} 】, 镜像为【 ${TAGGED_DOCKER_IMG} 】确定吗？")
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
          sh "docker build --build-arg JAR_FILE=${JAR_FILE} --tag ${TAGGED_DOCKER_IMG} ."

          echo "4.3 发布 Image"
          sh "docker push ${TAGGED_DOCKER_IMG}"
        }
      }
    }
    stage('Deploy') {
      steps {
        echo "5. Deploy Stage"
        input "确认要部署线上环境吗？"

        withCredentials([sshUserPrivateKey(credentialsId: 'sshKey', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'username')]) {
          script {
            def remote = [:]
            remote.name = "o-w-o"
            remote.host = "o-w-o.ink"
            remote.allowAnyHosts = true
            remote.user = username
            remote.identityFile = identity


            try {
              sshCommand remote: remote, command: "docker stop api"
              sshCommand remote: remote, command: "docker rm api"
            } catch (e) {
              ERROR_MESSAGE = e.message
              echo "部署预处理异常 -> ${e.message}"
            } finally {
              echo "${TAGGED_DOCKER_IMG}"
            }

            warnError('部署预处理出现异常') {
              input("部署预处理出现异常，确认继续执行 【${TAGGED_DOCKER_IMG}】 部署行为？")
            }

            try {
              sshCommand remote: remote, command: "docker run -i --rm --net=host --name=api -e JAVA_OPTS='-Xms128m -Xmx256m' ${TAGGED_DOCKER_IMG}"
            } catch (e) {
              ERROR_MESSAGE = e.message
              echo "部署异常 -> ${e.message}"
            } finally {
              echo "部署检测"
              sshCommand remote: remote, command: "docker ps"
            }

          }
        }
      }
    }
  }
}
