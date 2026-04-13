#!/bin/sh
# ==========================================
# Docker 容器启动脚本
# 1. 导入 CAS 证书（如果存在）
# 2. 启动应用
# ==========================================

set -e

echo "=========================================="
echo "Docker 启动脚本"
echo "=========================================="

# Java 密钥库路径
KEYSTORE_PATH="$JAVA_HOME/lib/security/cacerts"
if [ ! -f "$KEYSTORE_PATH" ]; then
    KEYSTORE_PATH="/opt/java/openjdk/lib/security/cacerts"
fi

echo "Java 密钥库路径: $KEYSTORE_PATH"
echo "Java 版本:"
java -version 2>&1 | head -1

# 导入证书函数
import_certs() {
    local cert_dir="/app/certs"
    
    if [ -d "$cert_dir" ]; then
        echo ""
        echo "发现证书目录: $cert_dir"
        echo "证书文件列表:"
        ls -la "$cert_dir"/
        
        echo ""
        echo "开始导入证书..."
        local import_count=0
        for cert in "$cert_dir"/*.crt "$cert_dir"/*.pem; do
            if [ -f "$cert" ]; then
                alias=$(basename "$cert" | sed 's/[^a-zA-Z0-9]/_/g')
                echo "  导入: $(basename "$cert") -> alias: $alias"
                if keytool -import -alias "$alias" -keystore "$KEYSTORE_PATH" -file "$cert" -storepass changeit -noprompt 2>/dev/null; then
                    echo "  ✓ 导入成功"
                    import_count=$((import_count + 1))
                else
                    # 检查是否已存在
                    if keytool -list -keystore "$KEYSTORE_PATH" -storepass changeit | grep -q "$alias"; then
                        echo "  ℹ 证书已存在，跳过"
                    else
                        echo "  ✗ 导入失败"
                    fi
                fi
            fi
        done
        echo ""
        echo "证书导入完成，成功导入 $import_count 个证书"
        
        # 显示当前密钥库中的所有证书别名
        echo ""
        echo "当前密钥库中的证书别名列表:"
        keytool -list -keystore "$KEYSTORE_PATH" -storepass changeit | grep -E "^[a-zA-Z0-9]" | head -20
    else
        echo ""
        echo "未找到证书目录: $cert_dir，跳过证书导入"
    fi
}

# 执行证书导入
import_certs

echo ""
echo "=========================================="
echo "启动应用..."
echo "=========================================="
exec java -jar -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod app.jar
