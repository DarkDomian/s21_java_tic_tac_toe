# Tic-Tac-Toe: Разбор технологий и подходов

Проект: RESTful API для игры в крестики-нолики с компьютером на Spring Boot.

---

## 1. Web-приложения

**Web-приложение** — программа, которая работает на сервере и взаимодействует с клиентом через протокол HTTP. Клиентом может быть браузер, мобильное приложение, Postman или curl.

### Как выглядит запрос (from curl to Java)

```
curl -X POST http://localhost:8080/game/550e8400-e29b-41d4-a716-446655440000
  -H "Content-Type: application/json"
  -d '{"board":[[0,0,0],[0,1,0],[0,0,0]]}'
```

Этот запрос проходит путь:
1. **Клиент** (curl) отправляет HTTP POST на сервер
2. **Сервер** (Tomcat, встроенный в Spring Boot) принимает соединение
3. **DispatcherServlet** определяет, какой контроллер обработает запрос
4. **Контроллер** вызывает сервис, тот — репозиторий
5. **Ответ** идёт обратно тем же путём

### Ключевая идея

Клиент и сервер — слабо связаны. Они общаются через HTTP — единственный контракт. Клиенту не важно, на чём написан сервер (Java, Python, Go). Серверу не важно, какой клиент к нему обращается.

---

## 2. Spring Framework

Spring — это фреймворк для создания Java-приложений. Его главные задачи:

- **Управление зависимостями** (Dependency Injection) — объекты не создают друг друга, их создаёт и связывает контейнер
- **Упрощение инфраструктуры** — вместо написания тонны boilerplate кода, вы описываете, что должно произойти, а Spring делает это
- **Интеграция** — базы данных, веб, безопасность, очереди — всё подключается декларативно

### Dependency Injection / IoC (Inversion of Control)

Базовый принцип Spring: **объект не ищет свои зависимости — зависимости в него впрыскиваются**.

#### Без DI

```java
public class GameServiceImpl implements GameService {
    private Repository repo;

    public GameServiceImpl() {
        this.repo = new RepositoryImpl(); // ← жёсткая связь
    }
}
```

Проблема: `GameServiceImpl` привязан к конкретной реализации `RepositoryImpl`. Заменить на другую (например, для тестов) — нельзя.

#### С DI

```java
public class GameServiceImpl implements GameService {
    private final Repository repo;

    public GameServiceImpl(Repository repo) { // ← зависимость приходит извне
        this.repo = repo;
    }
}
```

Кто создаёт и передаёт `repo`? Контейнер Spring.

### ApplicationContext и бины

**ApplicationContext** — контейнер, который управляет всеми объектами (бинами). Каждый бин — это объект, жизненным циклом которого управляет Spring.

Бины создаются через:

#### 1. Аннотации (`@Component`, `@RestController`, `@Service`, `@Repository`)

```java
@RestController  // ← Spring создаст бин этого класса
public class GameController {
    // ...
}
```

`@SpringBootApplication` включает **component scanning** — Spring сканирует пакет и находит все классы с `@Component` и её производными.

#### 2. Java Config (`@Configuration` + `@Bean`)

```java
@Configuration
public class AppConfig {

    @Bean
    public Repository RepositoryImpl() {
        return new RepositoryImpl();
    }

    @Bean
    GameService GameServiceImpl(Repository repo) {
        return new GameServiceImpl(repo);
    }
}
```

Здесь вручную описано, какие бины создать и как их связать.

### Какие аннотации используются в проекте

