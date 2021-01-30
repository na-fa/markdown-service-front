package com.example.utils;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.parser.ParserEmulationProfile;

import org.owasp.html.PolicyFactory;
import org.owasp.html.HtmlPolicyBuilder;

import java.util.Arrays;
import java.util.List;

public class MarkDown {

  private String markdown;
  
  private String html;

  public MarkDown(String markdown) {
    this.markdown = markdown;
    parser();
    sanitize();
  }

  public void parser() {
    MutableDataSet options = new MutableDataSet();
    options.setFrom(ParserEmulationProfile.MARKDOWN);
    options.set(Parser.EXTENSIONS,
      Arrays.asList(
        AnchorLinkExtension.create(),
        StrikethroughExtension.create(),
        TablesExtension.create()
    ));

    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    Node document = parser.parse(this.markdown);
    this.html = renderer.render(document);
  }

  public void sanitize() {
    PolicyFactory policy = new HtmlPolicyBuilder()
    .allowElements(
      "h1","h2","h3","h4","h5","h6","p",
      "img","a","ol","ul","li","table","thead","tr","th","td",
      "code","strong","em","del","pre","blockquote","br","hr")
    .allowAttributes("id").onElements("h1","h2","h3","h4","h5","h6")
    .allowAttributes("src","alt","title").onElements("img")
    .allowAttributes("href","id").onElements("a")
    .allowAttributes("align").onElements("th","td")
    .allowAttributes("class").onElements("code")
    .allowUrlProtocols("https","http")
    .toFactory();
    this.html = policy.sanitize(this.html);
  }

  public String getMarkdown() {
    return markdown;
  }

  public void setMarkdown(String markdown) {
    this.markdown = markdown;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  @Override
  public String toString() {
    return "MarkDown{" +
      "markdown='" + markdown + '\'' +
      ", html='" + html + '\'' +
    '}';
  }

}