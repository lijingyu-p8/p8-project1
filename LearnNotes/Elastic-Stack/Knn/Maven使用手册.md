# build的plugins插件

## 一、spring-boot-maven-plugin

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <!-- 指定该Main Class为全局的唯一入口 -->
        <mainClass>com.example.demo.DemoApplication</mainClass>
    </configuration>
    <executions>
        <execution>
            <goals>
                <!--可以把依赖的包都打包到生成的Jar包中-->
                <!--执行package时实际执行的是mvn package spring-boot:repackage-->
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

repackage：这个是默认 goal，在 mvn package 执行之后，这个命令再次打包生成可执行的 jar，同时将 mvn package 生成的 jar 重命名为 *.original。其中original文件中只包含class文件，不包含依赖的第三方jar包。

## 二、Resource文件拷贝

```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
        <!--				<includes>-->
        <!--					<include>**/*.xml</include>-->
        <!--				</includes>-->
        <!--使用include配置包含项、exclude配置排除项，下面配置可拷贝所有非java文件-->
        <excludes>
            <exclude>**/*.java</exclude>
        </excludes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>**/*</include>
        </includes>
    </resource>
</resources>
```

默认package打包只会将resource目录下的文件拷贝到类路径下。但是当使用mybatis等第三方组件时，在java目录下会存在诸如mapper.xml等非.java后缀的文件，此类文件打包时也需要编译到类路径下，所以需要使用resources进行文件拷贝的设置。

注意：当设置了resource时，除了配置目标路径，原有的resource会失效，所以也需要进行配置。

## 三、distributionManagement

```xml
<distributionManagement>
    <snapshotRepository><!--pom文件中进行配置，将快照发布到仓库中-->
        <id>nexus-mine</id>
        <url>http://localhost:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
    <repository>
        <id>nexus-mine</id><!--pom文件中进行配置，将release版本发布到仓库中-->
        <url>http://localhost:8081/repository/maven-releases/</url>
    </repository>
</distributionManagement>
```

## 四、setting.xml文件配置仓库地址

```xml
<mirror>
	<id>nexus-mine</id>
		<mirrorOf>central</mirrorOf>
		<name>Nexus mine</name>
	<url>http://localhost:8081/repository/maven-public/</url>
</mirror>
<!--如果有用户验证，需要配置service-->
<server>
    <id>releases</id>
    <username>admin</username>
    <password>adminnexus</password>
</server>
```

mirrorOf：代表过滤拦截条件，central含义为覆盖默认的中央仓库，配置的*号的含义为所有的依赖下载都要走当前的url

​                    如果配置了多组的mirror，当mirrorOf相同时，按照从上到下的顺序只会使用第一个镜像进行依赖下载，当mirrorOf不同         时，按照从上到下的顺序进行请求拦截，当第一个镜像加载不到jar包时，依次使用下面的镜像，所以尽量不要使用*号，会拦截所有请求，可以对最后一个镜像设置。

```xml
<profile>
    <id>dev</id> 
        <activation>    
             <activeByDefault>true</activeByDefault>    
             <jdk>1.8</jdk>    
         </activation>    
         <properties>    
         <maven.compiler.source>1.8</maven.compiler.source>  
         <maven.compiler.target>1.8</maven.compiler.target>   
         <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>   

         </properties>   -->
    <repositories>
        <repository> <!--仓库id，repositories可以配置多个仓库，保证id不重复 -->
            <id>snapshots</id> <!--仓库地址，即nexus仓库组的地址 -->
            <url>http://localhost:8081/repository/maven-snapshots/</url>
        </repository>
        <repository> <!--仓库id，repositories可以配置多个仓库，保证id不重复 -->
            <id>releases</id> <!--仓库地址，即nexus仓库组的地址 -->
            <url>http://localhost:8081/repository/maven-releases/</url>
        </repository>
    </repositories>
</profile>
<!--设置激活哪个profile-->
<activeProfiles>
    <activeProfile>dev</activeProfile>
</activeProfiles>
```

如果配置了repository，则以repository优先，并且如果repository中的仓库需要密码验证，同样需要server