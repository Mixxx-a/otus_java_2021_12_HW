package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        cache.addListener(new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
        cache.addListener((key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action));
        HwListener<String, Integer> hwListener = (key, value, action) ->
                logger.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(hwListener);

        cache.put("1", 1);
        cache.remove("1");

        System.gc();

        cache.put("2", 2);

        cache.removeListener(hwListener);

        cache.put("3", 3);
    }
}
