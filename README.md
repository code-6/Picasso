# "Picasso" is
CRM for tourist agency NoviNomad that manages tours, drivers and guides relations. Main functionality is to prevent attach same drivers\guides to same tours and prevent time intersection between different tours. For example if one guide was attached to the tour with period from 2022-01-01 to 2022-01-10 then system will not allow attach same guide to the next tour, that has date interaction from 2022-01-05 to 2022-01-15 (intersection period 2022-01-05 to 2022-01-10) because one person may take a part only in one tour at the same time.

# ERM
TODO: add ERM image

# Stack
- Java 17
- MySQL DB v. 8.0.29 (production)
- H2 DB (dev)
- Spring Boot v. 2.7.0
- Gradle v. 7.4.1
- Embedded Tomcat web server

# Mandatory environment variables
| Name               | Description                                              |
|--------------------|----------------------------------------------------------|
| PCS_SPRING_PROFILE | **dev** for local  or **prod** for production server.    |
| PCS_DB_PASSWORD    | password for DB user. Required only for production mode. |

# Start application
- Production: Create required environment variables. Build executable jar and run it. Application will be started as OS service in embedded tomcat.
- Local: Create required environment variables in run config. Press debug/run button in IntelliJ IDE