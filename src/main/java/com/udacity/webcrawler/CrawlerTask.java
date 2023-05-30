package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class CrawlerTask extends RecursiveTask<CrawlResult> {
    private final String url;
    private final PageParserFactory parserFactory;
    private final Duration timeout;
    private final int maxDepth;
    private final List<Pattern> ignoredUrls;

    public CrawlerTask(
        String url,
        PageParserFactory parserFactory,
        @Timeout Duration timeout,
        @MaxDepth int maxDepth,
        List<Pattern> ignoredUrls) {
            this.url =  url;
            this.parserFactory = parserFactory;
            this.timeout = timeout;
            this.maxDepth = maxDepth;
            this.ignoredUrls = ignoredUrls;
    }

    @Override
    protected CrawlResult compute() {
        PageParser.Result result = parserFactory.get(url).parse();
        List<CrawlerTask> crawlerTasks = result.getLinks().stream()
                .map(i -> new CrawlerTask(i, parserFactory, timeout, maxDepth, ignoredUrls))
                .collect(Collectors.toList());
        return null;
    }
}
