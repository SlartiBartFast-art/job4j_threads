package ru.job4j.ref;

/**
 * 4. Thread без общих ресурсов [#267919]
 * Уровень : 3. Мидл Категория : 3.1. Multithreading Топик : 3.1.2. Общие ресурсы
 */
public class ShareNotSaffe {
    public static void main(String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("name");
        cache.add(user);
        Thread first = new Thread(
                () -> {
                    user.setName("rename");
                }
        );
        first.start();
        first.join();
        System.out.println(cache.findById(1).getName());
    }
}
