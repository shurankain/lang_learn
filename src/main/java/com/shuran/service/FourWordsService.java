package com.shuran.service;

import com.shuran.model.Word;
import com.shuran.model.ExerciseType;
import com.shuran.model.FourWordsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FourWordsService {

    private static final Random RANDOM = new Random();

    private final WordService wordService;

    @Autowired
    public FourWordsService(WordService wordService) {
        this.wordService = wordService;
    }

    public FourWordsDto getFourWords(ExerciseType exerciseType) {
        Map<String, String> wordsCache = wordService.getWordsCache();
        FourWordsDto fourWordsDto = new FourWordsDto();

        if (wordsCache.size() < 4) {
            return fourWordsDto;
        }
        List<Word> words = new ArrayList<>();
        List<String> cacheKeysList = new ArrayList<>(wordsCache.keySet());

        while (words.size() < 4) {
            Word extractedWord = new Word();
            extractedWord.setForeign(cacheKeysList.get(RANDOM.nextInt(cacheKeysList.size())));
            extractedWord.setTranslation(wordsCache.get(extractedWord.getForeign()));

            if (words.stream().noneMatch(word -> word.getForeign().equals(extractedWord.getForeign()))) {
                words.add(extractedWord);
            }
        }
        List<String> translations = new ArrayList<>();
        Collections.shuffle(words);
        if (ExerciseType.FT == exerciseType) {
            words.forEach(w -> translations.add(w.getTranslation()));
        }
        if (ExerciseType.TF == exerciseType) {
            words.forEach(w -> translations.add(w.getForeign()));
        }

        fourWordsDto.setOptions(translations);
        return fourWordsDto;
    }

    public void removeCorrectWordFromCache(String foreign) {
        Map<String, String> wordsCache = wordService.getWordsCache();
        wordsCache.remove(foreign);
    }
}
