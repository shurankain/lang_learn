package hello.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.Word;

@Service
public class FourWordsService {

    private static final Random RANDOM = new Random();

    private WordService wordService;

    @Autowired
    public FourWordsService(WordService wordService) {
        this.wordService = wordService;
    }

    public List<Word> getFourWords(){
        Map<String, String> wordsCache = wordService.getWordsCache();
        if(wordsCache.size() < 4){
            return Collections.emptyList();
        }
        List<Word> words = new ArrayList<>();
        List<String> cacheKeysList = new ArrayList<>(wordsCache.keySet());

        while (words.size() < 4){
            Word extractedWord = new Word();
            extractedWord.setForeign(cacheKeysList.get(RANDOM.nextInt(cacheKeysList.size())));
            extractedWord.setTranslation(wordsCache.get(extractedWord.getForeign()));

            if(words.stream().noneMatch(word -> word.getForeign().equals(extractedWord.getForeign()))){
                words.add(extractedWord);
            }
        }
        return words;
    }
}
