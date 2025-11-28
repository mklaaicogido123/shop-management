# 🚀 Auto-Generate Flyway Migration Files

Hướng dẫn tự động tạo migration files từ JPA Entities.

## 📋 Các phương pháp

### **Phương pháp 1: Sử dụng PowerShell Script (Khuyên dùng cho Windows)**

```powershell
.\generate-migration.ps1
```

### **Phương pháp 2: Sử dụng Maven Command**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=gen -Dspring-boot.run.arguments="--spring.main.web-application-type=none"
```

Sau đó copy file `generated_schema.sql` và đổi tên theo format Flyway: `V{version}__{description}.sql`

### **Phương pháp 3: Thủ công với Hibernate**

1. Thay đổi `application.properties`:
   ```properties
   spring.jpa.hibernate.ddl-auto=update
   spring.flyway.enabled=false
   ```

2. Chạy ứng dụng để Hibernate tự động cập nhật schema

3. Dùng tool để export schema thành SQL

4. Tạo file migration mới với SQL đã export

## 🎯 Quy trình làm việc khuyên dùng

### Khi thêm/sửa Entity:

1. **Tạo/Sửa Entity class** (ví dụ: `Product.java`)

2. **Generate migration file**:
   ```powershell
   .\generate-migration.ps1
   ```

3. **Review file migration** được tạo trong `src/main/resources/db/migration/`

4. **Chỉnh sửa nếu cần** (thêm index, constraint, default values, etc.)

5. **Đổi tên file** theo mô tả rõ ràng:
   ```
   V2__add_product_table.sql
   V3__add_category_column_to_product.sql
   ```

6. **Commit** file migration vào Git

7. **Chạy ứng dụng** - Flyway sẽ tự động apply migration

## 📝 Format tên file migration

```
V{version}__{description}.sql
```

**Ví dụ:**
- `V1__init.sql` - Initial schema
- `V2__add_product_table.sql` - Thêm bảng product
- `V3__add_indexes.sql` - Thêm indexes
- `V4__alter_user_table.sql` - Sửa bảng user

**Lưu ý:**
- Version phải là số tăng dần
- Dùng **2 dấu gạch dưới** `__` giữa version và description
- Description dùng snake_case

## 🔧 Cấu hình

### File `application-gen.properties`

Profile này được dùng để generate schema:
- Tắt Flyway
- Bật Hibernate schema generation
- Export schema ra file SQL

### File `application.properties` (Production)

Profile mặc định:
- Bật Flyway
- Tắt Hibernate DDL auto
- Chỉ cho phép Flyway quản lý schema

## ⚠️ Lưu ý quan trọng

1. **KHÔNG bao giờ** sửa file migration đã chạy trên production
2. **LUÔN LUÔN** review file migration trước khi commit
3. **NÊN** test migration trên database local trước
4. **NÊN** backup database trước khi chạy migration mới
5. **KHÔNG** dùng `spring.jpa.hibernate.ddl-auto=update` trên production

## 🛠️ Tools khác (Tùy chọn)

### **Liquibase** (Alternative to Flyway)
- Hỗ trợ nhiều format (XML, YAML, JSON, SQL)
- Có thể generate diff tự động
- Phức tạp hơn Flyway

### **Flyway Teams Edition** (Paid)
- Có tính năng undo migrations
- Generate migration từ database diff
- Support cho teams lớn

### **JPA Buddy Plugin** (IntelliJ IDEA)
- Generate migration files từ Entity changes
- Visual diff tool
- Free cho personal use

## 📚 Tài liệu tham khảo

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Hibernate Schema Generation](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#schema-generation)
- [Spring Boot Database Initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)
