package io.github.martinsamuelkoleff.spring_writer.components;

import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class MarkdownProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTR_NAME = "toHtml";
    private static final int PRECEDENCE = 10000;

    public MarkdownProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName,
            final String attributeValue,
            final IElementTagStructureHandler structureHandler) {

        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);
        final String markdownContent = (String) expression.execute(context);
        final String renderedMarkdown = renderMarkdown(markdownContent);
        structureHandler.setBody(renderedMarkdown, false);
    }

    private String renderMarkdown(String markdown) {
        List<Extension> extensions = List.of(
            TablesExtension.create(),
            StrikethroughExtension.create(),
            AutolinkExtension.create(),
            HeadingAnchorExtension.create(),
            TaskListItemsExtension.create()
        );

        Parser parser = Parser.builder()
            .extensions(extensions)
            .build();

        HtmlRenderer renderer = HtmlRenderer.builder()
            .extensions(extensions)
            .build();

        return renderer.render(parser.parse(markdown));
    }

}