| Аннотация | Где | Зачем |
|-----------|-----|-------|
| `@SpringBootApplication` | `TictactoeApplication.java` | Включает авто-конфигурацию, component scan, и дополнительные настройки Spring Boot |
| `@RestController` | `GameController.java` | Говорит Spring: этот класс — веб-контроллер, обрабатывающий HTTP-запросы. Ответы автоматически конвертируются в JSON |
| `@PostMapping("/game/{uuid}")` | `GameController.java` | Маппит POST-запросы по пути `/game/{uuid}` на метод `move()`. `{uuid}` — шаблон, подставится любое значение |
| `@PathVariable UUID uuid` | `GameController.java` | Извлекает значение из пути URL (то, что попало в `{uuid}`) |
| `@RequestBody BoardDTO board` | `GameController.java` | Jackson (библиотека JSON) автоматически десериализует тело запроса в объект `BoardDTO` |
| `@Configuration` | `AppConfig.java` | Указывает, что класс содержит определения бинов (методы с `@Bean`) |
| `@Bean` | `AppConfig.java` | Говорит: результат этого метода — бин, которым будет управлять Spring |

### Spring Boot Starter'ы

В `build.gradle.kts` подключены:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-web")
```

`spring-boot-starter-web` — это набор зависимостей, которые вместе дают готовое веб-приложение:
- Tomcat (встроенный сервер)
- Spring MVC (веб-фреймворк)
- Jackson (сериализация/десериализация JSON)
- Валидация

---

## 3. REST API

**REST** (Representational State Transfer) — архитектурный стиль для построения API.

### Принципы REST

1. **Ресурсы** — каждый объект (игра, пользователь) — это ресурс с уникальным URL
2. **HTTP-методы** — операции над ресурсами через стандартные глаголы:
   - `GET` — получить
   - `POST` — создать или обработать
   - `PUT` — заменить
   - `DELETE` — удалить
3. **Stateless** — каждый запрос содержит всю информацию, сервер не хранит сессию клиента
4. **Единый интерфейс** — один URL, разные методы, один формат (у нас — JSON)

### Как REST реализован в проекте

#### Единственный эндпоинт

```
POST /game/{uuid}
```

- `uuid` — идентификатор игры (ресурс)
- `POST` — идемпотентно-нейтральная операция: каждый вызов двигает игру вперёд
- Тело запроса: `{"board": [[...]]}` — текущая доска с ходом пользователя
- Тело ответа (успех): `{"board": [[...]]}` — доска с ходом компьютера
- Тело ответа (ошибка): `{"error": "Invalid move"}` — описание проблемы
- Тело ответа (игра завершена): `{"message": "Game is already ended"}` — уведомление

#### Код контроллера

```java
@PostMapping("/game/{uuid}")
public ResponseEntity<?> move(
        @PathVariable UUID uuid,
        @RequestBody BoardDTO board) {

    Game game = WebMapper.toDomain(uuid, board);

    if (game.board().grid().length != 3) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid move"));
    }

    if (gameService.isEndedGame(game)) {
        return ResponseEntity.ok(Map.of("message", "Game is already ended"));
    }

    if (!gameService.isValidGame(game)) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid move"));
    }

    Game result = gameService.computerMove(game);
    return ResponseEntity.ok(WebMapper.toDTO(result.board()));
}
```

#### Почему не GET, не PUT?

Мы могли бы использовать `PUT /game/{uuid}` для отправки хода, так как мы заменяем состояние игры. Но `POST` — гибче: он позволяет выполнять действия (validate + move + save за один вызов), не требуя идемпотентности.

#### Статус-коды

- **200** — ход принят и обработан, или игра уже завершена
- **400** — неверный запрос (пустая доска, неверная размерность, попытка перезаписать клетку)

---

## 4. MVC (Model-View-Controller)

**MVC** — паттерн разделения ответственности. В веб-приложениях он трансформировался:

| Компонент | В классическом MVC | В нашем проекте |
|-----------|-------------------|-----------------|
| **Model** | Данные и бизнес-логика | `domain/model/` (Board, Game, Move) + `domain/service/` |
| **View** | Отображение данных | JSON-ответ (сериализованный Jackson'ом) |
| **Controller** | Обработка ввода пользователя | `web/controller/GameController.java` |

### Как это работает в проекте

1. **Клиент** отправляет POST на `/game/{uuid}` с JSON-телом
2. **Controller** принимает запрос, преобразует DTO в доменную модель через маппер
3. **Controller** вызывает **Service** (бизнес-логика)
4. **Service** вызывает **Repository** (работа с данными)
5. **Controller** преобразует результат обратно в DTO и возвращает JSON

### Почему MVC?

- **Разделение ответственности** — контроллер не знает про алгоритм minimax, сервис не знает про HTTP
- **Тестируемость** — можно тестировать сервис без запуска сервера
- **Заменяемость** — можно сменить формат вывода (JSON → XML), не трогая логику

---

## 5. Domain-Driven Design (DDD)

**DDD** — подход, при котором код отражает предметную область (домен). Базовая идея: сложность бизнес-логики не должна размазываться по инфраструктурному коду.

### Слои в проекте

```
┌──────────────────────────────────────────────┐
│            web (controller, model, mapper)    │ ← Связь с внешним миром
├──────────────────────────────────────────────┤
│            domain (model, service)            │ ← Бизнес-логика, ядро
├──────────────────────────────────────────────┤
│          datasource (model, repository, mapper)│ ← Хранение данных
├──────────────────────────────────────────────┤
│            di (dependency injection)          │ ← Сборка приложения
└──────────────────────────────────────────────┘
```

#### domain — ядро системы

Содержит **чистую бизнес-логику**. Не зависит от:
- Spring
- Базы данных
- HTTP

```java
// domain/model/Game.java — чистый Java record, только поля + методы домена
public record Game(UUID uuid, Board board) {
    public List<Move> getAvailableMoves() { ... }
    public Board placeMark(Move mv, int player) { ... }
    public int getWinner() { ... }
}

