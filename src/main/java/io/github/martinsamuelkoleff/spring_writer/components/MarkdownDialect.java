package io.github.martinsamuelkoleff.spring_writer.components;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

@Component
public class MarkdownDialect extends AbstractProcessorDialect {

    public static final String DIALECT_PREFIX = "markdown";

    MarkdownDialect() {
        super("Markdown Dialect", DIALECT_PREFIX, 1000);
    }

    @Override
    public Set getProcessors(String dialectPrefix) {
        Set<MarkdownProcessor> processors = new HashSet<>();
        processors.add(new MarkdownProcessor(DIALECT_PREFIX));
        return processors;
    }

}
