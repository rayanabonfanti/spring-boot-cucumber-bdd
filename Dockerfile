FROM openjdk
ADD target/desafio-serasa-experian.jar desafio-serasa-experian.jar
ENV profile=test \
    my-secret-key=my-secret-key \
    password-admin=admin
ENTRYPOINT ["java","-jar","/desafio-serasa-experian.jar"]
