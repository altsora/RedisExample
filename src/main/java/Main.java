public class Main {
    public static void main(String[] args) throws InterruptedException {
        MeetingSite site = new MeetingSite(JedisUtil.getJedis());
        site.open();
        site.monitoring();
        site.close();
        JedisUtil.destroyPool();
    }
}
