#!/bin/bash
# CAS 证书排查脚本

echo "=========================================="
echo "CAS 证书导入检查"
echo "=========================================="
echo ""

# 1. 检查宿主机证书目录
echo "1. 宿主机证书目录检查:"
ls -la ./certs/ 2>/dev/null || echo "   ✗ certs 目录不存在"
echo ""

# 2. 检查容器状态
echo "2. 容器状态:"
docker ps | grep innovation_backend || echo "   ✗ 容器未运行"
echo ""

# 3. 检查容器内证书
echo "3. 容器内证书文件:"
docker exec innovation_backend ls -la /app/certs/ 2>/dev/null || echo "   ✗ 容器内无 certs 目录"
echo ""

# 4. 检查密钥库
echo "4. Java 密钥库中的证书:"
docker exec innovation_backend keytool -list -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit 2>/dev/null | grep -E "(cas_|Alias|Entry)" || echo "   未找到 CAS 相关证书"
echo ""

# 5. 测试连接
echo "5. 测试 CAS 服务器连接:"
docker exec innovation_backend wget --timeout=5 -O- https://ids.wisedu.com.cn:9086/authserver/login 2>&1 | head -3 || echo "   连接失败（可能是证书问题）"
echo ""

echo "=========================================="
echo "排查建议:"
echo "- 如果第1步失败: 运行 ./fetch-cas-cert.sh 获取证书"
echo "- 如果第3步失败: 检查 docker-compose.yml 的 volumes 配置"
echo "- 如果第4步失败: 查看容器日志 docker logs innovation_backend"
echo "- 如果第5步失败: 确认 CAS 地址和端口是否正确"
echo "=========================================="
