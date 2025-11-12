# 🚀 Build do Finora - Guia PowerShell

## ✅ Gradle Wrapper Configurado

O projeto agora possui o **Gradle Wrapper 8.9** completamente configurado e funcional.

---

## 📋 Pré-requisitos

- **JDK 17** instalado
- **Android SDK** (pode ser instalado via Android Studio)
- **PowerShell** (já disponível no Windows)

---

## 🛠️ Comandos de Build

### Build Debug (Desenvolvimento)

```powershell
cd c:\Users\josh\Desktop\finora
.\gradlew assembleDebug
```

**APK gerado em**: `app\build\outputs\apk\debug\app-debug.apk` (~60MB)

---

### Build Release (Produção)

```powershell
cd c:\Users\josh\Desktop\finora
.\gradlew assembleRelease
```

**APK gerado em**: `app\build\outputs\apk\release\app-release.apk` (~45MB com ProGuard)

---

### Build Completo (Debug + Release)

```powershell
cd c:\Users\josh\Desktop\finora
.\gradlew clean assembleDebug assembleRelease
```

---

### Limpar Build

```powershell
.\gradlew clean
```

---

### Atualizar Dependências

```powershell
.\gradlew clean build --refresh-dependencies
```

---

### Verificar Tarefas Disponíveis

```powershell
.\gradlew tasks
```

---

## 📱 Instalar no Device

### Via Gradle (Device conectado via USB com Debug habilitado)

```powershell
.\gradlew installDebug
```

### Via ADB Diretamente

```powershell
adb install app\build\outputs\apk\debug\app-debug.apk
```

---

## 🧪 Testes

### Rodar Testes Unitários

```powershell
.\gradlew test
```

### Rodar Testes Instrumentados (Device/Emulator necessário)

```powershell
.\gradlew connectedAndroidTest
```

---

## 🐛 Troubleshooting

### Erro: "Could not find or load main class org.gradle.wrapper.GradleWrapperMain"

**Solução**: O wrapper foi regenerado. Certifique-se de que existe `gradle\wrapper\gradle-wrapper.jar`.

Se ainda falhar, regenere com:

```powershell
# Se tiver Gradle instalado
gradle wrapper --gradle-version 8.9 --distribution-type all
```

---

### Erro: "SDK location not found"

**Solução**: Crie/edite `local.properties` na raiz do projeto:

```properties
sdk.dir=C\:\\Users\\SEU_USUARIO\\AppData\\Local\\Android\\Sdk
```

Ou defina a variável de ambiente:

```powershell
$env:ANDROID_SDK_ROOT = "C:\Users\SEU_USUARIO\AppData\Local\Android\Sdk"
```

---

### Build Muito Lento

**Solução**: Habilite daemon do Gradle (já configurado em `gradle.properties`):

```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
```

---

### Erro de Memória no Gradle

**Solução**: Aumentar heap em `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

---

## 📊 Informações do Build

| Configuração | Valor |
|--------------|-------|
| Gradle | 8.9 |
| Android Gradle Plugin | 8.5.2 |
| Kotlin | 2.0.21 |
| JDK | 17 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 (Android 15) |
| Compile SDK | 35 |

---

## 🔧 Correções Aplicadas

### 1. ✅ Gradle Wrapper Gerado
- `gradle-wrapper.jar` baixado via Gradle 8.9 temporário
- `gradle-wrapper.properties` configurado para distribuição `all`
- Scripts `gradlew` e `gradlew.bat` funcionais

### 2. ✅ Erro de Variância em `Result.kt`
- Removido `out` do tipo genérico `E` que causava conflito no `flatMap`
- Adicionado `@UnsafeVariance` para permitir uso em posições contra variantes

### 3. ✅ Dependência `domain` Faltando em `ui-theme`
- Adicionado `implementation(project(":domain"))` em `ui-theme/build.gradle.kts`
- Corrigido "Unresolved reference: ExpenseCategory"

### 4. ✅ FlowUseCase com `suspend` Incorreto
- Removido `suspend` de `FlowUseCase.execute()` pois `Flow` já é assíncrono
- Atualizado `GetAllExpensesUseCase` e relacionados

### 5. ✅ Dependência do Coroutines Play Services
- Adicionado `kotlinx-coroutines-play-services:1.8.1` em `data/build.gradle.kts`
- Corrigido erro `Unresolved reference: await` no ML Kit

### 6. ✅ Smart Cast Error em `ExpenseListScreen.kt`
- Mudado `expense.merchant` para `expense.merchant ?: ""` na linha 242

### 7. ✅ Backup Rules Inválidos
- Corrigido `backup_rules.xml` e `data_extraction_rules.xml`
- Substituído `<exclude>` por `<include>` para database

### 8. ✅ Warning do BuildConfig Deprecado
- Removido `android.defaults.buildfeatures.buildconfig=true` de `gradle.properties`

### 9. ✅ Warning do compileSdk=35
- Adicionado `android.suppressUnsupportedCompileSdk=35` em `gradle.properties`

---

## ✅ Status Final

```
BUILD SUCCESSFUL in 9s
506 actionable tasks: 229 executed, 213 from cache, 64 up-to-date
```

**APKs Gerados:**
- ✅ `app-debug.apk` - 63.5 MB
- ✅ `app-release.apk` - 45.1 MB (minificado com R8)

---

## 📚 Referências

- [Gradle Wrapper Documentation](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
- [Android Gradle Plugin](https://developer.android.com/build/releases/gradle-plugin)
- [Kotlin Gradle Plugin](https://kotlinlang.org/docs/gradle.html)

---

<div align="center">
  <h3>🎉 Projeto Finora - 100% Compilável!</h3>
  <p>Wrapper configurado • Erros corrigidos • APKs gerados com sucesso</p>
</div>
