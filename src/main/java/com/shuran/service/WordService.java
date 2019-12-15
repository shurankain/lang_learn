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

    private Logger logger = LoggerFactory.getLogger(WordService.class);

    private static final String FILE_NAME = "dictionary.txt";
    private Map<String, String> wordsCache = new HashMap<>();

    public WordService() {
        fillCacheFromFile();
    }

    Map<String, String> getWordsCache() {
        return wordsCache;
    }

    public String processWordSaving(Word word) {
        if (word == null || word.getForeign() == null ||
                "".equals(word.getForeign().trim()) || "".equals(word.getTranslation().trim())) {
            return "Invalid value!";
        } else {
            fillCacheFromFile(); //reloads words on each adding.
            if (wordsCache.containsKey(word.getForeign())) {
                return "Exists!";
            } else {
                saveWord(word);
                return "Added!";
            }
        }
    }

    private void saveWord(Word word) {
        wordsCache.put(word.getForeign(), word.getTranslation());

        File file = new File(FILE_NAME);
        try (FileWriter fr = new FileWriter(file, true)) {
            fr.write(word.getForeign() + "=" + word.getTranslation() + "\n");
        } catch (IOException e) {
            logger.debug("Error save to file: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    public void fillCacheFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(FILE_NAME);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line = reader.readLine();
                cleanWordsCache();
                while (line != null) {
                    String[] values = line.split("=");
                    if (values.length == 2) {
                        wordsCache.put(values[0], values[1]);
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
}
