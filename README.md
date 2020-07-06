# Cases-manager
Desktop app for manage cases

## Description

Java swing desktop app designed for manage cases connected with acceptance criteria by extension good quality of working routine, 
hierarchy of employees in the companyand and monitoring work progress.
The idea arose during reading scrum guide and encountering organisational problems during work.
GUI provides only main process with manage tasks. Rest funnctionality is backend implemented only.

### Graphs UML

[Documentation docx link](https://drive.google.com/file/d/1OPryWaWMrB99qSU1OnQqwwb-OCDrv935/view?usp=sharing)

## Requirements

For development, you will need Java 1.8+ for manage maven project.

## Database

Database is h2 embedded configured in hibernate.cfg.xml

#### Set up h2 driver and path with database source files:

```
 <!-- Database connection settings -->
    <property name="connection.driver_class">org.h2.Driver</property>
    <property name="connection.url">jdbc:h2:E:/Pliki/GitHub/Cases-manager/data/db</property>
```

#### Uncomment only before firsy app run to create database:

```
<!--Drop and re-create the database schema on startup-->
    <!--<property name="hbm2ddl.auto">create</property>-->
```

#### And add h2 dependency in pom.xml:

```
<!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>1.4.200</version>
    </dependency>
```

## Visualisation
Screenshots of crucial funnctionality:
- [Screenshot link 1](https://ibb.co/Nj5VSnC)
- [Screenshot link 2](https://ibb.co/f0x9pz5)
- [Screenshot link 3](https://ibb.co/3Bs4KWm)
- [Screenshot link 4](https://ibb.co/XzGJsWx)
- [Screenshot link 5](https://ibb.co/8P0pk2L)
- [Screenshot link 6](https://ibb.co/nfh5xWH)
- [Screenshot link 7](https://ibb.co/MhxSyzs)
- [Screenshot link 8](https://ibb.co/j82MQw3)
