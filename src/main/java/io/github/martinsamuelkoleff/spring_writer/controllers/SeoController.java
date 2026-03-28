package io.github.martinsamuelkoleff.spring_writer.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;

@Controller
public class SeoController {

    private final PostRepository postRepository;

    public SeoController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        List<Post> posts = postRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        sb.append("<url>")
          .append("<loc>https://springwriter.com/</loc>")
          .append("<changefreq>weekly</changefreq>")
          .append("<priority>1.0</priority>")
          .append("</url>");

        for (Post post : posts) {
            sb.append("<url>")
              .append("<loc>https://springwriter.com/blog/")
              .append(post.getSlug())
              .append("</loc>")
              .append("<changefreq>monthly</changefreq>")
              .append("<priority>0.8</priority>")
              .append("</url>");
        }

        sb.append("</urlset>");
        return sb.toString();
    }
}