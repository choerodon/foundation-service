# foundation-service

`Foundation Service` is the core service of Choerodon.  

The service is the basic service of Choerodon, which currently provides the setting of custom fields and custom pages

## Features
- **Object Scheme Management**
- **Object Scheme Field Management**
- **Page Management**
- **Page Field Management**
- **Field Value Management**

## Requirements
- Java8
- [Iam Service](https://github.com/choerodon/iam-service.git)
- [MySQL](https://www.mysql.com)

## Installation and Getting Started
1. init database

    ```sql
    CREATE USER 'choerodon'@'%' IDENTIFIED BY "choerodon";
    CREATE DATABASE foundation_service DEFAULT CHARACTER SET utf8;
    GRANT ALL PRIVILEGES ON foundation_service.* TO choerodon@'%';
    FLUSH PRIVILEGES;
    ```
1. run command `sh init-local-database.sh`
1. run command as follow or run `FoundationServiceApplication` in IntelliJ IDEA

    ```bash
    mvn clean spring-boot:run
    ```

## Dependencies
- `go-register-server`: Register server
- `iam-service`：iam service
- `mysql`: agile_service database
- `api-gateway`: api gateway server
- `gateway-helper`: gateway helper server
- `oauth-server`: oauth server
- `manager-service`: manager service
- `asgard-service`: asgard service
- `notify-service`: notify service

## Reporting Issues
If you find any shortcomings or bugs, please describe them in the  [issue](https://github.com/choerodon/choerodon/issues/new?template=issue_template.md).

## How to Contribute
Pull requests are welcome! [Follow](https://github.com/choerodon/choerodon/blob/master/CONTRIBUTING.md) to know for more information on how to contribute.