// domain/service/GameService.java — интерфейс, контракт сервиса
public interface GameService {
    Game computerMove(Game game);
    boolean isValidGame(Game game);
    boolean isEndedGame(Game game);
}
```

#### datasource — инфраструктура хранения

Содержит реализации интерфейсов, объявленных в domain.

```java
// datasource/model/GameEntity.java — сущность для хранения
public record GameEntity(UUID uuid, BoardEntity board) {}

// datasource/repository/RepositoryImpl.java — реализация репозитория
public class RepositoryImpl implements Repository {
    private final Storage storage;

    @Override
    public void saveGame(Game game) {
        GameEntity entity = DatasourceMapper.toEntity(game);
        storage.saveBoard(entity.uuid(), entity.board());
    }
}
```

#### web — слой связи с клиентом

Содержит DTO (объекты передачи данных) и мапперы.

```java
// web/model/BoardDTO.java
public record BoardDTO(int[][] board) {}

// web/mapper/WebMapper.java — преобразование между Game и DTO
public static Game toDomain(UUID uuid, BoardDTO board) {
    return new Game(uuid, toDomain(board));
}
```

#### Почему DTO отдельно?

В проекте `web/model/BoardDTO` и `datasource/model/BoardEntity` содержат одинаковые поля (`int[][]`). Зачем копировать?

1. **Независимость слоёв** — `Board` (domain) может измениться, не ломая формат API
2. **Безопасность** — DTO содержит только то, что хотим показать клиенту. Никаких паролей, внутренних ID
3. **Контракт** — API — это контракт с внешним миром. DTO — явное описание этого контракта

### Ports & Adapters (Hexagonal Architecture)

`domain/service/Repository.java` — это **port** (порт). Абстракция, которую требует бизнес-логика.
`datasource/repository/RepositoryImpl.java` — это **adapter** (адаптер). Конкретная реализация.

Бизнес-логика говорит: "Мне нужно уметь сохранять и загружать игры" (порт).
Инфраструктура отвечает: "Вот реализация этого через ConcurrentHashMap" (адаптер).

Можно заменить `ConcurrentHashMap` на PostgreSQL — для бизнес-логики ничего не поменяется.

---

## 6. SOLID

Разберём, как принципы SOLID применены в проекте.

### S — Single Responsibility

У каждого класса одна причина для изменения.

- `Board` — только хранение и проверка заполненности доски
- `Game` — только ходы и определение победителя
- `GameController` — только обработка HTTP-запросов
- `GameServiceImpl` — только business logic (minimax, валидация)
- `Storage` — только хранение игр в памяти

Если мы захотим изменить формат API, мы трогаем только web-слой. Если захотим изменить алгоритм — только сервис.

### O — Open/Closed

Классы открыты для расширения, закрыты для изменения.

- `GameService` — интерфейс. Можно добавить новую реализацию, не трогая существующую
- `Repository` — интерфейс. `RepositoryImpl` можно заменить на `PostgresRepositoryImpl`

```java
// Open for extension:
public interface Repository {
    void saveGame(Game game);
    Game loadGame(UUID uuid);
}

