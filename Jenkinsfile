node {
  checkout scm

  def aliDockerRegistry = 'registry.cn-beijing.aliyuncs.com/o-w-o'
  def aliDockerVpcRegistry = 'registry-vpc.cn-beijing.aliyuncs.com/o-w-o'
  def aliDockerInnerRegistry = 'registry-internal.cn-beijing.aliyuncs.com/o-w-o'
  def ioStore = [:]

  stage('Prepare') {
    docker.image('maven:3-jdk-11-slim').inside("-u root -v /var/mvn/repos:/root/.m2") {
      echo "1 Prepare Stage"

      echo "1.1 检验 mvn 版本"
      sh "mvn -v"

      echo '1.2 获取 项目 pom.xml 中的应用信息'

      ioStore.buideDir = 'target'
      ioStore.paramsJarFileName = sh(returnStdout: true, script: "mvn -q -N -Dexec.executable='echo' -Dexec.args='\${project.build.finalName}' exec:exec").trim()

      ioStore.dockerTag = sh(returnStdout: true, script: "mvn -q -N -Dexec.executable='echo' -Dexec.args='\${projects.version}' exec:exec").trim().toLowerCase()
      ioStore.dockerArgsJarFile = "${ioStore.buideDir}/${ioStore.paramsJarFileName}.jar"

      ioStore.dockerImageName = "${aliDockerRegistry}/api"
      ioStore.dockerVpcImageName = "${aliDockerVpcRegistry}/api"
      ioStore.dockerImageNameWithTag = "${ioStore.dockerImageName}:${ioStore.dockerTag}"

    }
  }

  stage('Prepare:Result') {
    println "【 ioStore.dockerTag 】-> ${ioStore.dockerTag}"
    println "【 ioStore.paramsJarFileName 】-> ${ioStore.paramsJarFileName}"
    println "【 ioStore.dockerArgsJarFile 】-> ${ioStore.dockerArgsJarFile}"
    println "【 ioStore.dockerImageNameWithTag 】-> ${ioStore.dockerImageNameWithTag}"
  }

  stage('Prepare:Debug') {
    if (params.ENABLE_DEBUG) {
      input("构建版本号为【 ${ioStore.dockerTag} 】, 镜像为【 ${ioStore.dockerImageNameWithTag} 】确定吗？")
    }
  }

  stage('Build') {
    docker.image('maven:3-jdk-11-slim').inside("-u root -v /var/mvn/repos:/root/.m2") {
      echo "2 Build Docker Image Stage"

      echo '2.1 使用 mvn 打包'
      sh "mvn clean package -Dmaven.test.skip=true"

      echo "2.2 保存 打包后的 jar 文件以备后续使用"
      stash(name: "${ioStore.paramsJarFileName}", includes: "**/target/*.jar")

      /**
       * 2.3 归档文件
       * archiveArtifacts artifacts: '**\/ target/*.jar', fingerprint: true
       */
    }
  }

  stage('Test') {
    echo '3.Test Stage'
  }

  stage('Push') {
    echo "4.Push Docker Image Stage"

    docker.withRegistry("https://${aliDockerVpcRegistry}", 'aliDockerRegistry') {
      echo "4.1 获取 Jar"
      unstash("${ioStore.paramsJarFileName}")

      echo "4.2 预检 Workspace"
      sh "ls -al"

      if (params.ENABLE_DEBUG) {
        input("是否继续进行下一步？")
      }

      echo "4.3 构建 Image"
      ioStore.dockerImage = docker.build(ioStore.dockerVpcImageName, "--build-arg JAR_FILE=${ioStore.dockerArgsJarFile} .")

      echo "4.4 发布 Image"
      ioStore.dockerImage.push("${ioStore.dockerTag}")
    }
  }
  stage('Deploy') {
    echo "5. Deploy Stage"

    withCredentials([sshUserPrivateKey(credentialsId: 'sshKey', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'username')]) {
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
        echo "部署预处理异常 -> ${e.message}"
        input("部署预处理出现异常，确认继续执行 【${ioStore.dockerImageNameWithTag}】 部署行为？")
      } finally {
        echo "${ioStore.dockerImageNameWithTag}"
      }

      try {
        sshCommand remote: remote, command: "docker pull ${ioStore.dockerImageNameWithTag}"
        sshCommand remote: remote, command: "docker run -i -d --net=host --name=api -e JAVA_OPTS='-Xms128m -Xmx256m' ${ioStore.dockerImageNameWithTag}"
      } catch (e) {
        echo "部署异常 -> ${e.message}"
      } finally {
        echo "部署检测"
        sshCommand remote: remote, command: "docker ps"
      }
    }
  }
}
