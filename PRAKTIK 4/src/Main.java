import java.util.function.*;
import java.util.Random;
import java.lang.annotation.*;
import java.lang.reflect.*;

// ═══════════════════════════════════════════════════════
// 1. ЛЯМБДА-ВЫРАЖЕНИЯ
// ═══════════════════════════════════════════════════════

// 1.1 Интерфейс Printable
@FunctionalInterface
interface Printable {
    void print();
}

// Класс с примерами лямбда-выражений
class LambdaExamples {

    public static void main(String[] args) {
        System.out.println("=== 1.1 Лямбда для Printable ===");
        testPrintable();

        System.out.println("\n=== 1.2 Проверка пустой строки ===");
        testStringValidation();

        System.out.println("\n=== 1.3 Проверка строки (J/N ... A) ===");
        testStringCheck();

        System.out.println("\n=== 1.4 HeavyBox с Consumer ===");
        testHeavyBox();

        System.out.println("\n=== 1.5 Function (число -> текст) ===");
        testFunction();

        System.out.println("\n=== 1.6 Supplier (случайное число) ===");
        testSupplier();
    }

    // 1.1 Лямбда для Printable
    private static void testPrintable() {
        Printable printable = () -> System.out.println("Привет из лямбда-выражения!");
        printable.print();

        Printable printable2 = () -> {
            System.out.println("Многострочное лямбда-выражение");
            System.out.println("Вторая строка");
        };
        printable2.print();
    }

    // 1.2 Проверка пустой строки
    private static void testStringValidation() {
        Predicate<String> isNotNull = str -> str != null;
        Predicate<String> isNotEmpty = str -> !str.isEmpty();
        Predicate<String> isValid = isNotNull.and(isNotEmpty);

        String[] testStrings = {null, "", "Hello", "   "};

        for (String str : testStrings) {
            boolean result = isValid.test(str);
            System.out.println("Строка: " + str + " | Валидна: " + result);
        }

        String userInput = "Java";
        if (isValid.test(userInput)) {
            System.out.println("✓ Строка '" + userInput + "' корректна");
        }
    }

    // 1.3 Проверка строки (начинается с J/N, заканчивается на A)
    private static void testStringCheck() {
        Predicate<String> startsWithJorN = str -> str != null &&
                (str.startsWith("J") || str.startsWith("N"));

        Predicate<String> endsWithA = str -> str != null && str.endsWith("A");

        Predicate<String> isValid = startsWithJorN.and(endsWithA);

        String[] testStrings = {"JAVA", "JAVASCRIPT", "NODE", "KOTLIN", "NET", "JPA"};

        for (String str : testStrings) {
            boolean result = isValid.test(str);
            System.out.println("Строка: " + str + " | Подходит: " + result);
        }
    }

    // 1.4 HeavyBox с Consumer и andThen
    private static void testHeavyBox() {
        class HeavyBox {
            private int weight;

            public HeavyBox(int weight) {
                this.weight = weight;
            }

            public int getWeight() {
                return weight;
            }
        }

        Consumer<HeavyBox> shipBox = box ->
                System.out.println("Отправляем ящик с весом " + box.getWeight());

        Consumer<HeavyBox> receiveBox = box ->
                System.out.println("Отгрузили ящик с весом " + box.getWeight());

        Consumer<HeavyBox> fullProcess = shipBox.andThen(receiveBox);

        HeavyBox box1 = new HeavyBox(50);
        HeavyBox box2 = new HeavyBox(100);

        System.out.println("Обработка ящика 1:");
        fullProcess.accept(box1);

        System.out.println("\nОбработка ящика 2:");
        fullProcess.accept(box2);
    }

    // 1.5 Function (число -> текст)
    private static void testFunction() {
        Function<Integer, String> numberToText = num -> {
            if (num > 0) {
                return "Положительное число";
            } else if (num < 0) {
                return "Отрицательное число";
            } else {
                return "Ноль";
            }
        };

        Integer[] numbers = {5, -3, 0, 100, -50};

        for (Integer num : numbers) {
            String result = numberToText.apply(num);
            System.out.println(num + " -> " + result);
        }
    }

    // 1.6 Supplier (случайное число)
    private static void testSupplier() {
        Supplier<Integer> randomSupplier = () -> new Random().nextInt(11);

        System.out.println("5 случайных чисел от 0 до 10:");
        for (int i = 0; i < 5; i++) {
            System.out.print(randomSupplier.get() + " ");
        }
        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════
// 2. АННОТАЦИИ
// ═══════════════════════════════════════════════════════

// 2.1 Кастомная аннотация @DeprecatedEx
@interface DeprecatedEx {
    String message();
}

// 2.2 Кастомная аннотация @JsonField
@interface JsonField {
    String name();
}

// ═══════════════════════════════════════════════════════
// 3. ОБРАБОТЧИК АННОТАЦИЙ
// ═══════════════════════════════════════════════════════

class AnnotationProcessor {

    public static void processClass(Class<?> clazz) {
        System.out.println("=== Обработка класса: " + clazz.getSimpleName() + " ===\n");

        if (clazz.isAnnotationPresent(DeprecatedEx.class)) {
            DeprecatedEx deprecated = clazz.getAnnotation(DeprecatedEx.class);
            System.out.println("! класс '" + clazz.getSimpleName() +
                    "' устарел – альтернатива: '" + deprecated.message() + "'");
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DeprecatedEx.class)) {
                DeprecatedEx deprecated = method.getAnnotation(DeprecatedEx.class);
                System.out.println("! метод '" + method.getName() +
                        "' устарел – альтернатива: '" + deprecated.message() + "'");
            }
        }
    }

