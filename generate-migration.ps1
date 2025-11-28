# PowerShell script để generate migration file từ JPA entities

Write-Host "🚀 Generating migration SQL from JPA entities..." -ForegroundColor Green

# Tạo timestamp cho version
$VERSION = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds()
$DESCRIPTION = "auto_generated"
$MIGRATION_FILE = "src\main\resources\db\migration\V${VERSION}__${DESCRIPTION}.sql"

# Chạy Spring Boot với profile gen để generate schema
Write-Host "📝 Running Hibernate schema generation..." -ForegroundColor Yellow
mvn spring-boot:run "-Dspring-boot.run.profiles=gen" "-Dspring-boot.run.arguments=--spring.main.web-application-type=none"

# Đợi file được tạo
Start-Sleep -Seconds 2

# Kiểm tra file generated
$GENERATED_FILE = "src\main\resources\db\migration\generated_schema.sql"
if (Test-Path $GENERATED_FILE) {
    # Copy và đổi tên theo format Flyway
    Copy-Item $GENERATED_FILE $MIGRATION_FILE
    Write-Host "✅ Migration file created: $MIGRATION_FILE" -ForegroundColor Green
    
    # Xóa file temp
    Remove-Item $GENERATED_FILE
    
    Write-Host "✨ Done! You can now review and edit the migration file." -ForegroundColor Cyan
} else {
    Write-Host "❌ Failed to generate schema file" -ForegroundColor Red
    exit 1
}
