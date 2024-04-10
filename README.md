
# HR-RESTful-SOAP api 🚪
![GitHub commit activity](https://img.shields.io/github/commit-activity/t/Hakugami/REST-SOAP-HR-Management-API) 
![Github Created At](https://img.shields.io/github/created-at/Hakugami/REST-SOAP-HR-Management-API)
![GitHub last commit](https://img.shields.io/github/last-commit/Hakugami/REST-SOAP-HR-Management-API) 
![GitHub repo size](https://img.shields.io/github/repo-size/Hakugami/REST-SOAP-HR-Management-API)

## Project Description 📝
An API that helps the HR do (his/her) job faster and more efficient



# Main Functionalities
This API addresses mainly HR who would be able to see all the employee-related activities

## Documentation
RESTful
https://documenter.getpostman.com/view/33538335/2sA3BgBw6x

Soap
https://documenter.getpostman.com/view/33538335/2sA3BgBvVn

## Main Features

- **HATEOAS** 🔍
- **XML and JSON Support** 📦
- **Sign In with JWT authentication and authorization** 🔐
- **Partial Responses and pagination for Resources** 👤
- **Request vacations and view all vacation requests with the ability to deny or approve** 🛍️
- **Mark attendance as an employee with automatic deductions, HR(Any privileged user) can view all the data** 🖥



# Technologies ⚙️

![Java JDK](https://img.shields.io/badge/Java%20JDK-21-blue?style=for-the-badge&logo=java)&nbsp;&nbsp;&nbsp;&nbsp;
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue?style=for-the-badge&logo=eclipse&logoColor=white)&nbsp;&nbsp;&nbsp;&nbsp;
![JPA Hibernate](https://img.shields.io/badge/JPA%20Hibernate-blue?style=for-the-badge&logo=hibernate)&nbsp;&nbsp;&nbsp;&nbsp;
![Hikari CP](https://img.shields.io/badge/Hikari%20CP-blue?style=for-the-badge&logo=java&logoColor=white)&nbsp;&nbsp;&nbsp;&nbsp;
![Mapstruct](https://img.shields.io/badge/Mapstruct-blue?style=for-the-badge&logo=java&logoColor=white)&nbsp;&nbsp;&nbsp;&nbsp;
![JSON](https://img.shields.io/badge/JSON-blue?style=for-the-badge&logo=json)&nbsp;&nbsp;&nbsp;&nbsp;
![XML](https://img.shields.io/badge/XML-blue?style=for-the-badge&logo=XML)&nbsp;&nbsp;&nbsp;&nbsp;
![JAX-RS](https://img.shields.io/badge/JAX-RS-blue?style=for-the-badge&logo=JAX-RS)&nbsp;&nbsp;&nbsp;&nbsp;
![JAX-WS](https://img.shields.io/badge/JAX-WS-blue?style=for-the-badge&logo=JAX-WS)&nbsp;&nbsp;&nbsp;&nbsp;
![Maven](https://img.shields.io/badge/Maven-blue?style=for-the-badge&logo=apache)

# Architecture


# Database Schema
![image](https://github.com/Hakugami/REST-SOAP-HR-Management-API/assets/62517820/58829ed2-fc7f-457a-85c2-bcf47596515e)


# How to Run 🏃‍♂️
## Prerequisites 🛠️

- JDK 21
- Tomcat 10
- MySQL & Workbench
- IntelliJ IDEA
- Maven

## Steps 📝

1. **Install the Dependencies, Compile and run:**
    - Open your terminal or command prompt.
    - Navigate to the root directory of the project.
    - Execute the following command:
      ```
      mvn clean install
      ```
       ```
      mvn clean tomcat7:deploy
      ```
      
  2. **Hibernate should build the tables if everything is setup correctly**









