# AuthGuard API 🔐 — Projeto 3/9 | Portfólio Java Backend

API REST educacional de **cadastro, autenticação JWT e autorização por perfis** com Java 21, Spring Boot 3.5.7, Spring Security, PostgreSQL e Flyway.

> Projeto de portfólio. Não utilizar diretamente em produção. Nunca publique chaves JWT ou senhas de produção.

## Funcionalidades
- Cadastro público com perfil USER fixo
- Login por e-mail/senha protegida com BCrypt
- Emissão de JWT assinado com HS256 e expiração de 30 minutos
- Proteção de rotas e controle de acesso USER/ADMIN
- PostgreSQL, Spring Data JPA, Flyway, validação de entrada
- Testes JUnit 5 e Mockito, GitHub Actions

## Executar localmente
Requisitos: JDK 21+, Maven 3.9+, Docker Compose.

1. `docker compose up -d`
2. Gere uma chave Base64: `openssl rand -base64 32`
3. Configure `JWT_SECRET_BASE64` no ambiente do terminal ou em Run Configurations > Environment no Eclipse. **Não a coloque no Git**.
4. `mvn clean test`
5. `mvn spring-boot:run`

API em `http://localhost:8080`, PostgreSQL na porta local 5434. Credenciais do docker-compose são **apenas para desenvolvimento**. Suporta `DB_URL`, `DB_USER`, `DB_PASSWORD`.

## Endpoints
| Método | Rota | Acesso |
| --- | --- | --- |
| POST | `/api/v1/auth/register` | Público |
| POST | `/api/v1/auth/login` | Público |
| GET | `/api/v1/users/me` | JWT |
| GET | `/api/v1/admin/users` | JWT ADMIN |
| GET | `/actuator/health` | Público |

### Exemplo de cadastro
```bash
curl -X POST http://localhost:8080/api/v1/auth/register -H 'Content-Type: application/json' -d '{"name":"Ana Silva","email":"ana@example.com","password":"senha-super-segura-2026"}'
```

### Exemplo de login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' -d '{"email":"ana@example.com","password":"senha-super-segura-2026"}'
```

### Perfil
```bash
curl http://localhost:8080/api/v1/users/me -H 'Authorization: Bearer SEU_ACCESS_TOKEN'
```

O cadastro não aceita a atribuição de ADMIN pelo cliente. Para testar apenas localmente, um responsável pode promover uma conta no banco de desenvolvimento e fazer login novamente:
```sql
UPDATE app_users SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

## Estrutura
```text
src/main/java/dev/portfolio/authguard/
  AuthGuardApplication.java
  admin/   # Controlador administrativo
  auth/    # Cadastro, login, DTOs e serviço
  common/  # Tratamento de erros
  security/# JWT e configuração de segurança
  user/    # Usuários e persistência
src/main/resources/db/migration/ # Flyway
src/test/java/ # Testes JUnit
.github/workflows/ci.yml
```

## Limitações de segurança
Projeto educacional, sem refresh token, revogação, MFA, rate limiting, confirmação de e-mail, recuperação de senha ou integração automatizada com PostgreSQL. Tokens contêm roles do instante da emissão; alterações só valem para novos tokens. Use TLS em ambientes reais. Provisionamento de administrador exigiria um fluxo seguro específico. O workflow testa unidades, não a integração com banco.

## Pontos para entrevista
Autenticação versus autorização, BCrypt, JWT, claims, HTTP 401/403, RBAC, Spring Security, `@Transactional`, DTOs, persistência e limites de JWT stateless.

## Licença
MIT — LICENSE.
