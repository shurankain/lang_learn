package hello.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.Word;
import hello.model.FourWordsDto;

@Service
public class FourWordsService {

    private static final Random RANDOM = new Random();

    private WordService wordService;

    @Autowired
    public FourWordsService(WordService wordService) {
        this.wordService = wordService;
    }

    public FourWordsDto getFourWords(){
        Map<String, String> wordsCache = wordService.getWordsCache();
        FourWordsDto fourWordsDto = new FourWordsDto();

        if(wordsCache.size() < 4){
            return fourWordsDto;
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
        List<String> transalations = new ArrayList<>();
        fourWordsDto.setQuestion(words.get(0).getForeign());
        fourWordsDto.setAnswer(words.get(0).getTranslation());
        Collections.shuffle(words);
        words.forEach(w -> transalations.add(w.getTranslation()));


        fourWordsDto.setOptions(transalations);
        return fourWordsDto;
    }
}
