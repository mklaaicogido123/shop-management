#!/bin/bash
# Script để generate migration file từ JPA entities

echo "🚀 Generating migration SQL from JPA entities..."

# Tạo timestamp cho version
VERSION=$(date +%s)
DESCRIPTION="auto_generated"
MIGRATION_FILE="src/main/resources/db/migration/V${VERSION}__${DESCRIPTION}.sql"

# Chạy Spring Boot với profile gen để generate schema
echo "📝 Running Hibernate schema generation..."
mvn spring-boot:run -Dspring-boot.run.profiles=gen -Dspring-boot.run.arguments="--spring.main.web-application-type=none"

# Đợi file được tạo
sleep 2

# Kiểm tra file generated
if [ -f "src/main/resources/db/migration/generated_schema.sql" ]; then
    # Copy và đổi tên theo format Flyway
    cp "src/main/resources/db/migration/generated_schema.sql" "$MIGRATION_FILE"
    echo "✅ Migration file created: $MIGRATION_FILE"
    
    # Xóa file temp
    rm "src/main/resources/db/migration/generated_schema.sql"
    
    echo "✨ Done! You can now review and edit the migration file."
else
    echo "❌ Failed to generate schema file"
    exit 1
fi