// Можно добавить:
public class PostgresRepositoryImpl implements Repository { ... }
public class RedisRepositoryImpl implements Repository { ... }
```

### L — Liskov Substitution

Любая реализация интерфейса может быть заменена другой без изменения корректности программы.

```java
// AppConfig — можно подставить любую реализацию Repository
@Bean
GameService GameServiceImpl(Repository repo) {
    return new GameServiceImpl(repo); // repo может быть любой реализацией
}
```

### I — Interface Segregation

Интерфейсы должны быть маленькими и специфичными.

```java
// Repository содержит только 2 метода — минимально необходимый набор
public interface Repository {
    void saveGame(Game game);
    Game loadGame(UUID uuid);
}

// GameService — отдельный интерфейс для бизнес-логики
public interface GameService {
    Game computerMove(Game game);
    boolean isValidGame(Game game);
    boolean isEndedGame(Game game);
}
```

Если бы мы смешали всё в один интерфейс, каждая реализация была бы вынуждена определять все методы.

### D — Dependency Inversion

Модули верхнего уровня не должны зависеть от модулей нижнего уровня. Оба должны зависеть от абстракций.

**Нарушение** (было бы без DDD):
```
GameServiceImpl → RepositoryImpl (зависит от конкретного класса)
```

**Исправление** (как сделано):
```
GameServiceImpl → Repository (интерфейс)
RepositoryImpl → Repository (интерфейс)
```

Оба зависят от абстракции `Repository`, а не друг от друга.

---

## 7. Алгоритм Minimax

**Minimax** — алгоритм принятия решений для игр с нулевой суммой (tic-tac-toe, шахматы, го). Игра с **нулевой суммой** означает: выигрыш одного игрока = проигрыш другого. Нет ситуации, где выигрывают оба.

### Идея

Алгоритм моделирует все возможные ходы до конца игры, выбирая:

- **Максимизирующий игрок** (компьютер, -1) — хочет максимизировать счёт
- **Минимизирующий игрок** (человек, 1) — хочет минимизировать счёт

### Реализация в проекте

```java
private int minimax(Game game, int depth, boolean isMax) {
    int score = evaluate(game);               // оценить текущую позицию

    if (score == 10) return score - depth;    // компьютер победил — чем быстрее, тем лучше
    if (score == -10) return score + depth;   // человек победил — чем медленнее, тем лучше
    if (game.board().isFull()) return 0;      // ничья

    if (isMax) {
        int best = Integer.MIN_VALUE;
        for (Move move : game.getAvailableMoves()) {
            Board newBoard = game.placeMark(move, -1);  // компьютер: -1
            Game next = new Game(game.uuid(), newBoard);
            best = Math.max(best, minimax(next, depth + 1, false));
        }
        return best;
    } else {
        int best = Integer.MAX_VALUE;
        for (Move move : game.getAvailableMoves()) {
            Board newBoard = game.placeMark(move, 1);   // человек: 1
            Game next = new Game(game.uuid(), newBoard);
            best = Math.min(best, minimax(next, depth + 1, true));
        }
        return best;
    }
}
```

### Функция оценки

```java
private int evaluate(Game game) {
    int winner = game.getWinner();
    if (winner == -1) return 10;    // компьютер выигрывает
    if (winner == 1) return -10;    // человек выигрывает
    return 0;                        // никто не выиграл
}
```

### Оптимизация: глубина

```java
if (score == 10) return score - depth;
if (score == -10) return score + depth;
```

Если компьютер может выиграть, он выберет самый быстрый путь (глубина меньше → score - depth больше).
Если компьютер проиграет, он отложит проигрыш как можно дальше (глубина больше → score + depth меньше).

Это свойство называется **предпочтение более быстрого выигрыша и более медленного проигрыша**.

### Дерево решений

Для крестиков-ноликов полное дерево поиска невелико: максимум 9! = 362880 состояний. Для шахмат это было бы ~10^120 — невозможно. Поэтому minimax на практике модифицируют:

- **Alpha-Beta pruning** — отсечение заведомо проигрышных ветвей
- **Ограничение глубины** — поиск до определённой глубины + эвристическая оценка

### Почему minimax, а не нейросеть?

Для крестиков-ноликов minimax **гарантирует** оптимальную игру. Нейросеть даёт вероятностный результат. Минимакс: если выигрыш существует — компьютер его найдёт. Если нет — сведёт к ничьей.

---

## 8. Сериализация и DTO

### Jackson

Когда Spring Boot получает JSON-запрос, Jackson автоматически превращает его в Java-объект:

```json
{"board": [[0,0,0],[0,1,0],[0,0,0]]}
```

```java
public record BoardDTO(int[][] board) {}
```

Jackson:
- Видит `{}` → создаёт `BoardDTO`
- Видит поле `board` → вызывает `BoardDTO()` конструктор с аргументом `[[0,0,0],[0,1,0],[0,0,0]]`
- Массив `int[]` в JSON превращается в `int[][]` в Java

### Мапперы

Разделение слоёв требует преобразования объектов. В проекте два маппера:

**WebMapper** — между доменом и слоем веб:

```java
public static BoardDTO toDTO(Board board) {
    return new BoardDTO(board.grid());       // Board → BoardDTO
}
public static Board toDomain(BoardDTO board) {
    return new Board(board.board());         // BoardDTO → Board
}
public static Game toDomain(UUID uuid, BoardDTO board) {
    return new Game(uuid, toDomain(board));  // UUID + BoardDTO → Game
}
```

**DatasourceMapper** — между доменом и слоем хранения:

```java
public static BoardEntity toEntity(Board board) {
    return new BoardEntity(board.grid());    // Board → BoardEntity
}
```

Мапперы статические — это чистые функции без состояния. Не нужно создавать экземпляр.

---

## 9. Thread Safety

### Проблема

Несколько пользователей могут играть одновременно. Каждый запрос обрабатывается в отдельном потоке. Если `Storage` не thread-safe, два потока могут одновременно читать/писать в мапу, вызывая race condition.

### Решение: ConcurrentHashMap

```java
public class Storage {
    private final ConcurrentHashMap<UUID, BoardEntity> store;

