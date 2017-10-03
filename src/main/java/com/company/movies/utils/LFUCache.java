package com.company.movies.utils;

import com.company.movies.transferobjects.impl.MovieDetailsDTO;

import java.util.LinkedHashMap;
import java.util.Map;


public class LFUCache {


    private static LFUCache instance = null;
    private static int initialCapacity = 10;
    private static LinkedHashMap<Integer, CacheEntry> cacheMap = new LinkedHashMap<>();


    private LFUCache() {

    }

    public LFUCache(int initialCapacity) {
        LFUCache.initialCapacity = initialCapacity;
    }

    public static LFUCache getInstance() {
        if (instance == null) {
            instance = new LFUCache();

        }
        return instance;
    }

    public static boolean isFull() {
        return cacheMap.size() == initialCapacity;

    }

    public void addCacheEntry(int key, MovieDetailsDTO movie) {
        if (!isFull()) {
            CacheEntry temp = new CacheEntry();
            temp.setMovie(movie);
            temp.setFrequency(0);

            cacheMap.put(key, temp);
        } else {
            int entryKeyToBeRemoved = getLFUKey();
            cacheMap.remove(entryKeyToBeRemoved);

            CacheEntry temp = new CacheEntry();
            temp.setMovie(movie);
            temp.setFrequency(0);

            cacheMap.put(key, temp);
        }
    }

    public int getLFUKey() {
        int key = 0;
        int minFreq = Integer.MAX_VALUE;

        for (Map.Entry<Integer, CacheEntry> entry : cacheMap.entrySet()) {
            if (minFreq > entry.getValue().frequency) {
                key = entry.getKey();
                minFreq = entry.getValue().frequency;
            }
        }

        return key;
    }

    public MovieDetailsDTO getCacheEntry(int key) {
        if (cacheMap.containsKey(key)) {
            CacheEntry temp = cacheMap.get(key);
            temp.frequency++;
            cacheMap.put(key, temp);
            return temp.movie;
        }
        return null;
    }

    class CacheEntry {
        private MovieDetailsDTO movie;
        private int frequency;


        private CacheEntry() {
        }

        public MovieDetailsDTO getMovie() {
            return movie;
        }

        void setMovie(MovieDetailsDTO movie) {
            this.movie = movie;
        }

        public int getFrequency() {
            return frequency;
        }

        void setFrequency(int frequency) {
            this.frequency = frequency;
        }

    }

}
