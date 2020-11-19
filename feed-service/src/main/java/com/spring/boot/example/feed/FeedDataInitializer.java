package com.spring.boot.example.feed;

import com.spring.boot.example.article.ArticleRepository;
import com.spring.boot.example.article.model.Article;
import com.spring.boot.example.article.model.Tag;
import com.spring.boot.example.core.initializer.DataInitializer;
import com.spring.boot.example.favourite.FavouriteRepository;
import com.spring.boot.example.favourite.model.Favourite;
import com.spring.boot.example.follow.FollowRepository;
import com.spring.boot.example.follow.model.FollowRelation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class FeedDataInitializer implements DataInitializer {

    private final ArticleRepository articleRepository;
    private final FavouriteRepository favouriteRepository;
    private final FollowRepository followRepository;

    @Override
    public void initialize() {
        List<Article> articles = List.of(
                createArticle("1",
                        "Java Magazin",
                        "java-magazin",
                        """                        
                                Das Java Magazin begleitet seit über 20 Jahren alle wesentlichen Entwicklungen der Java-Welt aktuell, 
                                kritisch und mit großer praktischer Relevanz.""",
                        new String[] {"Java", "Programmierung"}),
                createArticle("2",
                        "Entwickler Magazin",
                        "entwickler-magazin",
                        """
                               Das Entwickler Magazin bietet Software-Entwicklern Einblicke und Orientierung in einem Markt an 
                               Entwicklungstechnologien, -tools und -ansätzen, der stets ...""",
                        new String[] {"Java", "Programmierung", "PHP"}),
                createArticle( "3",
                        "PHP Magazin",
                        "php-magazin",
                        """
                               Das PHP Magazin liefert die gesamte Bandbreite an Wissen, das für moderne 
                               Webanwendungen benötigt wird – von PHP-Programmierthemen über JavaScript ...""",
                        new String[] {"PHP", "Programmierung"}),
                createArticle("4",
                        "Javascript Magazin",
                        "javascript-magazin",
                        """
                               Das JavaScript Magazine: Neu & kostenlos zum Download! Ausgabe 1 befasst sich 
                               mit Angular: Features im Überblick, Ivy und Code Smells.""",
                        new String[] {"Javascript", "Programmierung"}),
                createArticle("5",
                        "PC Magazin",
                        "pc-magazin",
                        """
                               Software und Hardware im Test, alles zu Browsern und Betriebssystemen, Smartphones und 
                               Computerspiele im Praxistest sowie Infos zu Datenschutz im ...""",
                        new String[] {"Windows", "PC"})
        );

        articleRepository.deleteAll()
                .thenMany(
                        Flux
                                .fromIterable(articles)
                                .flatMap(articleRepository::save)
                )
                .subscribe();

        List<Favourite> favourites = List.of(
                new Favourite("1", "1"),
                new Favourite("2", "1"),
                new Favourite("3", "1"),
                new Favourite("4", "1"),
                new Favourite("1", "2"),
                new Favourite("2", "2"),
                new Favourite("3", "2"),
                new Favourite("4", "2"),
                new Favourite("5", "2")
        );

        favouriteRepository.deleteAll()
                .thenMany(
                        Flux
                                .fromIterable(favourites)
                                .flatMap(favouriteRepository::save)
                )
                .subscribe();

        List<FollowRelation> followRelations = List.of(
                new FollowRelation("1", "2"),
                new FollowRelation("1", "3"),
                new FollowRelation("1", "4"),
                new FollowRelation("2", "1"),
                new FollowRelation("2", "3"),
                new FollowRelation("2", "4")
        );

        followRepository.deleteAll()
                .thenMany(
                        Flux
                                .fromIterable(followRelations)
                                .flatMap(followRepository::save)
                )
                .subscribe();
    }

    private Article createArticle(String articleId, String title, String slug, String description, String[] tags) {
        return new Article(articleId, slug, title, description, "", Arrays.stream(tags).map(Tag::new).collect(toSet()));
    }

}