    public Storage() {
        this.store = new ConcurrentHashMap<>();
    }
}
```

`ConcurrentHashMap` — потокобезопасная версия `HashMap`. Она:
- Позволяет нескольким потокам читать одновременно
- Блокирует только конкретный сегмент при записи (fine-grained locking)
- Гарантирует, что `put` и `get` атомарны

### Почему не synchronized?

`synchronized` блокирует весь объект — все операции ждут освобождения. `ConcurrentHashMap` использует сегментирование: 16 независимых блокировок, каждая для своего набора ключей. Это масштабируется гораздо лучше.

### Unique Game UUID

```java
@PostMapping("/game/{uuid}")
```

Каждая игра идентифицируется UUID. UUID — это 128-битный идентификатор, который гарантированно уникален (практически). Два игрока могут начать игры параллельно, и их данные не пересекутся.

---

## 10. Dependency Injection Configuration

### Как Spring собирает приложение

```
@ComponentScan (@SpringBootApplication)
  └─ GameController (через @RestController)
       │  нужен GameService → Spring ищет подходящий бин
       ▼
AppConfig (@Configuration)
  ├─ @Bean RepositoryImpl     → создаёт Storage внутри
  └─ @Bean GameServiceImpl(repo)  → Spring передаёт бин RepositoryImpl
