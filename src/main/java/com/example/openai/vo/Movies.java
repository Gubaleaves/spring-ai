package com.example.openai.vo;

import lombok.Data;

import java.util.List;

@Data
public class Movies {

    List<Movie> movies;

    @Override
    public String toString() {
        return "Movies{" +
                "movies=" + movies +
                '}';
    }

    @Data
    static class Movie{

        private String name;

        private String releaseTime;

        private String reason;

        @Override
        public String toString() {
            return "Movie{" +
                    "name='" + name + '\'' +
                    ", releaseTime='" + releaseTime + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }
}