    public static void processObject(Object obj) {
        if (obj == null) {
            System.out.println("Объект null");
            return;
        }
        processClass(obj.getClass());
    }
}

// ═══════════════════════════════════════════════════════
// 4. JSON СЕРИАЛИЗАТОР
// ═══════════════════════════════════════════════════════

class JsonSerializer {

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }

        StringBuilder json = new StringBuilder();
        json.append("{");

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        boolean first = true;

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonField.class)) {
                JsonField jsonField = field.getAnnotation(JsonField.class);
                String jsonName = jsonField.name();

                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (!first) {
                        json.append(", ");
                    }
                    first = false;

                    String jsonValue = formatValue(value);
                    json.append("\"").append(jsonName).append("\": ").append(jsonValue);

                } catch (IllegalAccessException e) {
                    System.err.println("Ошибка доступа к полю: " + field.getName());
                }
            }
        }

        json.append("}");
        return json.toString();
    }

    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return "null";
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        boolean first = true;

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonField.class)) {
                JsonField jsonField = field.getAnnotation(JsonField.class);
                String jsonName = jsonField.name();

                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (!first) {
                        json.append(",\n");
                    }
                    first = false;

                    String jsonValue = formatValue(value);
                    json.append("  \"").append(jsonName).append("\": ").append(jsonValue);

                } catch (IllegalAccessException e) {
                    System.err.println("Ошибка доступа к полю: " + field.getName());
                }
            }
        }

        json.append("\n}");
        return json.toString();
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else {
            return "\"" + value.toString() + "\"";
        }
    }
}

// ═══════════════════════════════════════════════════════
// 5. ТЕСТОВЫЕ КЛАССЫ
// ═══════════════════════════════════════════════════════

@DeprecatedEx(message = "Используйте класс NewUserService вместо этого")
class UserService {

    @DeprecatedEx(message = "Используйте метод findById() вместо этого")
    public void getUserById(int id) {
        System.out.println("Получение пользователя по ID: " + id);
    }

    @DeprecatedEx(message = "Используйте метод saveUser() вместо этого")
    public void createUser(String name) {
        System.out.println("Создание пользователя: " + name);
    }

    public void deleteUser(int id) {
        System.out.println("Удаление пользователя: " + id);
    }
}

class NewUserService {
    public void findById(int id) {
        System.out.println("Поиск пользователя: " + id);
    }
}

class Person {
    @JsonField(name = "person_id")
    private int id;

    @JsonField(name = "full_name")
    private String name;

    @JsonField(name = "age_years")
    private int age;

    @JsonField(name = "is_active")
    private boolean active;

    private String password; // Без аннотации - не попадёт в JSON

    public Person(int id, String name, int age, boolean active, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.active = active;
        this.password = password;
    }
}

class Product {
    @JsonField(name = "product_code")
    private String code;

    @JsonField(name = "product_name")
    private String name;

    @JsonField(name = "price_usd")
    private double price;

    @JsonField(name = "in_stock")
    private int quantity;

    public Product(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

// ═══════════════════════════════════════════════════════
// 6. ГЛАВНЫЙ КЛАСС
// ═══════════════════════════════════════════════════════

public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║     ЛАМБДА-ВЫРАЖЕНИЯ, АННОТАЦИИ, РЕФЛЕКСИЯ        ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        // 1. Тестирование лямбда-выражений
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("ЧАСТЬ 1: ЛЯМБДА-ВЫРАЖЕНИЯ");
        System.out.println("═══════════════════════════════════════════════════════\n");

        LambdaExamples.main(args);

        // 2. Тестирование аннотаций и рефлексии
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("ЧАСТЬ 2: АННОТАЦИИ И РЕФЛЕКСИЯ");
        System.out.println("═══════════════════════════════════════════════════════\n");

        System.out.println("=== 2.1 Обработка аннотации @DeprecatedEx ===\n");
        testDeprecatedEx();

        System.out.println("\n=== 2.2 JSON Сериализация с @JsonField ===\n");
        testJsonSerialization();

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ!");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    private static void testDeprecatedEx() {
        UserService userService = new UserService();
        AnnotationProcessor.processObject(userService);

        System.out.println("\n--- Вызов методов ---");
        userService.getUserById(1);
        userService.createUser("John");
        userService.deleteUser(2);
    }

    private static void testJsonSerialization() {
        Person person = new Person(1, "John Doe", 30, true, "secret123");
        Product product = new Product("P001", "Laptop", 999.99, 50);

        System.out.println("Объект Person:");
        System.out.println(JsonSerializer.toPrettyJson(person));

        System.out.println("\nОбъект Product:");
        System.out.println(JsonSerializer.toPrettyJson(product));

        System.out.println("\nКомпактный JSON:");
        System.out.println(JsonSerializer.toJson(person));

        System.out.println("\nПримечание: поле 'password' не попало в JSON, " +
                "так как не имеет аннотации @JsonField");
    }
}