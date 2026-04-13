#!/bin/bash
# CAS 集成测试运行脚本

set -e

echo "====================================="
echo "CAS 统一身份认证集成测试"
echo "====================================="
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查命令是否存在
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# 运行后端测试
run_backend_tests() {
    echo -e "${YELLOW}运行后端集成测试...${NC}"
    
    if ! command_exists ./mvnw; then
        echo -e "${RED}错误: 未找到 mvnw 命令${NC}"
        exit 1
    fi
    
    ./mvnw test -Dtest=CasAuthIntegrationTest,CasFrontendIntegrationTest,CasServiceTest \
        -Dspring.profiles.active=test
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ 后端测试通过${NC}"
    else
        echo -e "${RED}✗ 后端测试失败${NC}"
        exit 1
    fi
    echo ""
}

# 运行前端测试
run_frontend_tests() {
    echo -e "${YELLOW}运行前端单元测试...${NC}"
    
    if [ ! -d "frontend" ]; then
        echo -e "${RED}错误: 未找到 frontend 目录${NC}"
        exit 1
    fi
    
    cd frontend
    
    if ! command_exists npm; then
        echo -e "${RED}错误: 未找到 npm 命令${NC}"
        exit 1
    fi
    
    # 检查 node_modules 是否存在
    if [ ! -d "node_modules" ]; then
        echo "安装依赖..."
        npm install
    fi
    
    npm run test
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ 前端测试通过${NC}"
    else
        echo -e "${RED}✗ 前端测试失败${NC}"
        exit 1
    fi
    
    cd ..
    echo ""
}

# 主函数
main() {
    # 解析参数
    RUN_BACKEND=true
    RUN_FRONTEND=true
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --backend-only)
                RUN_FRONTEND=false
                shift
                ;;
            --frontend-only)
                RUN_BACKEND=false
                shift
                ;;
            --help)
                echo "用法: $0 [选项]"
                echo ""
                echo "选项:"
                echo "  --backend-only   只运行后端测试"
                echo "  --frontend-only  只运行前端测试"
                echo "  --help           显示帮助"
                exit 0
                ;;
            *)
                echo "未知选项: $1"
                exit 1
                ;;
        esac
    done
    
    # 运行测试
    if [ "$RUN_BACKEND" = true ]; then
        run_backend_tests
    fi
    
    if [ "$RUN_FRONTEND" = true ]; then
        run_frontend_tests
    fi
    
    echo ""
    echo "====================================="
    echo -e "${GREEN}所有测试通过！${NC}"
    echo "====================================="
}

# 运行主函数
main "$@"
