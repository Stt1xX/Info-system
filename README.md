# Информационные системы - Лабораторная Работа №1

## Навигация по проекту
* [Задание](Task.md)
* [Бэк-Энд](backend/src/main/java/com/example/backend)
* [Фронт-Энд](frontend/src)
* [Uml диаграмма](images/UmlDiagram.png)

## Описание

Проект представляет собой информационную систему, которая позволяет хранить информацию о [людях](images/HumansTable.png "HumansTable") их [автомобилях](images/CarsTable.png "CarsTable") и координатах.
В системе имеются end-points для [добавления](images/AddHumanForm[PreviewPage].png), удаления, редактирования и просмотра данных. Реализован функционал [aудита](images/AuditWindow.png "AuditWindow") и [администрирования](images/AdminRequests.png "AdminRequests").
Также сортировка, фильтрация и поиск по данным и [дополнительный функционал](images/AdditionalFunctionality.png "AdditionalFunctionality") по заданию.

## Backend

* Написан на *Java*, использует *Spring Boot*.
* Сборка и управление зависимостей - *Gradle*.
* В качестве базы данных используется *PostgreSQL*. Для работы с базой данных используется *Spring Data JPA* и *Hibernate*.
* Для регистрации и авторизации используется *Spring Security* (через логин и пароль). Для хранения паролей используется *SHA256*.
* Аутентификация происходит через *JSESSIONID*. Имеются роли - *ADMIN* и *USER*.
* Для управления коллекциями объектов и остальной логики используется *Spring MVC*.
* Для поддержания актуальности данных используются *Spring Message Broker* и *STOMP* (WebSockets).
* *CSRF-tokens* для защиты от CSRF-атак.

## Frontend

* Написан на *Vue*.
* Для лаконичного дизайна использовал *TailWindCss* - фреймворк для CSS.
* Для ajax запросов использовал *Jquery*.