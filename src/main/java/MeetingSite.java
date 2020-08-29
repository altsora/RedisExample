import redis.clients.jedis.Jedis;

import java.util.Random;

public class MeetingSite {
    private Jedis jedis;
    private static final String KEY = "MEETING SITE";
    private static final int USERS_COUNT = 10;

    public MeetingSite(Jedis jedis) {
        this.jedis = jedis;
    }

    public void open() {
        jedis.del(KEY);
        for (int i = 0; i < USERS_COUNT; i++) {
            jedis.rpush(KEY, String.valueOf(i + 1));
        }
        System.out.println("Сайт открылся! Первый запуск:");
        for (String user : jedis.lrange(KEY, 0, -1)) {
            System.out.println("Пользователь " + user);
        }
    }

    public void monitoring(int count) throws InterruptedException {
        System.out.println("\nМониторинг сайта:");
        for (int i = 0; i < count; i++) {
            int countService = 0;
            for (int j = 0; j < USERS_COUNT; j++) {
                if (countService < 2) {
                    boolean userOfService = new Random().nextBoolean();
                    if (userOfService) {
                        int var = USERS_COUNT - countService;
                        int randomIndex = new Random().nextInt(var);
                        String richUser = jedis.lindex(KEY, randomIndex);    // Случайный пользователь
                        jedis.lrem(KEY, 0, richUser);                  //Удаляем из списка
                        jedis.lpush(KEY, richUser);                         // Вставляем в начало очереди
                        countService++;
                        System.out.printf("\t$ Пользователь %s заказал платную услугу $%n", richUser);
                    }
                }
                String currentUser = jedis.lindex(KEY, 0);
                System.out.printf("\t- На главной странице показываем пользователя %s%n", currentUser);
                jedis.lrem(KEY, 0, currentUser);
                jedis.rpush(KEY, currentUser);          // Пользователь переходит с начала в конец
                Thread.sleep(300);
            }
            System.out.println("*\t*\t*");
            Thread.sleep(1000);
        }
    }

    public void monitoring() throws InterruptedException {
        monitoring(Integer.MAX_VALUE);
    }

    public void close() {
        jedis.del(KEY);
    }
}
