package com.shuran.service;

import com.shuran.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordService {
    private final Logger logger = LoggerFactory.getLogger(WordService.class);

    private Map<String, Integer> wordsBlackList = new HashMap<>();
    private Map<String, String> wordsCache = new HashMap<>();

    private static final String DICTIONARY_FILE = "dictionary.txt";
    private static final String BLACK_LIST_FILE = "black_list.txt";
    private static final int CORRECT_ANSWERS_CAP = 3;
    public static final String SPLITERATOR = "=";

    public WordService() {
        fillBlackListFromFile();
        fillDictCacheFromFile();
    }

    Map<String, String> getWordsCache() {
        return wordsCache;
    }

    public String processWordSaving(Word word) {
        if (word == null || isNullOrEmpty(word.getForeign()) || isNullOrEmpty(word.getTranslation())) {
            return "Invalid value!";
        } else {
            fillDictCacheFromFile(); //reloads words on each adding.
            if (wordsCache.containsKey(word.getForeign())) {
                return "Exists!";
            } else {
                saveWordToDictionary(word);
                return "Added!";
            }
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void saveWordToDictionary(Word word) {
        wordsCache.put(word.getForeign(), word.getTranslation());

        File file = new File(DICTIONARY_FILE);
        try (FileWriter fr = new FileWriter(file, true)) {
            fr.write(word.getForeign() + SPLITERATOR + word.getTranslation() + "\n");
        } catch (IOException e) {
            logger.debug("Error save to file: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    private void saveNewWordToBlackList(String key) {
        wordsBlackList.put(key, 1);

        File file = new File(BLACK_LIST_FILE);
        try (FileWriter fr = new FileWriter(file, true)) {
            fr.write(key + SPLITERATOR + 1 + "\n");
        } catch (IOException e) {
            logger.debug("Error save to file: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void updateWholeBlackList() throws IOException {
        File file = new File(BLACK_LIST_FILE);
        file.delete();
        file.createNewFile();
        try (FileWriter fr = new FileWriter(file, true)) {
            for (String key : wordsBlackList.keySet()) {
                fr.write(key + SPLITERATOR + wordsBlackList.get(key) + "\n");
            }
        } catch (IOException e) {
            logger.debug("Error save to file: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    public void addOrIncrementWordInBlackList(String key) {
        if (wordsBlackList.containsKey(key)) {
            Integer counter = wordsBlackList.get(key);
            if(counter == 3){ //if CAP was reached, remove word from Cache
                wordsCache.remove(key);
            }
            wordsBlackList.put(key, counter);
            try {
                updateWholeBlackList();
            } catch (IOException e) {
                logger.debug("Error updating blackList: {}", Arrays.toString(e.getStackTrace()));
            }
        } else {
            saveNewWordToBlackList(key);
        }
    }

    public void fillDictCacheFromFile() {
        File file = new File(DICTIONARY_FILE);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(DICTIONARY_FILE);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line = reader.readLine();
                cleanWordsCache();
                while (line != null && !line.isEmpty()) {
                    String[] values = line.split(SPLITERATOR);
                    if (values.length == 2 &&
                            !wordsBlackList.containsKey(values[0]) //if there is no this value in black list of < than CAP
                            || wordsBlackList.get(values[0]) < CORRECT_ANSWERS_CAP) {
                        wordsCache.put(values[0], values[1]);
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                logger.debug("Error read from file: {}", Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public void fillBlackListFromFile() {
        File file = new File(BLACK_LIST_FILE);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(BLACK_LIST_FILE);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line = reader.readLine();
                cleanBlackList();
                while (line != null && !line.isEmpty()) {
                    String[] values = line.split(SPLITERATOR);
                    if (values.length == 2) {
                        wordsBlackList.put(values[0], Integer.parseInt(values[1]));
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                logger.debug("Error read from file: {}", Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void cleanWordsCache() {
        wordsCache = new HashMap<>();
    }

    private void cleanBlackList() {
        wordsBlackList = new HashMap<>();
    }

}