```

### Почему AppConfig

В сложных системах не все зависимости можно настроить через аннотации:
- Нужна специфическая логика инициализации
- Нужно явно указать, какую реализацию интерфейса использовать
- Есть сторонние библиотеки, классы которых нельзя пометить `@Component`

`@Configuration` — это явное описание графа зависимостей в одном месте.

---

## 11. Records в Java

Начиная с Java 16, **records** — компактный способ объявления неизменяемых классов данных.

```java
public record Board(int[][] grid) {}
```

Компилятор автоматически генерирует:
- Приватное финальное поле `grid`
- Публичный конструктор `Board(int[][] grid)`
- Геттер `grid()`
- `equals()` и `hashCode()` (по значению всех полей)
- `toString()`

Без record пришлось бы писать ~40 строк boilerplate кода.

Важное ограничение: record иммутабелен — нельзя изменить поле после создания. Для `int[][]` это означает, что сам массив можно изменить (ссылку не поменять, но содержимое — да). В `placeMark` создаётся глубокая копия:

```java
public Board placeMark(Move mv, int player) {
    Board newBoard = deepCopy(this.board);  // копия массива
    newBoard.grid()[mv.row()][mv.col()] = player;  // изменение копии
    return newBoard;  // новый Board с изменённым массивом
}
```

---

## 12. Как связаны все части

```
HTTP Request
     │
     ▼
┌─────────────────┐
│  GameController  │ ← получает JSON, маппит в Game
│  (@RestController)│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  GameService     │ ← бизнес-логика: валидация, minimax
│  (interface)     │
└────────┬────────┘
         │
         ▼
┌──────────────────────┐
│  GameServiceImpl     │ ← реализация minimax
│  (implements GameService) │
└────────┬─────────────┘
         │
         ▼
┌─────────────────┐
│  Repository      │ ← интерфейс для хранения
│  (interface)     │
└────────┬────────┘
         │
         ▼
┌──────────────────────┐
│  RepositoryImpl      │ ← хранит в ConcurrentHashMap
│  (implements Repository) │
└────────┬─────────────┘
         │
         ▼
┌─────────────────┐
│  Storage         │ ← ConcurrentHashMap<UUID, BoardEntity>
└─────────────────┘
```

**Поток данных для одного запроса:**

1. `GameController.move()` — получает UUID из URL, BoardDTO из тела
2. `WebMapper.toDomain(uuid, board)` — собирает `Game`
3. `gameService.isEndedGame(game)` — проверяет, не завершена ли игра
4. `gameService.isValidGame(game)` — загружает предыдущее состояние, сравнивает
5. `gameService.computerMove(game)` — minimax, сохраняет в repo, возвращает
6. `WebMapper.toDTO(result.board())` — маппит Game → BoardDTO
7. JSON-ответ клиенту

---

## Итог

Проект демонстрирует:

| Концепция | Реализация |
|-----------|-----------|
| Web-приложение | Spring Boot + Tomcat |
| REST API | `POST /game/{uuid}` с JSON |
| MVC | Controller → Service → Repository |
| Dependency Injection | `@Bean` + constructor injection |
| DDD / Layered Architecture | domain → datasource → web (изолированные слои) |
| SOLID | Интерфейсы, dependency inversion, single responsibility |
| Minimax | Полный перебор с оценкой глубины |
| Thread Safety | ConcurrentHashMap |
| Java Records | Board, Game, Move — компактные иммутабельные модели |
| Мапперы | Static методы, разделение DTO и Entity |
