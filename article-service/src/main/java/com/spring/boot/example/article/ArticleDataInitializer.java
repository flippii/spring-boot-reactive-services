package com.spring.boot.example.article;

import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.core.initializer.DataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleDataInitializer implements DataInitializer {

    private final ArticleRepository articleRepository;

    @Override
    public void initialize() {
        Article javaMagazin = createArticle("Java Magazin", "Das Java Magazin begleitet seit über 20 Jahren alle wesentlichen " +
                        "Entwicklungen der Java-Welt aktuell, kritisch und mit großer praktischer Relevanz.", "");

        Article entwicklerMagazin = createArticle("Entwickler Magazin", "Das Entwickler Magazin bietet Software-Entwicklern Einblicke " +
                        "und Orientierung in einem Markt an Entwicklungstechnologien, -tools und -ansätzen, der stets ...","");

        Article phpMagazin = createArticle("PHP Magazin", "Das PHP Magazin liefert die gesamte Bandbreite an Wissen, das für moderne " +
                        "Webanwendungen benötigt wird – von PHP-Programmierthemen über JavaScript ...", "");

        Article javascriptMagazin = createArticle("Javascript Magazin", "Das JavaScript Magazine: Neu & kostenlos zum Download! " +
                        "Ausgabe 1 befasst sich mit Angular: Features im Überblick, Ivy und Code Smells.", "");

        articleRepository.deleteAll()
                .thenMany(
                        Flux
                                .just(javaMagazin, entwicklerMagazin, phpMagazin, javascriptMagazin)
                                .flatMap(articleRepository::save)
                )
                .subscribe();
    }

    private Article createArticle(String title, String description, String body) {
        return new Article(title, description, body);
    }

}
