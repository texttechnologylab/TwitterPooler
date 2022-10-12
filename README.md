<p align="center">
  <img src="TwitterPooler.png" width="500">
</p>

## Description
Within the scope of a bachelor thesis at the Institute of Computer Science and Mathematics at Goethe University, a system called "TwitterPooler" for the management of Twitter data was developed at the professorship for Computational Humanities / Text Technology. The application is able to perform NLP preprocessing as well as pooling on downloaded tweets. This repository contains the source code of the bachelor thesis as well as adaptions that are performed on top of it. 

You can find an running demonstration here: http://twitter-pooler.texttechnologylab.org/home

# Framework

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related Guides

- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

# Docker
We swapped out the application into a Docker container for an easier use. A Mongo DB instance is required to use the TwitterPooler. This can be set up by yourself or simply started by using docker-compose.

# Cite
If you want to use the Project, please quote this as follows:

# BibTeX
```

@unpublished{Siwiecki:2022,
  author    = {Siwiecki, Grzegorz},
  title     = {TwitterPooler: Ein System für das Management, Pooling und die NLP-Vorverarbeitung von Twitter-Daten},
  institution = {Institute of Computer Science and Mathematics, Text Technology Lab},
  address = {Frankfurt, Germany},
  type = {bachelor's thesis},
  year      = {2022},
  url       = {https://github.com/texttechnologylab/TwitterPooler/blob/main/Bachelorarbeit_Grzegorz_Siwiecki.pdf}
}

```
