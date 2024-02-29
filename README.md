# ATM Uygulaması
* Uygulama ayağa kaldırılmadan önce PostgreSQL veritabanında atm_db adında bir veri tabanı oluşturulmalıdır.
* Uygulama basit bir atm uygulamasıdır.
* Kullanıcı sisteme istenilen bilgiler ile kayıt olur.
* Eposta ve şifresi ile giriş yapar ve geriye bir token döner.
* Kullanıcı bu token ile bir daha sisteme giriş yapmaya gerek kalmadan, sisteme 24 saat boyunca istek atabilir.
* Süreyi service->JwtService altında generateToken metodunu altında setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) istenilen miktara göre değiştirilebilir.
* Şimdilik tokena gerek kalmadan istek atılabiliniyor. Bu SecurityConfig sınıfından güncellenebilir.
* (/auth/**) ksımları değiştirilmemeli, sisteme kayıt veya giriş yaparken bir tokena ihtiyaç duyulmamalı.
* Para çekme, yatırma ve transfer işlemleri gerçekleştirilebilir.
* Kullanıcı istediği kadar yeni hesap oluşturabilir, mevcut hesabını silebilir, tüm hesap bilgilerini görebilir.
* Kullanıcı yaptığı son işlemleri belirttiği adet kadar görüntüleyebilir.

## Kullanılan Teknolojiler
* Java 17
* Maven
* Spring Boot (Version : 3.1.5)
* Spring Security 6
* JWT (Json Web Token)
* Postqresql
* Dependencies
==> Spring Web
--  Spring Boot DevTools
--  Lombok
--  Spring Data JPA
--  Validation
--  Model Mapper
--  Swagger

## Run
- Project > Run As> Maven Build  ``spring-boot:run``

## Account Table


id  | balance | is_active | user_id |
---------|----------|--------------|----------|
1     | 0.00     | false  | 2   |
2     | 10000.00 | true | 2 | 
3     | 20000.00 | true  | 1 | 
4     | 100.00     | true | 3 | 
5     | 55.00     | true  | 4 | 

## User Table


id  | firstname | lastname | password | role |
---------|----------|--------------|----------|----------|
1     | Şahin    | Candemir  | encoded   | ADMIN|
2     | Veli | Meşe | encoded | USER|

## Transaction Table


id  | amount | description | transaction_date | transaction_type |account_id|
---------|----------|--------------|----------|----------|-------|
1     | 100.00    | User with ID: 1withdrew 100.00TL from their account  | 2024-02-28 06:39:24.233264   | WITHDRAW|1

## Swagger
* Dokümantasyon için aşağıdaki adres kullanılmalıdır.
* ``http://localhost:8080/swagger-ui/index.html`` 
